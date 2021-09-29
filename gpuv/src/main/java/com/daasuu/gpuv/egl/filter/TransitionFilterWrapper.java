package com.daasuu.gpuv.egl.filter;

public class TransitionFilterWrapper {
    public static final int PRESSING = -1;

    private GlFilter glFilter;
    private long startFrame;
    private long endFrame; //end frame =-1 -> đang press

    public GlFilter getGlFilter() {
        return glFilter;
    }

    public void setGlFilter(GlFilter glFilter) {
        this.glFilter = glFilter;
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
