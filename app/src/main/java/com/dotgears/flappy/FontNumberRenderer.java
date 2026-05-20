package com.dotgears.flappy;

import com.dotgears.NumberRenderer;
import com.dotgears.Renderer;

public class FontNumberRenderer extends NumberRenderer {

    public FontNumberRenderer() {
        super("font", 2);
    }

    public void drawCentered(Renderer renderer, int value, int centerX, int y) {
        setNumber(value, 10);
        int totalWidth = 0;
        for (int idx = 0; idx < k; idx++) {
            totalWidth += b[i[idx]];
        }
        totalWidth -= 2 * (k - 1);
        setLayout(centerX - totalWidth / 2, y, 2, 1.0f);
        draw(renderer);
    }
}
