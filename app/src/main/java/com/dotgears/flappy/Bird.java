package com.dotgears.flappy;

import com.dotgears.AnimatedEntity;
import com.dotgears.AtlasRegion;
import com.dotgears.BlinkAnimation;
import com.dotgears.MathTables;
import com.dotgears.Renderer;
import com.dotgears.Tweener;

public class Bird extends AnimatedEntity {

    public static final float GRAVITY        = 0.25f;
    public static final float FLAP_VELOCITY  = -4.6f;
    public static final float MAX_FALL_SPEED = 10.0f;
    public static final float ROTATION_UP    = -25.0f;
    public static final float ROTATION_DOWN  = 90.0f;
    public static final int   DEAD_SPIN_TICKS = 15;

    public float A;
    public float B;
    public float C;
    public float D;
    public int   E;
    public boolean aa;
    public boolean ab;
    public boolean ac;
    public boolean ad;
    public int ae;
    public Tweener af;
    public Tweener ag;
    public BlinkAnimation ah;
    private int ai;
    private int aj;

    public Bird(int birdType) {
        super();
        af = new Tweener();
        ag = new Tweener();
        ah = new BlinkAnimation();

        String prefix = "bird" + birdType;
        setSprites(prefix, 0, 0, 0, 0);

        int[] wingFrames = new int[]{0, 1, 2, 1};
        addAnimation(0, "flap", wingFrames, 4, 8, true);

        int[] idleFrames = new int[]{1};
        addAnimation(1, "idle", idleFrames, 1, 1, true);

        int[] deadFrames = new int[]{2};
        addAnimation(2, "dead", deadFrames, 1, 1, false);
    }

    public void spawn(int x, int y) {
        setPosition(x, y);
        A = 0.0f;
        B = 0.0f;
        C = 0.0f;
        D = 0.0f;
        aa = false;
        ab = false;
        ac = false;
        ad = false;
        ae = 0;
        ai = x;
        aj = y;
        playAnimation(1, true);
        F = true;
        G = true;
        ah.setPosition(x + 3, y - 10);
    }

    public void flap() {
        if (ab || ac) return;
        aa = true;
        A = FLAP_VELOCITY;
        C = ROTATION_UP;
        af.start(C, ROTATION_UP, 0, 0.1f);
        Renderer.D.pushEvent(9, 0);
    }

    @Override
    public void update(float deltaTime) {
        if (!F) return;

        if (!aa && !ab && !ac) {
            float bobOffset = MathTables.sinLut((int) MathTables.normalizeAngle(B));
            c = aj + (int) (bobOffset * 3.0f);
            B += 5.0f;
            if (B >= 360.0f) B -= 360.0f;
            ah.setPosition(b + 3, c - 10);
            ah.update(deltaTime);
            super.update(deltaTime);
            return;
        }

        if (ab) {
            ae++;
            if (ae < DEAD_SPIN_TICKS) {
                A += GRAVITY;
                if (A > MAX_FALL_SPEED) A = MAX_FALL_SPEED;
                c += (int) A;
                D += 14.0f;
                if (D >= 360.0f) D -= 360.0f;
            } else {
                if (!ac) {
                    ac = true;
                    ag.start(D, ROTATION_DOWN, 4, 0.3f);
                }
                if (!ag.g) {
                    ag.tick(deltaTime);
                    D = ag.a;
                }
                A += GRAVITY;
                if (A > MAX_FALL_SPEED) A = MAX_FALL_SPEED;
                c += (int) A;
            }
            return;
        }

        A += GRAVITY;
        if (A > MAX_FALL_SPEED) A = MAX_FALL_SPEED;
        c += (int) A;

        if (A < 0.0f) {
            if (!af.g) {
                af.tick(deltaTime);
                D = af.a;
            }
        } else {
            D = ROTATION_UP + (ROTATION_DOWN - ROTATION_UP) * (A / MAX_FALL_SPEED);
        }

        super.update(deltaTime);
        ah.setPosition(b + 3, c - 10);
        ah.update(deltaTime);
    }

    @Override
    public void draw(Renderer renderer) {
        if (!G) return;
        AtlasRegion region = (p != null && p.length > 0) ? p[0] : null;
        if (j != null && !j.a && p != null && j.j < p.length) {
            region = p[j.j];
        }
        if (region == null) return;
        int rotDeg = ((int) D % 360 + 360) % 360;
        renderer.drawSprite(region, b, c, rotDeg, 1.0f, 1.0f);
        if (!ab) {
            ah.draw(renderer);
        }
    }

    public void die() {
        ab = true;
        ae = 0;
        playAnimation(2, true);
        Renderer.D.pushEvent(10, 0);
    }

    public boolean isDead()    { return ab; }
    public boolean isLanding() { return ac; }
    public int getX()          { return b; }
    public int getY()          { return c; }
}
