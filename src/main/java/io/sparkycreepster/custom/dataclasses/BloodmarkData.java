package io.sparkycreepster.custom.dataclasses;

public class BloodmarkData {
    private final int requiredHits;
    private boolean complete;
    private final long markStartTime;
    private long completeUntil;
    public void setComplete(boolean complete) {
        this.complete = complete;
    }
    public int getRequiredHits() {

        return this.requiredHits;
    }
    public boolean isComplete() {

        return this.complete;
    }
    public boolean hasExpired(long currentTime) {
        if (complete) {
            return currentTime > completeUntil;
        }

        return currentTime - markStartTime > 6000;
    }
    public void completeMark(long currentTime) {
        this.complete = true;
        this.completeUntil = currentTime + 2400;
    }

    public BloodmarkData(int requiredHits, long markStartTime) {
        this.requiredHits = requiredHits;
        this.markStartTime = markStartTime;

    }
}
