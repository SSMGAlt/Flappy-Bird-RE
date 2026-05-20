package com.dotgears;

public class Medal extends GameObject {

    public int a;
    public int b;
    public int c;
    public int d;
    public int e;
    public int f;
    private AtlasRegion[] g;

    public Medal() {
        super();
        g = Renderer.D.getSpritesByPrefix("medals");
    }

    public void setup(int medalIndex) {
        a = 0;
        b = 0;
        c = 44;
        d = 44;
        e = medalIndex;
        f = 30;
        F = true;
        G = true;
    }

    public void update(float deltaTime) {
        if (!F) {
            return;
        }
        if (f <= 0) {
            return;
        }
        f--;
        if (f > 0) {
            return;
        }
        f = 30;
        int shimX = a - 3;
        int rangeX = c + 6;
        shimX += MathTables.randomRange(0, rangeX);
        int shimY = b - 3;
        int rangeY = d + 6;
        shimY += MathTables.randomRange(0, rangeY);
        Renderer.D.showBlink(shimX, shimY);
    }

    public void draw(Renderer renderer) {
        if (!G) {
            return;
        }
        AtlasRegion region = g[e];
        renderer.drawSprite(region.i, a, b, 1.0f, 1.0f, 1.0f);
    }
}
