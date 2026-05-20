package com.dotgears;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Renderer extends GameObject {

    public static Renderer D;

    public int A;
    public ScoreNumberRenderer B;
    public ScorePanel C;

    public AtlasRegion[] a;
    public int[] b;
    public int[] c;
    public GameObject[] d;
    public int e;
    public Tweener f;
    public int g;
    public Tweener h;
    public int i;
    public int j;
    public int k;
    public int l;
    public GameObjectPool m;
    public GameObjectPool n;
    protected AtlasRegion o;
    protected AtlasRegion p;
    public int q;
    public boolean r;
    protected int[] s;
    protected int[] t;
    protected int u;
    int v;
    int[] w;
    double[] x;
    public int y;
    public int z;

    public Renderer(int currentScore, int bestScore, InputStream atlasStream) {
        super();
        b = new int[50];
        c = new int[50];
        d = new GameObject[50];
        q = 1;
        s = new int[10];
        t = new int[10];
        w = new int[50];
        x = new double[50];
        z = currentScore;
        A = bestScore;
        a = new AtlasRegion[512];
        loadAtlas(atlasStream);
    }

    private void loadAtlas(InputStream stream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() <= 1) continue;
                String[] parts = line.trim().split("\\s+");
                if (parts.length < 7) continue;
                String name = parts[0];
                int w = Integer.parseInt(parts[1]);
                int h = Integer.parseInt(parts[2]);
                float u1 = Float.parseFloat(parts[3]);
                float v1 = Float.parseFloat(parts[4]);
                float u2 = Float.parseFloat(parts[5]);
                float v2 = Float.parseFloat(parts[6]);
                AtlasRegion region = new AtlasRegion(name, w, h, u1, v1, u1 + u2, v1 + v2);
                if (region.i < a.length) {
                    a[region.i] = region;
                }
            }
            reader.close();
        } catch (Exception ex) {
            android.util.Log.e("FlappyBird", "Atlas load failed: " + ex.getMessage());
        }
    }

    public void init() {
        MathTables.x = new MathTables();
        f = new Tweener();
        h = new Tweener();
        o = getSprite("black");
        p = getSprite("white");
        u = 0;
        m = new GameObjectPool();
        for (int idx = 0; idx < 20; idx++) {
            m.add(new FloatingScore());
        }
        n = new GameObjectPool();
        for (int idx = 0; idx < 10; idx++) {
            n.add(new BlinkAnimation());
        }
        B = new ScoreNumberRenderer("number_score");
        C = new ScorePanel();
        l = 0;
        r = false;
        v = 0;
        resetEventQueue();
        fade(false, 0, 0.5f);
    }

    public void setScore(int score) {
        pushEvent(4, score);
        if (score > z) {
            z = score;
        }
    }

    public void fade(boolean toBlack, int targetAlpha, float duration) {
        Tweener tw = f;
        if (tw.g) {
            if (toBlack) {
                tw.start(1.0f, 0.0f, 5, duration);
            } else {
                tw.start(0.0f, 1.0f, 5, duration);
            }
            tw.tick(0.0f);
            g = targetAlpha;
        }
    }

    public void showBlink(int x, int y) {
        BlinkAnimation blink = (BlinkAnimation) n.next();
        blink.setPosition(x, y);
    }

    public void tick(float deltaTime) {
        resetEventQueue();
        if (!r) {
            for (int idx = 0; idx < 50; idx++) {
                if (b[idx] <= 0) break;
                b[idx] -= 30;
            }
            C.update(deltaTime);
            m.update(deltaTime);
            if (!f.g || f.a != 0.0f) {
                f.tick(deltaTime);
            }
            if (!h.g || h.a != 0.0f) {
                h.tick(deltaTime);
            }
            if (l > 0) {
                l--;
                i = MathTables.randomRange(-k, k);
                j = MathTables.randomRange(-k, k);
            }
        } else {
            i = 0;
            j = 0;
        }
    }

    public void resetEventQueue() {
        v = 0;
    }

    public void pushEvent(int type, int data) {
        if (v < w.length) {
            w[v] = type;
            x[v] = data;
            v++;
        }
    }

    public void resetDelayedEvents() {
        e = 0;
        for (int idx = 0; idx < 50; idx++) {
            b[idx] = 0;
        }
    }

    public void setTouchPoint(int touchX, int touchY) {
        s[0] = touchX;
        t[0] = touchY;
        u = 1;
    }

    public void clearTouch() {
        u = 0;
    }

    public AtlasRegion getSprite(String name) {
        for (int idx = 0; idx < a.length; idx++) {
            if (a[idx] != null && a[idx].a.equals(name)) {
                return a[idx];
            }
        }
        return null;
    }

    public AtlasRegion[] getSpritesByPrefix(String prefix) {
        int count = 0;
        for (int idx = 0; idx < a.length; idx++) {
            if (a[idx] != null && a[idx].a.startsWith(prefix + "_")) count++;
        }
        if (count == 0) return new AtlasRegion[0];
        AtlasRegion[] result = new AtlasRegion[count];
        int ri = 0;
        for (int idx = 0; idx < a.length; idx++) {
            if (a[idx] != null && a[idx].a.startsWith(prefix + "_")) {
                result[ri++] = a[idx];
            }
        }
        return result;
    }

    public void drawSprite(int spriteIndex, int x, int y, float r, float g, float b) {
        com.dotgears.FlappyScene.enqueueSprite(spriteIndex, x + i, y + j);
    }

    public void drawSprite(AtlasRegion region, int x, int y, float scale) {
        if (region != null) {
            com.dotgears.FlappyScene.enqueueSprite(region.i, x + i, y + j);
        }
    }

    public void drawSprite(AtlasRegion region, int x, int y, int rotationDeg, float sx, float sy) {
        if (region != null) {
            com.dotgears.FlappyScene.enqueueSpriteRotated(region, x + i, y + j, rotationDeg, sx, sy);
        }
    }

    public void drawSprite(int spriteIndex, int x, int y, int rotationDeg, float scale) {
        if (spriteIndex >= 0 && spriteIndex < a.length && a[spriteIndex] != null) {
            com.dotgears.FlappyScene.enqueueSpriteRotated(a[spriteIndex], x + i, y + j, rotationDeg, scale, scale);
        }
    }

    public void drawSpriteScaledAt(int spriteIndex, int x, int y, int w2, int h2,
                                   float u1, float v1, float u2, float v2, float alpha) {
        if (spriteIndex >= 0 && spriteIndex < a.length && a[spriteIndex] != null) {
            com.dotgears.FlappyScene.enqueueSpriteFull(spriteIndex, x + i, y + j, w2, h2, u1, v1, u2, v2, alpha);
        }
    }
}
