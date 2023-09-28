package org.las2mile.scrcpy;

public class Options {
    private int maxSize;
    private int bitRate;
    private boolean turnScreenOff;
    private boolean tunnelForward;

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getBitRate() {
        return bitRate;
    }

    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    public boolean getTurnScreenOff() {
        return turnScreenOff;
    }

    public void setTurnScreenOff(boolean turnScreenOff) {
        this.turnScreenOff = turnScreenOff;
    }

    public boolean isTunnelForward() {
        return tunnelForward;
    }

    public void setTunnelForward(boolean tunnelForward) {
        this.tunnelForward = tunnelForward;
    }
}
