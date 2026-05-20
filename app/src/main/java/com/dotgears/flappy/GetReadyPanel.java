package com.dotgears.flappy;

import com.dotgears.AtlasRegion;
import com.dotgears.GameObject;
import com.dotgears.MathTables;
import com.dotgears.Renderer;
import com.dotgears.Tweener;

public class GetReadyPanel extends GameObject {

    public int a;
    public int b;
    public int c;
    public int d;
    public int e;
    public int f;
    public int g;
    public int h;
    public int i;
    public Tweener j;
    public Tweener k;
    private AtlasRegion l;
    private AtlasRegion m;

    public GetReadyPanel() {
        super();
        j = new Tweener();
        k = new Tweener();
        l = Renderer.D.getSprite("text_ready");
        m = Renderer.D.getSprite("tutorial");
        c = l.b;
        d = l.c;
        e = m.b;
        f = m.c;
    }

    public void show() {
        a = (288 - c) / 2;
        b = -d;
        g = (288 - e) / 2;
        h = (512 - f) / 2 + 20;
        F = true;
        G = true;
        i = 0;
        j.start((float) b, 80.0f, 12, 0.5f);
        k.start(0.0f, 1.0f, 0, 0.35f);
    }

    public void hide() {
        j.start((float) b, -d, 5, 0.3f);
        i = 1;
    }

    @Override
    public void update(float deltaTime) {
        if (!F) {
            return;
        }
        if (!j.g) {
            j.tick(deltaTime);
            b = (int) j.a;
        }
        if (!k.g) {
            k.tick(deltaTime);
        }
        if (i == 1 && j.g) {
            F = false;
            G = false;
        }
    }

    @Override
    public void draw(Renderer renderer) {
        if (!G) {
            return;
        }
        renderer.drawSprite(l.i, a, b, 1.0f, 1.0f, 1.0f);
        if (i == 0) {
            float alpha = k.a;
            renderer.drawSprite(m.i, g, h, alpha, alpha, alpha);
        }
    }
}
