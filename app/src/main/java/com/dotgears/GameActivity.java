package com.dotgears;

import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.google.android.gms.games.PlayGames;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;

public class GameActivity extends SimpleBaseGameActivity {

    private static final int CAMERA_WIDTH = 288;

    private Camera mCamera;
    private BitmapTextureAtlas mTextureAtlas;
    private ITextureRegion mAtlasRegion;
    private FlappyScene mScene;
    private SoundPool mSoundPool;
    private int mSfxWing;
    private int mSfxPoint;
    private int mSfxHit;
    private int mSfxDie;
    private int mSfxSwoosh;
    private boolean mSignedIn;

    @Override
    public EngineOptions onCreateEngineOptions() {
        int screenW;
        int screenH;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            android.graphics.Rect bounds = getWindowManager().getCurrentWindowMetrics().getBounds();
            screenW = bounds.width();
            screenH = bounds.height();
        } else {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            screenW = metrics.widthPixels;
            screenH = metrics.heightPixels;
        }

        if (screenW <= 0) screenW = CAMERA_WIDTH;
        if (screenH <= 0) screenH = 512;

        int virtualHeight = (int) ((float) CAMERA_WIDTH * screenH / screenW);

        com.dotgears.flappy.GameScene.VIRTUAL_WIDTH  = CAMERA_WIDTH;
        com.dotgears.flappy.GameScene.VIRTUAL_HEIGHT = virtualHeight;

        mCamera = new Camera(0, 0, CAMERA_WIDTH, virtualHeight);

        return new EngineOptions(
                true,
                ScreenOrientation.PORTRAIT_FIXED,
                new FillResolutionPolicy(),
                mCamera);
    }

    @Override
    public void onCreateResources() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        mTextureAtlas = new BitmapTextureAtlas(getTextureManager(), 1024, 1024);
        mAtlasRegion = BitmapTextureAtlasTextureRegionFactory
                .createFromAsset(mTextureAtlas, this, "atlas.png", 0, 0);
        mTextureAtlas.load();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes attrs = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            mSoundPool = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(attrs)
                    .build();
        } else {
            mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }

        mSfxWing   = loadSound("sounds/sfx_wing.ogg");
        mSfxPoint  = loadSound("sounds/sfx_point.ogg");
        mSfxHit    = loadSound("sounds/sfx_hit.ogg");
        mSfxDie    = loadSound("sounds/sfx_die.ogg");
        mSfxSwoosh = loadSound("sounds/sfx_swooshing.ogg");
    }

    private int loadSound(String assetPath) {
        try {
            AssetFileDescriptor afd = getAssets().openFd(assetPath);
            int id = mSoundPool.load(afd, 1);
            afd.close();
            return id;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public Scene onCreateScene() {
        mScene = new FlappyScene(this, mAtlasRegion);
        return mScene;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            PlayGames.getGamesSignInClient(this)
                    .isAuthenticated()
                    .addOnCompleteListener(task ->
                            mSignedIn = task.isSuccessful()
                                    && task.getResult().isAuthenticated());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSoundPool != null) mSoundPool.release();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isSignedIn() {
        return mSignedIn;
    }

    private void playSound(int soundId) {
        if (mSoundPool != null && soundId != 0) {
            mSoundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
        }
    }

    public void playSfxWing()   { playSound(mSfxWing); }
    public void playSfxPoint()  { playSound(mSfxPoint); }
    public void playSfxHit()    { playSound(mSfxHit); }
    public void playSfxDie()    { playSound(mSfxDie); }
    public void playSfxSwoosh() { playSound(mSfxSwoosh); }
}
