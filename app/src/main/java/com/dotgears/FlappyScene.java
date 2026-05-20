package com.dotgears;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.google.android.gms.games.PlayGames;

import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

public class FlappyScene extends Scene implements Scene.IOnSceneTouchListener {

    public static AtlasRegion[] a;
    static int c;

    public GameActivity d;
    public int e;
    private boolean pendingTouch;
    private int pendingTouchX;
    private int pendingTouchY;

    public FlappyScene(GameActivity activity,
                       org.andengine.opengl.texture.region.ITextureRegion atlasRegion) {
        super();
        pendingTouch = false;
        d = activity;

        int bestScore = activity.getSharedPreferences("FlappyBird", 0).getInt("score", 0);
        com.dotgears.flappy.GameScene gameScene = new com.dotgears.flappy.GameScene(
                bestScore, 0,
                activity.getResources().openRawResource(R.raw.atlas));
        Renderer.D = gameScene;
        Renderer.D.init();
        MathTables.seed((int) System.currentTimeMillis());

        a = new AtlasRegion[50];
        for (int idx = 0; idx < 50; idx++) {
            org.andengine.entity.sprite.Sprite sprite =
                    new org.andengine.entity.sprite.Sprite(0, 0, atlasRegion.deepCopy(),
                            activity.getVertexBufferObjectManager());
            sprite.setVisible(false);
            attachChild(sprite);
        }
        setOnSceneTouchListener(this);
    }

    public static void enqueueSprite(int spriteIndex, int x, int y) {
    }

    public static void enqueueSpriteRotated(AtlasRegion region, int x, int y,
                                            int rotDeg, float sx, float sy) {
    }

    public static void enqueueSpriteFull(int spriteIndex, int x, int y, int w, int h,
                                         float u1, float v1, float u2, float v2, float alpha) {
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        if (pendingTouch) {
            Renderer.D.setTouchPoint(pendingTouchX, pendingTouchY);
            pendingTouch = false;
        } else {
            Renderer.D.clearTouch();
        }
        Renderer.D.tick(pSecondsElapsed);
        dispatchEvents();
        super.onManagedUpdate(pSecondsElapsed);
    }

    private void dispatchEvents() {
        for (int idx = 0; idx < Renderer.D.v; idx++) {
            int type = Renderer.D.w[idx];
            int data = (int) Renderer.D.x[idx];
            switch (type) {
                case 4:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && d.isSignedIn()) {
                        PlayGames.getLeaderboardsClient(d)
                                .submitScore(d.getString(R.string.leaderboard_id), data);
                    }
                    if (data > e) {
                        d.getSharedPreferences("FlappyBird", 0)
                                .edit()
                                .putInt("score", data)
                                .apply();
                        e = data;
                    }
                    break;
                case 5:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && d.isSignedIn()) {
                        PlayGames.getLeaderboardsClient(d)
                                .getLeaderboardIntent(d.getString(R.string.leaderboard_id))
                                .addOnSuccessListener(intent ->
                                        d.startActivityForResult(intent, 1));
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        PlayGames.getGamesSignInClient(d).signIn();
                    }
                    break;
                case 7:
                    d.playSfxPoint();
                    break;
                case 8:
                    d.playSfxDie();
                    break;
                case 9:
                    d.playSfxWing();
                    break;
                case 10:
                    d.playSfxHit();
                    break;
                case 11:
                    try {
                        Intent rateIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=com.dotgears.flappybird"));
                        d.startActivity(rateIntent);
                    } catch (Exception ignored) {
                    }
                    break;
                case 12:
                    d.playSfxSwoosh();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public boolean onSceneTouchEvent(Scene scene, TouchEvent touchEvent) {
        if (touchEvent.isActionDown() || touchEvent.isActionMove()
                || touchEvent.isActionPointerDown() || touchEvent.isActionOutside()) {
            pendingTouch = true;
            pendingTouchX = (int) touchEvent.getX();
            pendingTouchY = (int) touchEvent.getY();
        }
        return true;
    }
}
