package at.htlleonding.entity;

import java.util.Arrays;

public class Block {
    private int previousHash;
    private int blockHash;
    private String data;

    public Block (int previousHash, String data){
        this.previousHash = previousHash;
        this.data = data;

        Object[] contens = {Arrays.hashCode(data.toCharArray()), previousHash};
        this.blockHash = Arrays.hashCode(contens);
    }

    public int getPreviousHash() {
        return previousHash;
    }

    public String getData() {
        return data;
    }

    public int getBlockHash() {
        return blockHash;
    }
}
