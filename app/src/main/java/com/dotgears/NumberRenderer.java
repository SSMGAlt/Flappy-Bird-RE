package com.dotgears;

public class NumberRenderer {

    public static String l = "0123456789";

    public AtlasRegion[] a;
    protected int[] b;
    protected int c;
    protected int d;
    protected int e;
    protected int f;
    protected int g;
    protected float h;
    public char[] i;
    public char[] j;
    public int k;

    public NumberRenderer(String prefix, int alignment) {
        a = new AtlasRegion[256];
        b = new int[256];
        i = new char[256];
        j = new char[256];
        k = 0;

        AtlasRegion[] regions = Renderer.D.getSpritesByPrefix(prefix);
        for (int idx = 0; idx < regions.length; idx++) {
            String name = regions[idx].a;
            String[] parts = name.split("_");
            int digit = Integer.parseInt(parts[parts.length - 1]);
            a[digit] = regions[idx];
            b[digit] = regions[idx].b;
            if (regions[idx].c > c) {
                c = regions[idx].c;
            }
        }
        b[0x20] = b[0x30];
        d = alignment;
    }

    public void setNumber(int value, int maxDigits) {
        k = 0;
        int v = value;
        int digits = maxDigits;
        while (digits > 0) {
            if (v > 0) {
                int rem = v % 10;
                v = v / 10;
                j[k] = l.charAt(rem);
                k++;
            } else {
                break;
            }
            digits--;
        }
        if (k == 0) {
            i[0] = '0';
            k = 1;
        } else {
            for (int idx = 0; idx < k; idx++) {
                i[idx] = j[k - 1 - idx];
            }
        }
    }

    public void setLayout(int x, int y, int spacing, float scale) {
        f = x;
        g = y;
        e = spacing;
        h = scale;
    }

    public void draw(Renderer renderer) {
        int totalWidth = 0;
        for (int idx = 0; idx < k; idx++) {
            totalWidth += b[i[idx]];
        }

        int drawX = f;
        int drawY = g;
        if ((e & 2) != 0) {
            drawX = f - totalWidth / 2;
        } else if ((e & 1) != 0) {
            drawX = f - totalWidth;
        }

        for (int idx = 0; idx < k; idx++) {
            AtlasRegion region = a[i[idx]];
            if (region != null) {
                renderer.drawSprite(region, drawX, drawY, h);
            }
            drawX += b[i[idx]] - d;
        }
    }
}
