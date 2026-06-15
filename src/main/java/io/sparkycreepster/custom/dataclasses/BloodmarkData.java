package io.sparkycreepster.custom.dataclasses;

public class BloodmarkData {
    private int requiredHits;
    private boolean complete;
    private long markStartTime;
    private long completeUntil;

    public int getRequiredHits() {

        return this.requiredHits;
    }
    public boolean isComplete() {

        return this.complete;
    }
    public BloodmarkData(int requiredHits, long markStartTime) {
        this.requiredHits = requiredHits;
        this.markStartTime = markStartTime;

    }
}
