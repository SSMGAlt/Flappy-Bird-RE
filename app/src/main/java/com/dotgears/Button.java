package com.dotgears;

public class Button extends GameObject {

    public AtlasRegion a;
    public int b;
    public int c;
    public int d;
    public int e;
    public boolean f;
    public boolean g;
    public boolean h;
    public boolean i;

    public Button() {
        super();
    }

    public void setSprite(String spriteName) {
        a = Renderer.D.getSprite(spriteName);
        d = a.b;
        e = a.c;
    }

    public void setPosition(int x, int y) {
        b = x;
        c = y;
        F = true;
        G = true;
        i = false;
        g = false;
        h = false;
        f = false;
    }

    public void update(float deltaTime) {
        int touchCount = Renderer.D.u;
        int[] touchX = Renderer.D.s;
        int[] touchY = Renderer.D.t;

        i = false;
        for (int idx = 0; idx < touchCount; idx++) {
            if (touchX[idx] > b && touchX[idx] < b + d
                    && touchY[idx] > c && touchY[idx] < c + e) {
                i = true;
                break;
            }
        }

        g = false;
        h = false;
        if (i != f) {
            if (f) {
                h = true;
                f = false;
            }
        }
        if (!f && i) {
            g = true;
            f = true;
        }
    }

    public void draw(Renderer renderer) {
        if (!f) {
            int spriteIndex = a.i;
            int drawX = b;
            int drawY = c + (f ? 2 : 0);
            renderer.drawSprite(spriteIndex, drawX, drawY, 1.0f, 1.0f, 1.0f);
        } else {
            int spriteIndex = a.i;
            int drawX = b;
            int drawY = c + 2;
            renderer.drawSprite(spriteIndex, drawX, drawY, 1.0f, 1.0f, 1.0f);
        }
    }
}
