package com.dotgears;

public class GameObjectPool extends GameObject {

    public GameObject[] a;
    int b;
    int c;

    public GameObjectPool() {
        super();
        a = new GameObject[30];
    }

    public int getCount() {
        return c;
    }

    public void update(float deltaTime) {
        for (int idx = 0; idx < getCount(); idx++) {
            a[idx].update(deltaTime);
        }
    }

    public void draw(Renderer renderer) {
        for (int idx = 0; idx < getCount(); idx++) {
            a[idx].draw(renderer);
        }
    }

    public void add(GameObject obj) {
        a[c] = obj;
        b = 0;
        c++;
    }

    public void resetAll() {
        for (int idx = 0; idx < getCount(); idx++) {
            a[idx].F = false;
            a[idx].G = false;
        }
        b = 0;
    }

    GameObject next() {
        GameObject obj = a[b];
        b++;
        if (b == getCount()) {
            b = 0;
        }
        return obj;
    }
}
