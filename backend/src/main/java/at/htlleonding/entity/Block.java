package at.htlleonding.entity;

import java.util.Arrays;
import java.util.Date;

public class Block {
    private int blockHash;
    private final int previousHash;
    private final Date timeStamp = new Date();
    private final String data;

    public Block (int previousHash, String data){
        this.previousHash = previousHash;
        this.data = data;

        Object[] contens = {Arrays.hashCode(data.toCharArray()), previousHash, Arrays.hashCode(timeStamp.toString().toCharArray())};
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

    public Date getTimeStamp(){
        return timeStamp;
    }
}
