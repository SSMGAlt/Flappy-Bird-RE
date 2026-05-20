package com.dotgears.flappy;

import android.content.Intent;

import com.dotgears.GameActivity;

public class SplashRunnable implements Runnable {

    private final SplashScreen a;

    public SplashRunnable(SplashScreen splashScreen) {
        a = splashScreen;
    }

    @Override
    public void run() {
        Intent intent = new Intent(a, GameActivity.class);
        a.startActivity(intent);
        a.finish();
    }
}
