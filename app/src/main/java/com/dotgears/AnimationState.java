package com.dotgears;

public class AnimationState {

    public boolean a;
    public String b;
    public int c;
    public int[] d;
    public float e;
    public boolean f;
    public int g;
    public int h;
    public int i;
    public int j;

    public AnimationState(int index, String name, int[] frameIndices, int frameCount, int fps, boolean looping) {
        b = name;
        c = frameCount;
        d = new int[c];
        System.arraycopy(frameIndices, 0, d, 0, c);
        e = (float) (1000 / fps);
        f = looping;
        g = index;
    }

    public void reset() {
        h = 0;
        i = 0;
        j = d[0];
    }

    public void resetState() {
        if (!f && a) {
            reset();
        }
        a = false;
    }

    public void tick(float deltaTime) {
        if (a) {
            return;
        }
        h += 15;
        if ((float) h >= e) {
            h = 0;
            i++;
            if (i >= c) {
                if (f) {
                    i = 0;
                } else {
                    a = true;
                    i = 0;
                }
            }
            j = d[i];
        }
    }
}
