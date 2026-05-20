package com.dotgears;

public class AtlasRegion {

    public static int h = 0;

    public String a;
    public int b;
    public int c;
    public float d;
    public float e;
    public float f;
    public float g;
    public int i;

    public AtlasRegion(String name, int width, int height, float u1, float v1, float u2, float v2) {
        a = name;
        b = width;
        c = height;
        d = u1;
        e = v1;
        f = u2;
        g = v2;
        i = h;
        h++;
    }
}
