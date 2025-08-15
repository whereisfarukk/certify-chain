package com.example.certifychain.service;

import com.example.certifychain.model.Block;
import com.example.certifychain.util.HashUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
public class BlockchainService {
    private final List<Block> chain = new ArrayList<>();
    private final ObjectMapper mapper = new ObjectMapper();
    private final File storage = new File("chain.json");

    public BlockchainService() {
        load();
        if (chain.isEmpty()) {
            // genesis block
            String genesisData = "{\"genesis\":true}";
            String hash = HashUtil.sha256("0|" + genesisData);
            chain.add(new Block("0", "0", genesisData, hash));
            save();
        }
    }

    public synchronized Block addBlock(String dataJson) {
        Block last = chain.get(chain.size() - 1);
        String index = String.valueOf(chain.size());
        String toHash = last.getHash() + "|" + dataJson;
        String hash = HashUtil.sha256(toHash);
        Block b = new Block(index, last.getHash(), dataJson, hash);
        chain.add(b);
        save();
        return b;
    }

    public synchronized List<Block> getChain() { return new ArrayList<>(chain); }

    public synchronized boolean isValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block prev = chain.get(i - 1);
            Block curr = chain.get(i);
            String expected = HashUtil.sha256(prev.getHash() + "|" + curr.getDataJson());
            if (!curr.getPreviousHash().equals(prev.getHash())) return false;
            if (!curr.getHash().equals(expected)) return false;
        }
        return true;
    }

    private void save() {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(storage, chain);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void load() {
        try {
            if (storage.exists() && Files.size(storage.toPath()) > 0) {
                List<Block> loaded = mapper.readValue(storage, new TypeReference<List<Block>>(){});
                chain.clear();
                chain.addAll(loaded);
            }
        } catch (Exception e) {
            // start fresh if corrupted
            chain.clear();
        }
    }
}
