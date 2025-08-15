package com.example.certifychain.model;

import java.time.Instant;

public class Block {
    private String index;
    private String timestamp;
    private String previousHash;
    private String dataJson;
    private String hash;

    public Block() {}

    public Block(String index, String previousHash, String dataJson, String hash) {
        this.index = index;
        this.timestamp = Instant.now().toString();
        this.previousHash = previousHash;
        this.dataJson = dataJson;
        this.hash = hash;
    }

    public String getIndex() { return index; }
    public void setIndex(String index) { this.index = index; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getPreviousHash() { return previousHash; }
    public void setPreviousHash(String previousHash) { this.previousHash = previousHash; }

    public String getDataJson() { return dataJson; }
    public void setDataJson(String dataJson) { this.dataJson = dataJson; }

    public String getHash() { return hash; }
    public void setHash(String hash) { this.hash = hash; }
}
