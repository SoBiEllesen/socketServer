package com.ixtens.client;

import java.util.concurrent.CompletableFuture;

public class PairTimestampFuture <F>{

    private final long timestamp;
    private CompletableFuture<F> future;
    private final long expireTime = 5000;

    public PairTimestampFuture(CompletableFuture<F> future) {
        this.future = future;
        this.timestamp = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public CompletableFuture<F> getFuture() {
        return future;
    }

    public void setFuture(CompletableFuture<F> future) {
        this.future = future;
    }

    public boolean isExpired(){
        return System.currentTimeMillis() - timestamp > expireTime;
    }
}
