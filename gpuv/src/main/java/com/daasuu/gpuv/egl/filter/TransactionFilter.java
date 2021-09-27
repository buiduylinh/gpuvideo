package com.daasuu.gpuv.egl.filter;

import android.opengl.GLES20;
import android.os.Handler;
import android.os.Looper;

import com.daasuu.gpuv.egl.GlFramebufferObject;

import java.util.LinkedList;
import java.util.Queue;

import static android.opengl.GLES20.GL_FLOAT;

public class TransactionFilter extends GlFilter {


    private GlFilter currentFilter = new GlFilter();

    private GlFilter glFilter = new GlFilter();

    private final Queue<Runnable> runOnDraw;

    public TransactionFilter() {
        runOnDraw = new LinkedList<>();
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

    private long timeStick = 0l;

    @Override
    public void setup() {

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                timeStick++;
                handler.postDelayed(this, 1000);
                if (timeStick == 5) {
                    currentFilter = new GlBulgeDistortionFilter();
                    setVertexShaderSource(TransactionFilter.this.currentFilter.getVertexShaderSource());
                    setFragmentShaderSource(TransactionFilter.this.currentFilter.getFragmentShaderSource());
                    runOnDraw(new Runnable() {
                        @Override
                        public void run() {
                            currentFilter.setup();
                        }
                    });
                }

                if (timeStick == 7) {
                    currentFilter = new GlFilter();
                    setVertexShaderSource(TransactionFilter.this.currentFilter.getVertexShaderSource());
                    setFragmentShaderSource(TransactionFilter.this.currentFilter.getFragmentShaderSource());
                    runOnDraw(new Runnable() {
                        @Override
                        public void run() {
                            currentFilter.setup();
                        }
                    });
                }

                if (timeStick == 14) {
                    currentFilter = new GlSwirlFilter();
                    setVertexShaderSource(TransactionFilter.this.currentFilter.getVertexShaderSource());
                    setFragmentShaderSource(TransactionFilter.this.currentFilter.getFragmentShaderSource());
                    runOnDraw(new Runnable() {
                        @Override
                        public void run() {
                            currentFilter.setup();
                        }
                    });
                }

                if (timeStick == 16) {
                    currentFilter = new GlFilter();
                    setVertexShaderSource(TransactionFilter.this.currentFilter.getVertexShaderSource());
                    setFragmentShaderSource(TransactionFilter.this.currentFilter.getFragmentShaderSource());
                    runOnDraw(new Runnable() {
                        @Override
                        public void run() {
                            currentFilter.setup();
                        }
                    });
                }
            }
        });


        setVertexShaderSource(this.currentFilter.getVertexShaderSource());
        setFragmentShaderSource(this.currentFilter.getFragmentShaderSource());
        currentFilter.setup();
    }

    private void runOnDraw(final Runnable runnable) {
        synchronized (runOnDraw) {
            runOnDraw.add(runnable);
        }
    }

    @Override
    protected void onDraw() {
        currentFilter.onDraw();
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
