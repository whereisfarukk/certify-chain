package com.example.certifychain.service;

import com.example.certifychain.model.Certificate;
import com.example.certifychain.util.HashUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CertificateService {

    private final BlockchainService blockchainService;
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${app.institution.secret:INSTITUTION_SECRET}")
    private String institutionSecret;

    public CertificateService(BlockchainService blockchainService) {
        this.blockchainService = blockchainService;
    }

    public Certificate issue(Certificate cert) {
        // assign id
        cert.setCertificateId(UUID.randomUUID().toString());
        // sign (HMAC) the JSON payload (without block hash)
        try {
            String payload = mapper.writeValueAsString(certWithoutBlockHash(cert));
            String sig = HashUtil.hmacSha256(institutionSecret, payload);
            cert.setIssuerSignature(sig);
            String blockHash = blockchainService.addBlock(payload).getHash();
            cert.setBlockHash(blockHash);
            return cert;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verify(String certificateId, String issuerSignature, String blockHash) {
        try {
            // naive verification: scan chain for block containing matching certificateId
            String needle = "\"certificateId\":\"" + certificateId + "\"";
            return blockchainService.getChain().stream()
                    .anyMatch(b -> b.getHash().equals(blockHash)
                            && b.getDataJson().contains(needle)
                            && HashUtil.hmacSha256(institutionSecret, b.getDataJson()).equals(issuerSignature))
                    && blockchainService.isValid();
        } catch (Exception e) {
            return false;
        }
    }

    private Certificate certWithoutBlockHash(Certificate c) {
        Certificate x = new Certificate();
        x.setStudentName(c.getStudentName());
        x.setInstitution(c.getInstitution());
        x.setProgram(c.getProgram());
        x.setYear(c.getYear());
        x.setGpa(c.getGpa());
        x.setCertificateId(c.getCertificateId());
        x.setIssuerSignature(null);
        x.setBlockHash(null);
        return x;
    }
}
