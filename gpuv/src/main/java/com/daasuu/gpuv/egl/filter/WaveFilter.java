package com.daasuu.gpuv.egl.filter;

import android.opengl.GLES20;

import java.nio.FloatBuffer;

public class WaveFilter extends GlFilter {

    public static final String BILATERAL_FRAGMENT_SHADER = "" +
            "precision mediump float;" +
            "varying vec2 vTextureCoord;\n" +
            "uniform lowp sampler2D sTexture;\n" +
            "uniform float motion;\n" +
            "uniform float angle;\n" +
            "uniform float strength;\n" +
            "void main()\n" +
            "{\n" +
            "   vec2 coord;\n" +
            "   coord.x = vTextureCoord.x + strength * sin(motion + vTextureCoord.x * angle);\n" +
            "   coord.y = vTextureCoord.y + strength * sin(motion + vTextureCoord.y * angle);\n" +
            "   gl_FragColor = texture2D(sTexture, coord);\n" +
            "}";


    public WaveFilter() {
        super(DEFAULT_VERTEX_SHADER, BILATERAL_FRAGMENT_SHADER);
    }

    private float motion = 0.0f;

    @Override
    protected void onDraw() {
        motion += 0.5f;
        GLES20.glUniform1f(getHandle("motion"), motion);
        if (motion > 3.14159f * 2) {
            motion -= 3.14159f * 2;
        }

        GLES20.glUniform1f(getHandle("angle"), 20.0f);
        GLES20.glUniform1f(getHandle("strength"), 0.01f);
    }
}
