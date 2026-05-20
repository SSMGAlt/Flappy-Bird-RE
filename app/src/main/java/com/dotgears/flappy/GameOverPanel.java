package com.dotgears.flappy;

import com.dotgears.AtlasRegion;
import com.dotgears.GameObject;
import com.dotgears.Renderer;
import com.dotgears.Tweener;

public class GameOverPanel extends GameObject {

    public int a;
    public int b;
    public int c;
    public int d;
    public int e;
    public Tweener f;
    private AtlasRegion g;

    public GameOverPanel() {
        super();
        f = new Tweener();
        g = Renderer.D.getSprite("text_game_over");
        c = g.b;
        d = g.c;
    }

    public void show() {
        a = (288 - c) / 2;
        b = -d;
        e = (512 - d) / 2 - 60;
        F = true;
        G = true;
        f.start((float) b, (float) e, 12, 0.5f);
    }

    @Override
    public void update(float deltaTime) {
        if (!F) {
            return;
        }
        if (!f.g) {
            f.tick(deltaTime);
            b = (int) f.a;
        }
    }

    @Override
    public void draw(Renderer renderer) {
        if (!G) {
            return;
        }
        renderer.drawSprite(g.i, a, b, 1.0f, 1.0f, 1.0f);
    }
}
