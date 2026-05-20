package com.dotgears;

public class AnimatedEntity extends GameObject {

    AnimationState[] a;
    public int b;
    public int c;
    public int d;
    public int e;
    public int f;
    public int g;
    public int h;
    public float i;
    public AnimationState j;
    public boolean k;
    public boolean l;
    public boolean m;
    public boolean n;
    public int o;
    public AtlasRegion[] p;

    public AnimatedEntity() {
        super();
        a = new AnimationState[10];
        i = 1.0f;
        j = null;
        m = false;
        n = false;
    }

    public void update(float deltaTime) {
        if (!m) {
            return;
        }
        if (j == null) {
            return;
        }
        j.tick(deltaTime);
    }

    public void setPosition(int x, int y) {
        b = x;
        c = y;
        d = 0;
        m = true;
        n = true;
        k = false;
        l = false;
        i = 1.0f;
    }

    public void addAnimation(int index, String name, int[] frameIndices, int frameCount, int fps, boolean looping) {
        AnimationState state = new AnimationState(index, name, frameIndices, frameCount, fps, looping);
        a[index] = state;
    }

    public void playAnimation(int index, boolean resetFirst) {
        if (resetFirst) {
            a[index].reset();
        }
        a[index].resetState();
        j = a[index];
    }

    public void setSprites(String prefix, int width, int height, int offsetX, int offsetY) {
        p = Renderer.D.getSpritesByPrefix(prefix);
        o = p.length;
        if (width != 0 && height != 0) {
            e = width;
            f = height;
        } else {
            e = p[0].b;
            f = p[0].c;
        }
        if (offsetX != 0 && offsetY != 0) {
            g = (p[0].b - e) >> 1;
            h = (p[0].c - f) >> 1;
        } else {
            g = (p[0].b - e) >> 1;
            h = (p[0].c - f) >> 1;
        }
    }

    public void draw(Renderer renderer) {
        if (!n) {
            return;
        }
        AtlasRegion region = p[0];
        if (j != null && !j.a) {
            region = p[j.j];
        }
        int drawX = b - g;
        int drawY = c - h;
        float rotation = (j != null) ? 0.0f : 0.0f;
        int rotDeg = (int) rotation;
        renderer.drawSprite(region, drawX, drawY, i, rotDeg);
    }
}
