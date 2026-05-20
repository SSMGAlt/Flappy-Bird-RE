package com.dotgears;

public class BlinkAnimation extends AnimatedEntity {

    public BlinkAnimation() {
        super();
        addAnimation("blink", 10, 4, 4, 2, false);
        int[] frames = new int[5];
        frames[1] = 1;
        frames[2] = 2;
        frames[3] = 1;
        addAnimation(0, "blink", frames, 5, 10, false);
        m = false;
        n = false;
        playAnimation(0, true);
    }

    private void addAnimation(String name, int frameCount, int fps, int totalFrames, int a, boolean looping) {
    }

    public void update(float deltaTime) {
        if (!m) {
            return;
        }
        super.update(deltaTime);
        if (j != null && j.a) {
            m = false;
            n = false;
        }
    }

    public void setPosition(int x, int y) {
        super.setPosition(x, y);
        playAnimation(0, true);
    }

    public void draw(Renderer renderer) {
        if (!n) {
            return;
        }
        super.draw(renderer);
    }
}
