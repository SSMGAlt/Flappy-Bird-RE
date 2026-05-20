package com.dotgears;

public class Tweener {

    public float a;
    public float b;
    public float c;
    public float d;
    public float e;
    public int f;
    public boolean g;
    private int h;
    private int i;
    private float j;

    public Tweener() {
        g = true;
        a = 0.0f;
    }

    public void start(float from, float to, int easingMode, float durationSeconds) {
        c = from;
        d = to;
        e = to - from;
        h = (int) (60.0f * durationSeconds);
        j = 1.0f / (float) h;
        i = 0;
        f = easingMode;
        g = false;
        a = c;
    }

    public void tick(float deltaTime) {
        if (g) {
            return;
        }
        i++;
        b = (float) i * j;
        applyEasing();
        a = b * e + c;
        if (i == h) {
            g = true;
            a = d;
            b = 1.0f;
        }
    }

    private void applyEasing() {
        int pct = (int) (b * 100.0f);
        switch (f) {
            default:
                break;
            case 1:  b = MathTables.easeInQuadLut(pct);      break;
            case 2:  b = MathTables.easeOutQuadLut(pct);     break;
            case 3:  b = MathTables.easeInCubicLut(pct);     break;
            case 4:  b = MathTables.easeOutCubicLut(pct);    break;
            case 5:  b = MathTables.easeInQuartLut(pct);     break;
            case 6:  b = MathTables.easeOutQuartLut(pct);    break;
            case 7:  b = MathTables.easeInQuintLut(pct);     break;
            case 8:  b = MathTables.easeOutQuintLut(pct);    break;
            case 9:  b = MathTables.easeInSexticLut(pct);    break;
            case 10: b = MathTables.easeOutSexticLut(pct);   break;
            case 11: b = MathTables.easeInBackLut(pct);      break;
            case 12: b = MathTables.easeOutBackLut(pct);     break;
            case 13: b = MathTables.easeInOutBackLut(pct);   break;
            case 14: b = MathTables.easeInElasticLut(pct);   break;
            case 15: b = MathTables.easeOutElasticLut(pct);  break;
            case 16: b = MathTables.easeOutElasticLut(pct);  break;
            case 17: b = MathTables.easeInElasticLut(pct);   break;
            case 18: b = MathTables.easeInBounceLut(pct);    break;
            case 19: b = MathTables.easeOutBounceLut(pct);   break;
            case 20: b = MathTables.easeInOutBounceLut(pct); break;
            case 21: b = MathTables.easeInBounceLut(pct);    break;
        }
    }
}
