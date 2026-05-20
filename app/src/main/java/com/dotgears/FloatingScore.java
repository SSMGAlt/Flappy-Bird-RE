package com.dotgears;

public class FloatingScore extends GameObject {

    public int a;
    public int b;
    public boolean c;
    public int d;
    public int e;
    public int f;
    public int g;
    public int h;
    public int i;
    public int j;
    public int k;
    public int l;
    private AtlasRegion[] m;

    public FloatingScore() {
        super();
        a = 12;
        b = 14;
        m = Renderer.D.getSpritesByPrefix("number_context");
    }

    public void update(float deltaTime) {
        if (!F) {
            return;
        }
        if (d <= 0) {
            return;
        }
        d--;
        if (g < 2) {
            k += g;
            i++;
            if (i == 4) {
                i = 0;
                g += h;
            }
        }
        if (d <= 0) {
            F = false;
            G = false;
        }
    }

    public void draw(Renderer renderer) {
        if (!G) {
            return;
        }
        if (c) {
            AtlasRegion plus = m[10];
            renderer.drawSprite(plus.i, j, k, 1.0f, 1.0f, 1.0f);
        }
        drawDigits(renderer, e, j + l, k, false, f);
    }

    private void drawDigits(Renderer renderer, int value, int x, int y, boolean rightAlign, int maxDigits) {
        int startX = x - a;
        int v = value;
        int digits = maxDigits;
        while (digits > 0) {
            if (v > 0) {
                int digit = v % 10;
                v = v / 10;
                AtlasRegion region = m[digit];
                renderer.drawSprite(region.i, startX, y, 1.0f, 1.0f, 1.0f);
                int advance = a;
                if (digit == 1) {
                    advance = a - 4;
                    startX -= (a - 2);
                } else {
                    startX -= (a - 2);
                }
            } else {
                break;
            }
            digits--;
        }
    }
}
