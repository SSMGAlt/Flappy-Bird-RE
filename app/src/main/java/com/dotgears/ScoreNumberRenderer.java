package com.dotgears;

public class ScoreNumberRenderer {

    public int a;
    public int b;
    private AtlasRegion[] c;

    public ScoreNumberRenderer(String prefix) {
        c = Renderer.D.getSpritesByPrefix(prefix);
        a = c[0].b;
        b = c[0].c;
    }

    public void draw(Renderer renderer, int value, int x, int y, boolean rightAlign, int maxDigits) {
        int startX = x - a;
        int v = value;
        int digits = maxDigits;
        while (digits > 0) {
            if (v > 0) {
                int digit = v % 10;
                v = v / 10;
                AtlasRegion region = c[digit];
                renderer.drawSprite(region, startX, y, 1.0f, 1.0f, 1.0f);
                startX -= a - b;
            } else {
                if (rightAlign) {
                    break;
                }
                AtlasRegion region = c[0];
                renderer.drawSprite(region, startX, y, 1.0f, 1.0f, 1.0f);
                startX -= a - b;
            }
            digits--;
        }
        if (value == 0) {
            renderer.drawSprite(c[0], x, y, 1.0f, 1.0f, 1.0f);
        }
    }
}
