package com.daasuu.gpuv.egl.filter;

public class TransactionObject {
    private GlFilter glFilter;
    private long startTime;
    private long endTime;

    public GlFilter getGlFilter() {
        return glFilter;
    }

    public void setGlFilter(GlFilter glFilter) {
        this.glFilter = glFilter;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
