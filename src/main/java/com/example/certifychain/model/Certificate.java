package com.example.certifychain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Certificate {
    @NotBlank
    private String studentName;
    @NotBlank
    private String institution;
    @NotBlank
    private String program;
    @NotBlank
    private String year;
    @NotNull
    private Double gpa;

    // assigned by system
    private String certificateId;
    private String issuerSignature; // HMAC signature
    private String blockHash; // hash of block containing this cert

    public Certificate() {}

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getInstitution() { return institution; }
    public void setInstitution(String institution) { this.institution = institution; }

    public String getProgram() { return program; }
    public void setProgram(String program) { this.program = program; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public Double getGpa() { return gpa; }
    public void setGpa(Double gpa) { this.gpa = gpa; }

    public String getCertificateId() { return certificateId; }
    public void setCertificateId(String certificateId) { this.certificateId = certificateId; }

    public String getIssuerSignature() { return issuerSignature; }
    public void setIssuerSignature(String issuerSignature) { this.issuerSignature = issuerSignature; }

    public String getBlockHash() { return blockHash; }
    public void setBlockHash(String blockHash) { this.blockHash = blockHash; }
}
