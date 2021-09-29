package com.daasuu.gpuv.egl.filter;

import android.opengl.GLES20;

import java.nio.FloatBuffer;

public class GlDynamicFilter extends GlFilter {

    public static final String TRANSITION_FRAGMENT_SHADER = "precision mediump float;\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "//varying mat4 sMatrix;\n" +
                    "uniform lowp sampler2D sTexture;\n" +
                    "uniform int type;\n" +
                    "uniform float wave[120];\n" +
                    "uniform float scalex;\n" +
                    "uniform float scaley;\n" +
                    "uniform float offset;\n" +
                    "uniform float offset2;\n" +
                    "uniform float random1;\n" +
                    "uniform int filterType;\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "highp float rand(in vec2 co, in float seed){\n" +
                    "    highp float a = 12.9898;\n" +
                    "    highp float b = 78.233;\n" +
                    "    highp float c = 43758.5453;\n" +
                    "    highp float dt= dot(co.xy ,vec2(a,b));\n" +
                    "    highp float sn= mod(dt,3.14);\n" +
                    "    return fract(sin(sn) * c * seed);\n" +
                    "}\n" +
                    "\n" +
                    "void main() {\n" +
                    "\n" +
                    "    vec2 coordUsed = vTextureCoord;\n" +
                    "    int finalType = type;\n" +
                    "    if(scalex<1.0){\n" +
                    "        coordUsed.x = coordUsed.x+((coordUsed.x-0.5)/scalex * (1.0-scalex));\n" +
                    "        if(coordUsed.x<0.0 || coordUsed.x>1.0){\n" +
                    "            finalType=99;\n" +
                    "        }\n" +
                    "    }\n" +
                    "    if(scaley<1.0){\n" +
                    "        coordUsed.y = coordUsed.y+((coordUsed.y-0.5)/scaley * (1.0-scaley));\n" +
                    "        if(coordUsed.y<0.0 || coordUsed.y>1.0){\n" +
                    "            finalType=99;\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "\n" +
                    "    highp vec4 textureColor = texture2D(sTexture, coordUsed);\n" +
                    "    float r = textureColor.r;\n" +
                    "    float g = textureColor.g;\n" +
                    "    float b = textureColor.b;\n" +
                    "\n" +
                    "    float r_shift = 0.0;\n" +
                    "    float g_shift = 0.0;\n" +
                    "    float b_shift = 0.0;\n" +
                    "\n" +
                    "\n" +
                    "    if(finalType==99){\n" +
                    "        r = 0.0;\n" +
                    "        g = 0.0;\n" +
                    "        b = 0.0;\n" +
                    "    }\n" +
                    "    else if(finalType==100){\n" +
                    "        r = 0.0;\n" +
                    "        g = 0.0;\n" +
                    "        b = 0.0;\n" +
                    "    }\n" +
                    "    else if(finalType==1){\n" +
                    "        for(int i=0;i<120;i+=6){\n" +
                    "            if(coordUsed.x>wave[i]-offset && coordUsed.x<wave[i+1]+offset && coordUsed.y>wave[i+2] && coordUsed.y<wave[i+3]){\n" +
                    "                if(wave[i+5]<0.3){\n" +
                    "                    r_shift+=wave[i+4];\n" +
                    "                    g_shift-=wave[i+4];\n" +
                    "                    b_shift-=wave[i+4];\n" +
                    "                }\n" +
                    "                else if(wave[i+5]<0.6){\n" +
                    "                    r_shift-=wave[i+4];\n" +
                    "                    g_shift+=wave[i+4];\n" +
                    "                    b_shift-=wave[i+4];\n" +
                    "                }\n" +
                    "                else{\n" +
                    "                    r_shift-=wave[i+4];\n" +
                    "                    g_shift-=wave[i+4];\n" +
                    "                    b_shift+=wave[i+4];\n" +
                    "                }\n" +
                    "                break;\n" +
                    "            }\n" +
                    "        }\n" +
                    "        highp vec2 wavecord_r = coordUsed+vec2(r_shift,0.00);\n" +
                    "        highp vec2 wavecord_g = coordUsed+vec2(g_shift,0.00);\n" +
                    "        highp vec2 wavecord_b = coordUsed+vec2(b_shift,0.00);\n" +
                    "        highp vec4 tc_r = texture2D(sTexture, wavecord_r);\n" +
                    "        highp vec4 tc_g = texture2D(sTexture, wavecord_g);\n" +
                    "        highp vec4 tc_b = texture2D(sTexture, wavecord_b);\n" +
                    "        r = tc_r.r;\n" +
                    "        g = tc_g.g;\n" +
                    "        b = tc_b.b;\n" +
                    "    }\n" +
                    "\n" +
                    "    else if(finalType==3){\n" +
                    "        r_shift = (wave[4])/2.0;\n" +
                    "        for(int i=0;i<120;i+=6){\n" +
                    "            if(coordUsed.y>wave[i+2] && coordUsed.y<wave[i+3]){\n" +
                    "                if(wave[i+5]<0.3){\n" +
                    "                    r_shift=wave[i+4];\n" +
                    "                    g_shift=wave[i+4];\n" +
                    "                    b_shift=wave[i+4];\n" +
                    "                    break;\n" +
                    "                }\n" +
                    "            }\n" +
                    "        }\n" +
                    "        highp vec2 wavecord_r = coordUsed+vec2(r_shift,0.00);\n" +
                    "        highp vec2 wavecord_g = coordUsed+vec2(g_shift,0.00);\n" +
                    "        highp vec2 wavecord_b = coordUsed+vec2(b_shift,0.00);\n" +
                    "        highp vec4 tc_r = texture2D(sTexture, wavecord_r);\n" +
                    "        highp vec4 tc_g = texture2D(sTexture, wavecord_g);\n" +
                    "        highp vec4 tc_b = texture2D(sTexture, wavecord_b);\n" +
                    "        r = tc_r.r;\n" +
                    "        g = tc_g.g;\n" +
                    "        b = tc_b.b;\n" +
                    "    }\n" +
                    "\n" +
                    "    else if(finalType==4){\n" +
                    "        g_shift = (offset2/50.0);\n" +
                    "        for(int i=0;i<120;i+=6){\n" +
                    "            if(coordUsed.y>wave[i+2]+offset && coordUsed.y<wave[i+3]+offset){\n" +
                    "                if(wave[i+5]<0.3){\n" +
                    "                    r_shift=wave[i+4];\n" +
                    "                    g_shift=wave[i+4];\n" +
                    "                    b_shift=wave[i+4];\n" +
                    "                    break;\n" +
                    "                }\n" +
                    "            }\n" +
                    "        }\n" +
                    "        highp vec2 wavecord_r = coordUsed+vec2(r_shift,0.00);\n" +
                    "        highp vec2 wavecord_g = coordUsed+vec2(g_shift,0.00);\n" +
                    "        highp vec2 wavecord_b = coordUsed+vec2(b_shift,0.00);\n" +
                    "        highp vec4 tc_r = texture2D(sTexture, wavecord_r);\n" +
                    "        highp vec4 tc_g = texture2D(sTexture, wavecord_g);\n" +
                    "        highp vec4 tc_b = texture2D(sTexture, wavecord_b);\n" +
                    "        r = tc_r.r;\n" +
                    "        g = tc_g.g;\n" +
                    "        b = tc_b.b;\n" +
                    "    }\n" +
                    "\n" +
                    "    else if(finalType==5){\n" +
                    "        highp vec2 wavecord_r = coordUsed+vec2(r_shift,0.00);\n" +
                    "        highp vec2 wavecord_g = coordUsed+vec2(g_shift,0.00);\n" +
                    "        highp vec2 wavecord_b = coordUsed+vec2(b_shift,0.00);\n" +
                    "        highp vec4 tc_r = texture2D(sTexture, wavecord_r);\n" +
                    "        highp vec4 tc_g = texture2D(sTexture, wavecord_g);\n" +
                    "        highp vec4 tc_b = texture2D(sTexture, wavecord_b);\n" +
                    "        r = tc_r.r;\n" +
                    "        g = tc_g.g;\n" +
                    "        b = tc_b.b;\n" +
                    "        float a = mod(offset,0.02);\n" +
                    "        if(mod(coordUsed.y+offset,0.02)<0.01){\n" +
                    "            r-=0.1;\n" +
                    "            g-=0.1;\n" +
                    "            b-=0.1;\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "    else if(finalType==7){\n" +
                    "\n" +
                    "        float sx2 = offset;\n" +
                    "        float sy2 = offset;\n" +
                    "\n" +
                    "        vec2 coordUsedZoom = vec2(0.0,0.0);\n" +
                    "        coordUsedZoom.x = coordUsed.x+((coordUsed.x-0.5)/sx2 * (1.0-sx2));\n" +
                    "        coordUsedZoom.y = coordUsed.y+((coordUsed.y-0.5)/sy2 * (1.0-sy2));\n" +
                    "\n" +
                    "        highp vec4 tca = texture2D(sTexture, coordUsedZoom);\n" +
                    "\n" +
                    "        r = tca.r + (r*(1.0-offset2));\n" +
                    "        g = tca.g + (g*(1.0-offset2));\n" +
                    "        b = tca.b + (b*(1.0-offset2));\n" +
                    "\n" +
                    "    }\n" +
                    "\n" +
                    "    else if(finalType==8){\n" +
                    "\n" +
                    "        float sx = offset;\n" +
                    "        float sy = offset;\n" +
                    "        float sx2 = offset2;\n" +
                    "        float sy2 = offset2;\n" +
                    "\n" +
                    "        vec2 coordUsedR = vec2(0.0,0.0);\n" +
                    "        vec2 coordUsedA = vec2(0.0,0.0);\n" +
                    "\n" +
                    "        coordUsedA.x = coordUsed.x+((coordUsed.x-0.5)/sx * (1.0-sx));\n" +
                    "        coordUsedA.y = coordUsed.y+((coordUsed.y-0.5)/sy * (1.0-sy));\n" +
                    "        coordUsedR.x = coordUsed.x+((coordUsed.x-0.5)/sx2 * (1.0-sx2));\n" +
                    "        coordUsedR.y = coordUsed.y+((coordUsed.y-0.5)/sy2 * (1.0-sy2));\n" +
                    "\n" +
                    "        highp vec2 wavecord_r = coordUsedR+vec2(r_shift,0.00);\n" +
                    "        highp vec2 wavecord_g = coordUsedA+vec2(g_shift,0.00);\n" +
                    "        highp vec2 wavecord_b = coordUsedA+vec2(b_shift,0.00);\n" +
                    "        highp vec4 tc_r = texture2D(sTexture, wavecord_r);\n" +
                    "        highp vec4 tc_g = texture2D(sTexture, wavecord_g);\n" +
                    "        highp vec4 tc_b = texture2D(sTexture, wavecord_b);\n" +
                    "\n" +
                    "        r = tc_r.r;\n" +
                    "        g = tc_g.g;\n" +
                    "        b = tc_b.b;\n" +
                    "    }\n" +
                    "\n" +
                    "    //curve_1\n" +
                    "    else if(finalType==10){\n" +
                    "        for(int i=0;i<120;i+=6){\n" +
                    "            if(coordUsed.y>wave[i+2]+offset && coordUsed.y<wave[i+3]+offset){\n" +
                    "                highp float startY = wave[i+2]+offset;\n" +
                    "                highp float endY = wave[i+3]+offset;\n" +
                    "                highp float shiftRadius = wave[i+3];\n" +
                    "                highp float shiftFactor = wave[i+4];\n" +
                    "                highp float x = coordUsed.x;\n" +
                    "                highp float y = coordUsed.y;\n" +
                    "                highp float max = endY-startY;\n" +
                    "                highp float perc = ((y-startY)/max);\n" +
                    "                highp float ang = perc*3.14*2.0;\n" +
                    "                highp float sinVal = sin(ang);\n" +
                    "                highp float shiftX = sinVal*shiftFactor*2.0;\n" +
                    "\n" +
                    "                highp vec2 shiftXcoord1 = vec2(shiftX,0.00);\n" +
                    "                highp vec2 shiftXcoord2 = vec2(shiftX*0.75,0.00);\n" +
                    "                highp vec2 shiftXcoord3 = vec2(shiftX*0.5,0.00);\n" +
                    "\n" +
                    "                highp vec2 shiftPos1 = coordUsed - shiftXcoord1;\n" +
                    "                highp vec2 shiftPos2 = coordUsed - shiftXcoord2;\n" +
                    "                highp vec2 shiftPos3 = coordUsed - shiftXcoord3;\n" +
                    "\n" +
                    "                highp vec4 tc1 = texture2D(sTexture, shiftPos1);\n" +
                    "                highp vec4 tc2 = texture2D(sTexture, shiftPos2);\n" +
                    "                highp vec4 tc3 = texture2D(sTexture, shiftPos3);\n" +
                    "                r = tc2.r;\n" +
                    "                g = tc1.g;\n" +
                    "                b = tc1.b;\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "    //curve_2\n" +
                    "    else if(finalType==11){\n" +
                    "        for(int i=0;i<120;i+=6){\n" +
                    "            if(coordUsed.y>wave[i+2] && coordUsed.y<wave[i+3]){\n" +
                    "                highp float startY = wave[i+2]+offset;\n" +
                    "                highp float endY = wave[i+3]+offset;\n" +
                    "                highp float shiftRadius = wave[i+3];\n" +
                    "                highp float shiftFactor = wave[i+4];\n" +
                    "                highp float x = coordUsed.x;\n" +
                    "                highp float y = coordUsed.y;\n" +
                    "                highp float max = endY-startY;\n" +
                    "                highp float perc = ((y-startY)/max);\n" +
                    "                highp float ang = perc*3.14*2.0;\n" +
                    "                highp float sinVal = sin(ang);\n" +
                    "                highp float shiftX = sinVal*shiftFactor*2.0;\n" +
                    "\n" +
                    "                highp vec2 shiftXcoord1 = vec2(shiftX,0.00);\n" +
                    "                highp vec2 shiftXcoord2 = vec2(shiftX*0.75,0.00);\n" +
                    "                highp vec2 shiftXcoord3 = vec2(shiftX*0.5,0.00);\n" +
                    "\n" +
                    "                highp vec2 shiftPos1 = coordUsed - shiftXcoord1;\n" +
                    "                highp vec2 shiftPos2 = coordUsed - shiftXcoord2;\n" +
                    "                highp vec2 shiftPos3 = coordUsed - shiftXcoord3;\n" +
                    "\n" +
                    "                highp vec4 tc1 = texture2D(sTexture, shiftPos1);\n" +
                    "                highp vec4 tc2 = texture2D(sTexture, shiftPos2);\n" +
                    "                highp vec4 tc3 = texture2D(sTexture, shiftPos3);\n" +
                    "                r = tc2.r;\n" +
                    "                g = tc1.g;\n" +
                    "                b = tc1.b;\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "    //white_1\n" +
                    "    else if(finalType==12){\n" +
                    "        highp float x = coordUsed.x;\n" +
                    "        highp float y_1 = coordUsed.y-offset;\n" +
                    "        if(y_1<0.0){\n" +
                    "            y_1 = 1.0+y_1;\n" +
                    "        }\n" +
                    "\n" +
                    "        highp float randval = 0.1;\n" +
                    "\n" +
                    "        highp float factor2 = offset2+(randval/5.0)+y_1/5.0;\n" +
                    "        if(mod((1.0-y_1),factor2)<factor2/2.0 && (mod(y_1,0.005)<0.002)){\n" +
                    "            r+=0.7;\n" +
                    "            g+=0.7;\n" +
                    "            b+=0.7;\n" +
                    "        }\n" +
                    "\n" +
                    "    }\n" +
                    "\n" +
                    "    //white_2\n" +
                    "    else if(finalType==13){\n" +
                    "        highp float x = coordUsed.x;\n" +
                    "        highp float y_1 = coordUsed.y-offset;\n" +
                    "        if(y_1<0.0){\n" +
                    "            y_1 = 1.0+y_1;\n" +
                    "        }\n" +
                    "\n" +
                    "        highp float randval = rand(coordUsed.xy,random1);\n" +
                    "        highp float factor2 = offset2+(randval/5.0)+y_1/5.0;\n" +
                    "        if(mod((1.0-y_1),factor2)<factor2/2.0 && (mod(y_1,0.005)<0.002)){\n" +
                    "            r+=0.7;\n" +
                    "            g+=0.7;\n" +
                    "            b+=0.7;\n" +
                    "        }\n" +
                    "\n" +
                    "    }\n" +
                    "\n" +
                    "    //shake_1\n" +
                    "    else if(finalType==14){\n" +
                    "        highp float L = distance(coordUsed,vec2(0.5,0.5));\n" +
                    "        highp float beta = atan((coordUsed.y-0.5),(coordUsed.x-0.5));\n" +
                    "        highp float teta = beta + random1;\n" +
                    "        highp float y_d = 0.5+L*sin(teta);\n" +
                    "        highp float x_d = 0.5+L*cos(teta);\n" +
                    "        highp vec2 shiftPos3 = vec2(x_d,y_d);\n" +
                    "        highp vec4 tc3 = texture2D(sTexture, shiftPos3);\n" +
                    "        r = tc3.r;\n" +
                    "        g = tc3.g;\n" +
                    "        b = tc3.b;\n" +
                    "    }\n" +
                    "\n" +
                    "    //shake_2\n" +
                    "    else if(finalType==15){\n" +
                    "        highp float L = distance(coordUsed,vec2(0.5,0.5));\n" +
                    "        highp float beta = atan((coordUsed.y-0.5),(coordUsed.x-0.5));\n" +
                    "\n" +
                    "        highp float teta = beta + random1;\n" +
                    "        highp float y_d = 0.5+L*sin(teta);\n" +
                    "        highp float x_d = 0.5+L*cos(teta);\n" +
                    "        highp vec2 shiftPos3 = vec2(x_d,y_d);\n" +
                    "        highp vec4 tc3 = texture2D(sTexture, shiftPos3);\n" +
                    "\n" +
                    "        highp float teta2 = beta + random1/2.0;\n" +
                    "        highp float y_d2 = 0.5+L*sin(teta2);\n" +
                    "        highp float x_d2 = 0.5+L*cos(teta2);\n" +
                    "        highp vec2 shiftPos2 = vec2(x_d2,y_d2);\n" +
                    "        highp vec4 tc2 = texture2D(sTexture, shiftPos2);\n" +
                    "\n" +
                    "        r = tc3.r;\n" +
                    "        g = tc2.g;\n" +
                    "        b = tc2.b;\n" +
                    "    }\n" +
                    "\n" +
                    "    //old_cinema_1\n" +
                    "    else if(finalType==16){\n" +
                    "        highp float y_1 = coordUsed.y-offset;\n" +
                    "        if(y_1<0.0){\n" +
                    "            y_1 = 1.0+y_1;\n" +
                    "        }\n" +
                    "        highp vec4 tc2 = texture2D(sTexture, vec2(coordUsed.x,y_1));\n" +
                    "        r = tc2.r;\n" +
                    "        g = tc2.g;\n" +
                    "        b = tc2.b;\n" +
                    "\n" +
                    "        y_1 = coordUsed.y+offset;\n" +
                    "        if(y_1<0.0){\n" +
                    "            y_1 = 1.0+y_1;\n" +
                    "        }\n" +
                    "\n" +
                    "        highp float randval = rand(coordUsed.xy,random1);\n" +
                    "        highp float factor2 = offset2+(randval/5.0)+y_1/5.0;\n" +
                    "        if(mod((1.0-y_1),factor2)<factor2/2.0 && (mod(y_1,0.005)<0.002)){\n" +
                    "            r+=0.7;\n" +
                    "            g+=0.7;\n" +
                    "            b+=0.7;\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "    //old_cinema_2\n" +
                    "    else if(finalType==17){\n" +
                    "        highp float y_1 = coordUsed.y-offset;\n" +
                    "        if(y_1<0.0){\n" +
                    "            y_1 = 1.0+y_1;\n" +
                    "        }\n" +
                    "        highp vec4 tc1 = texture2D(sTexture, vec2(coordUsed.x,y_1));\n" +
                    "        highp vec4 tc2 = texture2D(sTexture, vec2(coordUsed.x+random1,y_1));\n" +
                    "        r = tc1.r;\n" +
                    "        g = tc2.g;\n" +
                    "        b = tc1.b;\n" +
                    "\n" +
                    "        y_1 = coordUsed.y+offset;\n" +
                    "        if(y_1<0.0){\n" +
                    "            y_1 = 1.0+y_1;\n" +
                    "        }\n" +
                    "\n" +
                    "        highp float randval = rand(coordUsed.xy,random1);\n" +
                    "        highp float factor2 = offset2+(randval/5.0)+y_1/5.0;\n" +
                    "        if(mod((1.0-y_1),factor2)<factor2/2.0 && (mod(y_1,0.005)<0.002)){\n" +
                    "            r+=0.7;\n" +
                    "            g+=0.7;\n" +
                    "            b+=0.7;\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "    //leak_1\n" +
                    "    else if(finalType==18){\n" +
                    "        highp float L = distance(coordUsed,vec2(0.0,offset));\n" +
                    "        r+=0.5*L;\n" +
                    "        g+=0.25*L;\n" +
                    "\n" +
                    "        L*=L;\n" +
                    "        r+=0.5*L;\n" +
                    "    }\n" +
                    "\n" +
                    "    //leak_2\n" +
                    "    else if(finalType==19){\n" +
                    "        highp float L = distance(coordUsed,vec2(0.0,offset));\n" +
                    "        L=1.0-L;\n" +
                    "        r+=0.5*L;\n" +
                    "        g+=0.25*L;\n" +
                    "\n" +
                    "        L*=L;\n" +
                    "        r+=0.5*L;\n" +
                    "    }\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "    if(filterType==1){\n" +
                    "        //curve merah, gelap kurang merah, terang lebih merah\n" +
                    "        r = 0.5 * sin ( r*3.14 - 1.57 ) + 0.5;\n" +
                    "    }\n" +
                    "\n" +
                    "    if(filterType==2){\n" +
                    "        r = 0.25 * tan ( r*3.14 -1.57 )/1.4 + 0.5;\n" +
                    "    }\n" +
                    "\n" +
                    "    if(filterType==3){\n" +
                    "        g = 0.5 * sin ( g*3.14 - 1.57 ) + 0.5;\n" +
                    "    }\n" +
                    "\n" +
                    "    if(filterType==4){\n" +
                    "        g = 0.25 * tan ( g*3.14 -1.57 )/1.4 + 0.5;\n" +
                    "    }\n" +
                    "\n" +
                    "    if(filterType==5){\n" +
                    "        b = 0.5 * sin ( b*3.14 - 1.57 ) + 0.5;\n" +
                    "    }\n" +
                    "\n" +
                    "    if(filterType==6){\n" +
                    "        b = 0.25 * tan ( b*3.14 -1.57 )/1.4 + 0.5;\n" +
                    "    }\n" +
                    "\n" +
                    "    if(filterType==7){\n" +
                    "        r = 0.5 * sin ( r*3.14 - 1.57 ) + 0.5;\n" +
                    "        b = 0.25 * tan ( b*3.14 -1.57 )/1.4 + 0.5;\n" +
                    "    }\n" +
                    "\n" +
                    "    if(filterType==8){\n" +
                    "        r = 0.5 * sin ( r*3.14 - 1.57 ) + 0.5;\n" +
                    "        g = 0.25 * tan ( g*3.14 -1.57 )/1.4 + 0.5;\n" +
                    "    }\n" +
                    "\n" +
                    "    if(filterType==9){\n" +
                    "        g = 0.5 * sin ( g*3.14 - 1.57 ) + 0.5;\n" +
                    "        r = 0.25 * tan ( r*3.14 -1.57 )/1.4 + 0.5;\n" +
                    "    }\n" +
                    "\n" +
                    "    if(filterType==10){\n" +
                    "        g = 0.5 * sin ( g*3.14 - 1.57 ) + 0.5;\n" +
                    "        b = 0.25 * tan ( b*3.14 -1.57 )/1.4 + 0.5;\n" +
                    "    }\n" +
                    "\n" +
                    "    if(filterType==11){\n" +
                    "        b = 0.5 * sin ( b*3.14 - 1.57 ) + 0.5;\n" +
                    "        r = 0.25 * tan ( r*3.14 -1.57 )/1.4 + 0.5;\n" +
                    "    }\n" +
                    "\n" +
                    "    if(filterType==12){\n" +
                    "        b = 0.5 * sin ( b*3.14 - 1.57 ) + 0.5;\n" +
                    "        g = 0.25 * tan ( g*3.14 -1.57 )/1.4 + 0.5;\n" +
                    "    }\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "//    r+=(scalex+scaley)*0.0;\n" +
                    "    gl_FragColor = vec4(r,g,b,1.0);\n" +
                    "}";

    /* renamed from: k */
    public int f21321k = 0;

    /* renamed from: l */
    public float f21322l = 0.0f;

    /* renamed from: m */
    public float f21323m = 0.0f;

    /* renamed from: n */
    public float f21324n = 0.0f;

    /* renamed from: o */
    public float f21325o = 1f;

    /* renamed from: p */
    public float f21326p = 1f;

    /* renamed from: q */
    public int f21327q = 1;

    /* renamed from: r */
    public float[] f21328r = new float[120];

    long j = 0;

    public GlDynamicFilter(int filterType) {
        super(DEFAULT_VERTEX_SHADER, TRANSITION_FRAGMENT_SHADER);
        f21321k = filterType;
    }

    @Override
    protected void onDraw() {
        j += 30;

        mo12133N(f21321k, j);

        int b = getHandle("wave");
        float[] fArr = this.f21328r;
        GLES20.glUniform1fv(b, fArr.length, FloatBuffer.wrap(fArr));
        GLES20.glUniform1i(getHandle("type"), this.f21327q);
        GLES20.glUniform1i(getHandle("filterType"), this.f21321k);
        GLES20.glUniform1f(getHandle("scalex"), this.f21325o);
        GLES20.glUniform1f(getHandle("scaley"), this.f21326p);
        GLES20.glUniform1f(getHandle("offset"), this.f21322l);
        GLES20.glUniform1f(getHandle("offset2"), this.f21323m);
        GLES20.glUniform1f(getHandle("random1"), this.f21324n);
    }

    public void mo12133N(int i, long j) {
        float f;
        float f2;
        float random;
        float f3 = 0; //linhbd
        int i2;
        int i3;
        int i4;
        float f4;
        int i5;
        float abs = 0; //linhbd
        int i6;
        float f5;

        int i7 = i;
        f21322l = 0.0f;
        f21323m = 0.0f;
        char c = 1;
        if (i7 == 1) {
            f21327q = 1;
            if (j % 20 < 10) {
                f21328r = m15227q();
                return;
            }
            return;
        }
        if (i7 == 2) {
            f21327q = 1;
            if (j % 50 < 10) {
                f21328r = m15227q();
            }
            f5 = ((float) (j % 1000)) / 1000.0f;
        } else {
            double d = 0.01d;
            if (i7 == 3) {
                f21327q = 3;
                float[] fArr = new float[120];
                int i8 = 0;
                int i9 = 0;
                while (i9 < 100) {
                    if (Math.random() > 0.1d) {
                        float f6 = ((float) i9) / 100.0f;
                        float L = (float) m15200L(0.01d, 0.04d);
                        float f7 = f6 + L;
                        i9 += (int) (L * 200.0f);
                        int i10 = i8 + 1;
                        fArr[i8] = 0.0f;
                        int i11 = i10 + 1;
                        fArr[i10] = 1.0f;
                        int i12 = i11 + 1;
                        fArr[i11] = f6;
                        int i13 = i12 + 1;
                        fArr[i12] = f7;
                        int i14 = i13 + 1;
                        fArr[i13] = (float) m15200L(0.01d, 0.03d);
                        i8 = i14 + 1;
                        fArr[i14] = (float) m15200L(0.0d, 3.0d);
                        if (i8 >= 120) {
                            break;
                        }
                    }
                    if (i8 >= 120) {
                        break;
                    }
                    i9++;
                }
                f21328r = fArr;
                f = (float) (j % 1000);
                f2 = 10000.0f;
            } else {
                double d2 = 0.3d;
                if (i7 == 4) {
                    f21327q = 4;
                    float[] fArr2 = new float[120];
                    int i15 = 0;
                    int i16 = 0;
                    while (i15 < 30) {
                        if (Math.random() > d2) {
                            float f8 = ((float) i15) / 100.0f;
                            float L2 = (float) m15200L(d, 0.02d);
                            float f9 = f8 + L2;
                            i15 += (int) (L2 * 110.0f);
                            int i17 = i16 + 1;
                            fArr2[i16] = 0.0f;
                            int i18 = i17 + 1;
                            fArr2[i17] = 1.0f;
                            int i19 = i18 + 1;
                            fArr2[i18] = f8;
                            int i20 = i19 + 1;
                            fArr2[i19] = f9;
                            int i21 = i20 + 1;
                            fArr2[i20] = (float) m15200L(0.2d, 0.4d);
                            i6 = i21 + 1;
                            fArr2[i21] = (float) m15200L(0.0d, 3.0d);
                            if (i6 >= 120) {
                                break;
                            }
                        } else {
                            i6 = i16;
                        }
                        if (i6 >= 120) {
                            break;
                        }
                        i15++;
                        i16 = i6;
                        d = 0.01d;
                        d2 = 0.3d;
                    }
                    f21328r = fArr2;
                    f21322l = ((float) (j % 3000)) / 3000.0f;
                    f3 = ((float) (j % 500)) / 500.0f;
                } else if (i7 == 5) {
                    f21327q = 5;
                    f = (float) (j % 3000);
                    f2 = 3000.0f;
                } else if (i7 == 6) {
                    f21327q = 5;
                    f21322l = ((float) (j % 3000)) / 3000.0f;
                    if (j % 60 < 30) {
                        f21327q = 100;
                        return;
                    }
                    return;
                } else if (i7 == 7) {
                    f21327q = 7;
                    float f10 = ((float) (j % 500)) / 500.0f;
                    f21322l = (f10 / 2.0f) + 1.0f;
                    f3 = (1.0f - f10) / 4.0f;
                } else {
                    if (i7 == 8) {
                        f21327q = 8;
                        abs = ((float) Math.abs(250 - (j % 500))) / 500.0f;
                        f21322l = (abs / 10.0f) + 1.0f;
                    } else if (i7 == 9) {
                        f21327q = 8;
                        long j2 = j % 1000;
                        int i22 = (j2 > 250 ? 1 : (j2 == 250 ? 0 : -1));
                        char c2 = i22 > 0 ? 1 : i22 == 0 ? (char) 0 : 65535;
                        char c3 = c2 > 0 ? 1 : c2 == 0 ? (char) 0 : 65535;
                        if (c3 <= 0) {
                            c = c3 != 0 ? (char) 65535 : 0;
                        }
                        abs = ((float) (c < 0 ? Math.abs(125 - j2) : (c <= 0 || j2 >= 500) ? 0 : -Math.abs(125 - j2))) / 125.0f;
                        f21322l = (abs / 10.0f) + 1.0f;
                    } else {
                        if (i7 == 10) {
                            f21327q = 10;
                            float[] fArr3 = new float[120];
                            int i23 = 0;
                            int i24 = 0;
                            while (i23 < 100) {
                                double d3 = 0.5d;
                                if (Math.random() > 0.5d) {
                                    float f11 = ((float) i23) / 100.0f;
                                    int i25 = i24;
                                    float L3 = (float) m15200L(0.1d, 0.3d);
                                    float f12 = f11 + L3;
                                    int i26 = ((int) (L3 * 90.0f)) + i23;
                                    int i27 = 0;
                                    while (true) {
                                        if (i27 >= 100) {
                                            i24 = i25;
                                            i4 = 1;
                                            break;
                                        }
                                        if (Math.random() > d3) {
                                            float f13 = (float) i27;
                                            float f14 = f13 / 100.0f;
                                            int i28 = i25 + 1;
                                            fArr3[i25] = f14;
                                            int i29 = i28 + 1;
                                            fArr3[i28] = f14 + 1.0f;
                                            int i30 = i29 + 1;
                                            fArr3[i29] = f11;
                                            int i31 = i30 + 1;
                                            fArr3[i30] = f12;
                                            int i32 = i31 + 1;
                                            f4 = f11;
                                            fArr3[i31] = (float) m15200L(0.01d, 0.05d);
                                            fArr3[i32] = (float) m15200L(0.0d, 1.0d);
                                            i27 = (int) (f13 + 90.0f);
                                            i4 = 1;
                                            i5 = i32 + 1;
                                        } else {
                                            f4 = f11;
                                            i4 = 1;
                                            i5 = i25;
                                        }
                                        if (i5 >= 120) {
                                            i24 = i5;
                                            break;
                                        }
                                        i27 += i4;
                                        f11 = f4;
                                        i25 = i5;
                                        d3 = 0.5d;
                                    }
                                    i23 = i26;
                                    int i33 = i4;
                                } else {
                                    int i34 = i24;
                                    if (i34 >= 120) {
                                        break;
                                    }
                                    i24 = i34;
                                }
                            }
                            f21328r = fArr3;
                            f3 = ((float) (j % 1000)) / 1000.0f;
                        } else if (i7 == 11) {
                            f21327q = 11;
                            float[] fArr4 = new float[120];
                            int i35 = 0;
                            int i36 = 0;
                            int i37 = 100;
                            while (i35 < i37) {
                                double d4 = 0.5d;
                                if (Math.random() > 0.5d) {
                                    float f15 = ((float) i35) / 100.0f;
                                    float L4 = (float) m15200L(0.3d, 0.6d);
                                    float f16 = f15 + L4;
                                    int i38 = ((int) (L4 * 90.0f)) + i35;
                                    int i39 = 0;
                                    i2 = 100;
                                    while (true) {
                                        if (i39 >= 100) {
                                            break;
                                        }
                                        if (Math.random() > d4) {
                                            float f17 = (float) i39;
                                            float f18 = f17 / 100.0f;
                                            int i40 = i36 + 1;
                                            fArr4[i36] = f18;
                                            int i41 = i40 + 1;
                                            fArr4[i40] = f18 + 1.0f;
                                            int i42 = i41 + 1;
                                            fArr4[i41] = f15;
                                            int i43 = i42 + 1;
                                            fArr4[i42] = f16;
                                            int i44 = i43 + 1;
                                            fArr4[i43] = (float) m15200L(0.01d, 0.05d);
                                            fArr4[i44] = (float) m15200L(0.0d, 1.0d);
                                            i39 = (int) (f17 + 90.0f);
                                            i3 = 1;
                                            i36 = i44 + 1;
                                        } else {
                                            i3 = 1;
                                        }
                                        if (i36 >= 120) {
                                            break;
                                        }
                                        i39 += i3;
                                        d4 = 0.5d;
                                    }
                                    i35 = i38;
                                } else {
                                    i2 = 100;
                                    if (i36 >= 120) {
                                        break;
                                    }
                                }
                                i37 = i2;
                            }
                            f21328r = fArr4;
                            f3 = ((float) (j % 1000)) / 1000.0f;
                        } else {
                            if (i7 == 12) {
                                f21327q = 12;
                                f21328r = m15228r();
                                f21322l = ((float) (j % 1000)) / 1000.0f;
                                f21323m = 0.1f;
                            } else if (i7 == 13) {
                                f21327q = 13;
                                f21328r = m15228r();
                                f21322l = ((float) (j % 1000)) / 1000.0f;
                                f21323m = 0.1f;
                            } else {
                                if (i7 == 14) {
                                    f21327q = 14;
                                    f21328r = m15228r();
                                    f21322l = ((float) (j % 1000)) / 1000.0f;
                                    f21323m = 0.1f;
                                } else if (i7 == 15) {
                                    f21327q = 15;
                                    f21328r = m15228r();
                                    f21322l = ((float) (j % 1000)) / 1000.0f;
                                    f21323m = 0.1f;
                                } else if (i7 == 16) {
                                    f21327q = 16;
                                    f21328r = m15228r();
                                    f21322l = ((float) (j % 1000)) / 1000.0f;
                                    f21323m = 0.1f;
                                } else if (i7 == 17) {
                                    f21327q = 17;
                                    f21328r = m15228r();
                                    f21322l = ((float) (j % 1000)) / 1000.0f;
                                    f21323m = 0.1f;
                                } else {
                                    if (i7 == 18) {
                                        f21327q = 18;
                                        long j3 = j % 4000;
                                        f = j3 < 2000 ? (float) j3 : (float) (4000 - j3);
                                    } else if (i7 == 19) {
                                        f21327q = 19;
                                        long j4 = j % 4000;
                                        f = j4 < 2000 ? (float) j4 : (float) (4000 - j4);
                                    } else {
                                        return;
                                    }
                                    f2 = 2000.0f;
                                }
                                random = (((float) Math.random()) * 0.2f) - 0.1f;
                                f21324n = random;
                                return;
                            }
                            random = (float) Math.random();
                            f21324n = random;
                            return;
                        }
                        f21322l = f3;
                    }
                    f3 = (Math.abs(0.5f - abs) / 10.0f) + 1.0f;
                }
                f21323m = f3;
                return;
            }
            f5 = f / f2;
        }
        f21322l = f5;
    }


    public static float[] m15227q() {
        float f;
        float[] fArr = new float[120];
        int i = 0;
        int i2 = 0;
        do {
            int i3 = 100;
            if (i >= 100) {
                break;
            }
            double d = 0.5d;
            if (Math.random() > 0.5d) {
                float f2 = 100.0f;
                float f3 = ((float) i) / 100.0f;
                float L = (float) m15200L(0.1d, 0.3d);
                float f4 = f3 + L;
                int i4 = ((int) (L * 90.0f)) + i;
                int i5 = 0;
                while (i5 < i3) {
                    if (Math.random() > d) {
                        float f5 = (float) i5;
                        float f6 = f5 / f2;
                        float f7 = f5;
                        float L2 = (float) m15200L(0.3d, d);
                        int i6 = i2 + 1;
                        fArr[i2] = f6;
                        int i7 = i6 + 1;
                        fArr[i6] = f6 + L2;
                        int i8 = i7 + 1;
                        fArr[i7] = f3;
                        int i9 = i8 + 1;
                        fArr[i8] = f4;
                        int i10 = i9 + 1;
                        f = f3;
                        fArr[i9] = (float) m15200L(0.05d, 0.2d);
                        fArr[i10] = (float) m15200L(0.0d, 3.0d);
                        i2 = i10 + 1;
                        i5 = (int) ((L2 * 90.0f) + f7);
                    } else {
                        f = f3;
                    }
                    if (i2 >= 120) {
                        break;
                    }
                    i5 += 10;
                    f3 = f;
                    i3 = 100;
                    f2 = 100.0f;
                    d = 0.5d;
                }
                i = i4;
                continue;
            }
        } while (i2 < 120);
        return fArr;
    }

    /* renamed from: L */
    public static double m15200L(double d, double d2) {
        return (Math.random() * (d2 - d)) + d;
    }

    public static float[] m15228r() {
        float f;
        float[] fArr = new float[120];
        int i = 0;
        int i2 = 0;
        do {
            int i3 = 100;
            if (i >= 100) {
                break;
            }
            double d = 0.5d;
            if (Math.random() > 0.5d) {
                float f2 = 100.0f;
                float f3 = ((float) i) / 100.0f;
                float L = (float) m15200L(0.1d, 0.3d);
                float f4 = f3 + L;
                int i4 = ((int) (L * 90.0f)) + i;
                int i5 = 0;
                while (i5 < i3) {
                    if (Math.random() > d) {
                        float f5 = (float) i5;
                        float f6 = f5 / f2;
                        float f7 = f5;
                        float L2 = (float) m15200L(0.3d, d);
                        int i6 = i2 + 1;
                        fArr[i2] = f6;
                        int i7 = i6 + 1;
                        fArr[i6] = f6 + L2;
                        int i8 = i7 + 1;
                        fArr[i7] = f3;
                        int i9 = i8 + 1;
                        fArr[i8] = f4;
                        int i10 = i9 + 1;
                        f = f3;
                        fArr[i9] = (float) m15200L(0.05d, 0.2d);
                        fArr[i10] = (float) m15200L(0.0d, 3.0d);
                        i2 = i10 + 1;
                        i5 = (int) ((L2 * 90.0f) + f7);
                    } else {
                        f = f3;
                    }
                    if (i2 >= 120) {
                        break;
                    }
                    i5++;
                    f3 = f;
                    i3 = 100;
                    f2 = 100.0f;
                    d = 0.5d;
                }
                i = i4;
                continue;
            }
        } while (i2 < 120);
        return fArr;
    }

}
