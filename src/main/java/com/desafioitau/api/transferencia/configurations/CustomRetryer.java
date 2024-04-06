package com.desafioitau.api.transferencia.configurations;

import feign.RetryableException;
import feign.Retryer;

public class CustomRetryer implements Retryer {

    private final int maxAttempts;
    private final long backoff;
    private int attempt;

    public CustomRetryer() {
        this(3, 100);
    }

    public CustomRetryer(int maxAttempts, long backoff) {
        this.maxAttempts = maxAttempts;
        this.backoff = backoff;
        this.attempt = 1;
    }

    @Override
    public void continueOrPropagate(RetryableException ex) {
        if (ex.status() == 429 && attempt++ <= maxAttempts) {
            try {
                Thread.sleep(backoff);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        } else {
            throw ex;
        }
    }

    @Override
    public Retryer clone() {
        return new CustomRetryer(maxAttempts, backoff);
    }
}
