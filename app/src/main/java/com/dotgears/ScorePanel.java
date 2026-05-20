package com.dotgears;

public class ScorePanel extends GameObject {

    public int a;
    public int b;
    public int c;
    public int d;
    public int e;
    public int f;
    public int g;
    public int h;
    public int i;
    public int j;
    public int k;
    public int l;
    public boolean m;
    public int n;
    public Medal o;
    public Tweener p;
    public AtlasRegion q;
    public AtlasRegion r;
    public ScoreNumberRenderer s;

    public ScorePanel() {
        super();
        e = 0;
        f = 0;
        l = 0;
        r = Renderer.D.getSprite("score_panel");
        q = Renderer.D.getSprite("new");
        s = Renderer.D.B;
        c = r.b;
        d = r.c;
        n = (512 - d) / 2;
        p = new Tweener();
        o = new Medal();
    }

    public void show(int currentScore, int bestScore, int bronzeThreshold, int silverThreshold,
                     int goldThreshold, int platinumThreshold) {
        l = currentScore;
        f = bestScore;
        e = 0;
        g = bronzeThreshold;
        h = silverThreshold;
        i = goldThreshold;
        j = platinumThreshold;
        F = true;
        G = true;
        m = false;
        a = (288 - c) / 2;
        b = 504;
        p.start((float) b, (float) n, 11, 0.5f);
        k = 0;
        o.F = false;
        o.G = false;
    }

    public void update(float deltaTime) {
        if (!F) {
            return;
        }
        if (!p.g) {
            p.tick(deltaTime);
        }
        switch (k) {
            case 0:
                b = (int) p.a;
                if (p.g) {
                    if (l > 0) {
                        k = 1;
                        p.start(0.0f, (float) l, 0, 0.5f);
                    } else {
                        k = 2;
                    }
                    Renderer.D.setScore(l);
                    if (l > f) {
                        f = l;
                        m = true;
                    }
                    setupMedal();
                    o.a = a + 32;
                    o.b = b + 44;
                }
                break;
            case 1:
                e = (int) p.a;
                if (p.g) {
                    k = 2;
                }
                break;
            case 2:
                o.update(deltaTime);
                break;
        }
    }

    private void setupMedal() {
        if (e >= j) {
            o.setup(0);
        } else if (e >= i) {
            o.setup(1);
        } else if (e >= h) {
            o.setup(2);
        } else if (e >= g) {
            o.setup(3);
        }
    }

    public void draw(Renderer renderer) {
        if (!G) {
            return;
        }
        renderer.drawSprite(r.i, a, b, 1.0f, 1.0f, 1.0f);
        s.draw(renderer, e, a + 210, b + 36, false, 10);
        s.draw(renderer, f, a + 210, b + 78, false, 10);
        if (m) {
            renderer.drawSprite(q.i, a + 142, b + 60, 1.0f, 1.0f, 1.0f);
        }
        o.draw(renderer);
    }
}
