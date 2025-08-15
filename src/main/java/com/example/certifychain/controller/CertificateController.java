package com.example.certifychain.controller;

import com.example.certifychain.model.Certificate;
import com.example.certifychain.service.BlockchainService;
import com.example.certifychain.service.CertificateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class CertificateController {

    private final CertificateService certificateService;
    private final BlockchainService blockchainService;

    public CertificateController(CertificateService certificateService, BlockchainService blockchainService) {
        this.certificateService = certificateService;
        this.blockchainService = blockchainService;
    }

    // Simple API key gating for issuing (institution only)
    @Value("${app.keys.institution:INSTITUTION_SECRET}")
    private String institutionKey;

    // Employers can verify
    @Value("${app.keys.employer:EMPLOYER_SECRET}")
    private String employerKey;

    private boolean isInstitution(String key) { return institutionKey.equals(key); }
    private boolean isEmployer(String key) { return employerKey.equals(key) || institutionKey.equals(key); }

    @PostMapping("/certs/issue")
    public ResponseEntity<?> issue(@RequestHeader("X-API-KEY") String apiKey, @Valid @RequestBody Certificate cert) {
        if (!isInstitution(apiKey)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error","Forbidden"));
        Certificate issued = certificateService.issue(cert);
        return ResponseEntity.ok(issued);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestHeader("X-API-KEY") String apiKey, @RequestBody Map<String,String> body) {
        if (!isEmployer(apiKey)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error","Forbidden"));
        String certificateId = body.get("certificateId");
        String issuerSignature = body.get("issuerSignature");
        String blockHash = body.get("blockHash");
        boolean ok = certificateService.verify(certificateId, issuerSignature, blockHash);
        return ResponseEntity.ok(Map.of("valid", ok));
    }

    @GetMapping("/chain")
    public ResponseEntity<?> chain() {
        return ResponseEntity.ok(blockchainService.getChain());
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() { return ResponseEntity.ok(Map.of("status","UP")); }
}
