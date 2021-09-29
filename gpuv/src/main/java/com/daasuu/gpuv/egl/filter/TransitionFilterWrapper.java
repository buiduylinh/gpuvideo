package com.daasuu.gpuv.egl.filter;

public class TransitionFilterWrapper {
    public static final int PRESSING = -1;

    private int filterType;
    private long startFrame;
    private long endFrame; //end frame =-1 -> đang press

    public int getFilterType() {
        return filterType;
    }

    public void setFilterType(int filterType) {
        this.filterType = filterType;
    }

    public long getStartFrame() {
        return startFrame;
    }

    public void setStartFrame(long startFrame) {
        this.startFrame = startFrame;
    }

    public long getEndFrame() {
        return endFrame;
    }

    public void setEndFrame(long endFrame) {
        this.endFrame = endFrame;
    }
}
