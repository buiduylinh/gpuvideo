package com.daasuu.gpuv.egl.filter;

import java.util.ArrayList;
import java.util.List;

public class GlTransitionFilter extends GlDynamicFilter {

    private List<TransitionFilterWrapper> transitionFilters = new ArrayList<>();

    public GlTransitionFilter() {
        super(EMPTY_FILTER);
        //hard code to test start
        TransitionFilterWrapper filter1 = new TransitionFilterWrapper();
        filter1.setFilterType(8);
        filter1.setStartFrame(120);
        filter1.setEndFrame(180);
        TransitionFilterWrapper filter2 = new TransitionFilterWrapper();
        filter2.setFilterType(2);
        filter2.setStartFrame(240);
        filter2.setEndFrame(330);
        transitionFilters.add(filter1);
        transitionFilters.add(filter2);
        //hard code to test end
    }

    private long currentFrame = 0L;

    public void seekFrame(long frame) {
        currentFrame = frame;
    }

    public void addFilter(TransitionFilterWrapper transitionFilterWrapper) {
        transitionFilters.add(transitionFilterWrapper);
    }

    @Override
    protected void onDraw() {
        super.onDraw();
        currentFrame++;
        checkFilter();
    }

    private synchronized void checkFilter() {
        //for ngược để lấy thằng filter sau, nếu trùng thời gian thì filter sau sẽ được apply
        int expectedFilterType = EMPTY_FILTER;
        for (int i = transitionFilters.size() - 1; i >= 0; i--) {
            TransitionFilterWrapper filterWrapper = transitionFilters.get(i);
            if ((filterWrapper.getEndFrame() == TransitionFilterWrapper.PRESSING) || (filterWrapper.getStartFrame() <= currentFrame && filterWrapper.getEndFrame() > currentFrame)) {
                expectedFilterType = filterWrapper.getFilterType();
                break;
            }
        }
        changeFilterType(expectedFilterType);
    }
}
