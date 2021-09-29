package com.daasuu.gpuv.egl.filter;

import android.opengl.GLES20;

import com.daasuu.gpuv.egl.GlFramebufferObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static android.opengl.GLES20.GL_FLOAT;

public class GlTransitionFilter extends GlFilter {

    private GlFilter currentFilter = new GlFilter();

    private GlFilter emptyFilter = new GlFilter(); //filter empty

    private List<TransitionFilterWrapper> transitionFilters = new ArrayList<>();

    private final Queue<Runnable> runOnDraw;

    public GlTransitionFilter() {
        runOnDraw = new LinkedList<>();

        //hard code to test start
        TransitionFilterWrapper filter1 = new TransitionFilterWrapper();
        filter1.setGlFilter(new GlDynamicFilter(8));
        filter1.setStartFrame(120);
        filter1.setEndFrame(180);


        TransitionFilterWrapper filter2 = new TransitionFilterWrapper();
        filter2.setGlFilter(new GlDynamicFilter(2));
        filter2.setStartFrame(240);
        filter2.setEndFrame(330);

        transitionFilters.add(filter1);
        transitionFilters.add(filter2);

        //hard code to test end
    }

    private void runAll(Queue<Runnable> queue) {
        synchronized (queue) {
            while (!queue.isEmpty()) {
                queue.poll().run();
            }
        }
    }

    @Override
    public int getProgram() {
        return currentFilter.getProgram();
    }

    private long currentFrame = 0L;

    public void seekFrame(long frame) {
        currentFrame = frame;
    }

    public void addFilter(TransitionFilterWrapper transitionFilterWrapper) {
        transitionFilters.add(transitionFilterWrapper);
    }

    private void changeFilter(GlFilter glFilter) {
        if (currentFilter == glFilter) return;
        currentFilter = glFilter;
        setVertexShaderSource(currentFilter.getVertexShaderSource());
        setFragmentShaderSource(currentFilter.getFragmentShaderSource());
        currentFilter.setup();
    }

    @Override
    public void setup() {
        setVertexShaderSource(currentFilter.getVertexShaderSource());
        setFragmentShaderSource(currentFilter.getFragmentShaderSource());
        currentFilter.setup();
    }

    private void runOnDraw(final Runnable runnable) {
        synchronized (runOnDraw) {
            runOnDraw.add(runnable);
        }
    }

    @Override
    protected void onDraw() {
        currentFrame++;
        checkFilter();

        currentFilter.onDraw();
    }

    private synchronized void checkFilter() {
        //for ngược để lấy thằng filter sau, nếu trùng thời gian thì filter sau sẽ được apply
        GlFilter expectedFilter = emptyFilter;
        for (int i = transitionFilters.size() - 1; i >= 0; i--) {
            TransitionFilterWrapper filterWrapper = transitionFilters.get(i);
            if ((filterWrapper.getEndFrame() == TransitionFilterWrapper.PRESSING) || (filterWrapper.getStartFrame() <= currentFrame && filterWrapper.getEndFrame() > currentFrame)) {
                expectedFilter = filterWrapper.getGlFilter();
                break;
            }
        }
        changeFilter(expectedFilter);
    }

    @Override
    public void setFrameSize(int width, int height) {
        super.setFrameSize(width, height);
        currentFilter.setFrameSize(width, height);
    }

    protected int getHandle(final String name) {
        final Integer value = currentFilter.getHandleMap().get(name);
        if (value != null) {
            return value;
        }

        int location = GLES20.glGetAttribLocation(currentFilter.getProgram(), name);
        if (location == -1) {
            location = GLES20.glGetUniformLocation(currentFilter.getProgram(), name);
        }
        if (location == -1) {
            throw new IllegalStateException("Could not get attrib or uniform location for " + name);
        }
        currentFilter.getHandleMap().put(name, Integer.valueOf(location));
        return location;
    }

    public void draw(final int texName, final GlFramebufferObject fbo) {
        runAll(runOnDraw);
        GLES20.glUseProgram(currentFilter.getProgram());

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, currentFilter.getVertexBufferName());
        GLES20.glEnableVertexAttribArray(getHandle("aPosition"));
        GLES20.glVertexAttribPointer(getHandle("aPosition"), VERTICES_DATA_POS_SIZE, GL_FLOAT, false, VERTICES_DATA_STRIDE_BYTES, VERTICES_DATA_POS_OFFSET);
        GLES20.glEnableVertexAttribArray(getHandle("aTextureCoord"));
        GLES20.glVertexAttribPointer(getHandle("aTextureCoord"), VERTICES_DATA_UV_SIZE, GL_FLOAT, false, VERTICES_DATA_STRIDE_BYTES, VERTICES_DATA_UV_OFFSET);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texName);
        GLES20.glUniform1i(getHandle("sTexture"), 0);

        onDraw();

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glDisableVertexAttribArray(getHandle("aPosition"));
        GLES20.glDisableVertexAttribArray(getHandle("aTextureCoord"));
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }
}
