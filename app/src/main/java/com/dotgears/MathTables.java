package com.dotgears;

public class MathTables {

    public static float A;
    public static float B;

    public static float[] a;
    public static float[] b;
    public static float[] c;
    public static float[] d;
    public static float[] e;
    public static float[] f;
    public static float[] g;
    public static float[] h;
    public static float[] i;
    public static float[] j;
    public static float[] k;
    public static float[] l;
    public static float[] m;
    public static float[] n;
    public static float[] o;
    public static float[] p;
    public static float[] q;
    public static float[] r;
    public static float[] s;
    public static float[] t;
    public static float[] u;
    public static float[] v;
    public static float[] w;

    public static MathTables x;

    public static int y;
    public static int z;

    public MathTables() {
        a = new float[360];
        b = new float[360];
        for (int idx = 0; idx < 360; idx++) {
            float rad = idx * (float) Math.PI / 180.0f;
            a[idx] = (float) Math.sin(rad);
            b[idx] = (float) Math.cos(rad);
        }

        c = new float[101]; d = new float[101]; e = new float[101];
        f = new float[101]; g = new float[101]; h = new float[101];
        i = new float[101]; j = new float[101]; k = new float[101];
        l = new float[101]; m = new float[101]; n = new float[101];
        o = new float[101]; p = new float[101]; q = new float[101];
        r = new float[101]; s = new float[101]; t = new float[101];
        u = new float[101]; v = new float[101]; w = new float[101];

        for (int idx = 0; idx <= 100; idx++) {
            double pct = idx / 100.0;
            c[idx]  = (float) easeInQuad(pct);
            d[idx]  = (float) easeOutQuad(pct);
            e[idx]  = (float) easeInCubic(pct);
            f[idx]  = (float) easeOutCubic(pct);
            g[idx]  = (float) easeInQuart(pct);
            h[idx]  = (float) easeOutQuart(pct);
            i[idx]  = (float) easeInQuint(pct);
            j[idx]  = (float) easeOutQuint(pct);
            k[idx]  = (float) easeInSextic(pct);
            l[idx]  = (float) easeOutSextic(pct);
            m[idx]  = (float) easeInBack(pct);
            n[idx]  = (float) easeOutBack(pct);
            o[idx]  = (float) easeInOutBack(pct);
            p[idx]  = (float) easeInElastic(pct);
            q[idx]  = (float) easeOutElastic(pct);
            r[idx]  = q[idx];
            s[idx]  = p[idx];
            t[idx]  = (float) easeInBounce(pct);
            u[idx]  = (float) easeOutBounce(pct);
            v[idx]  = (float) easeInOutBounce(pct);
            w[idx]  = (float) easeSineWave(pct);
        }
    }

    public static float normalizeAngle(float angle) {
        while (angle >= 360.0f) angle -= 360.0f;
        while (angle < 0.0f)    angle += 360.0f;
        return angle;
    }

    public static int nextRandom() {
        int tmp = (z & 0xFFFF) * 0x9069;
        tmp += (z >>> 16);
        z = tmp;
        int tmp2 = (y & 0xFFFF) * 0x4650;
        tmp2 += (y >>> 16);
        y = tmp2;
        return Math.abs((z << 16) + y);
    }

    public static int randomRange(int min, int max) {
        return nextRandom() % (max - min) + min;
    }

    public static void rotatePoint(float px, float py, float cx, float cy, float angleDeg) {
        float ang = normalizeAngle(angleDeg);
        float cos = cosLut(ang);
        float sin = sinLut(ang);
        float dx = px - cx;
        float dy = py - cy;
        A = cos * dx - sin * dy + cx;
        B = sin * dx + cos * dy + cy;
    }

    public static void seed(int seed) {
        android.util.Log.i("FlappyBird", "Engine: Randomize " + seed);
        y = seed % 32000;
        z = seed % 0xFFFF;
    }

    public static float sinLut(float angle) {
        return a[(int) angle];
    }

    public static float cosLut(float angle) {
        return b[(int) angle];
    }

    public static float easeInQuadLut(int pct)       { return c[pct]; }
    public static float easeOutQuadLut(int pct)      { return d[pct]; }
    public static float easeInCubicLut(int pct)      { return e[pct]; }
    public static float easeOutCubicLut(int pct)     { return f[pct]; }
    public static float easeInQuartLut(int pct)      { return g[pct]; }
    public static float easeOutQuartLut(int pct)     { return h[pct]; }
    public static float easeInQuintLut(int pct)      { return i[pct]; }
    public static float easeOutQuintLut(int pct)     { return j[pct]; }
    public static float easeInSexticLut(int pct)     { return k[pct]; }
    public static float easeOutSexticLut(int pct)    { return l[pct]; }
    public static float easeInBackLut(int pct)       { return m[pct]; }
    public static float easeOutBackLut(int pct)      { return n[pct]; }
    public static float easeInOutBackLut(int pct)    { return o[pct]; }
    public static float easeInElasticLut(int pct)    { return p[pct]; }
    public static float easeOutElasticLut(int pct)   { return q[pct]; }
    public static float easeInBounceLut(int pct)     { return t[pct]; }
    public static float easeOutBounceLut(int pct)    { return u[pct]; }
    public static float easeInOutBounceLut(int pct)  { return v[pct]; }

    private double easeInQuad(double t)      { return t * t; }
    private double easeOutQuad(double t)     { return -(t * t) + 2.0 * t; }
    private static double easeInCubic(double t)  { return t * t * t; }
    private static double easeOutCubic(double t) { double u = t - 1.0; return u * u * u + 1.0; }
    private static double easeInQuart(double t)  { return t * t * t * t; }
    private static double easeOutQuart(double t) { double u = t - 1.0; return -(u * u * u * u) + 1.0; }
    private static double easeInQuint(double t)  { return t * t * t * t * t; }
    private static double easeOutQuint(double t) { double u = t - 1.0; return u * u * u * u * u + 1.0; }
    private static double easeInSextic(double t) { return t * t * t * t * t * t; }
    private static double easeOutSextic(double t) { double u = t - 1.0; return u * u * u * u * u * u + 1.0; }
    private static double easeInBack(double t)   { return t * t * (20.420352248333657 * t) * Math.pow(2.0, 10.0 * (t - 1.0)); }
    private static double easeOutBack(double t)  { return Math.sin(-20.420352248333657 * (t + 1.0) * t) * Math.pow(2.0, -10.0 * t) + 1.0; }
    private static double easeInOutBack(double t) {
        if (t < 0.5) return Math.sin(20.420352248333657 * 2.0 * t) * 0.5 * Math.pow(2.0, 10.0 * (2.0 * t - 1.0));
        return Math.sin(-20.420352248333657 * (2.0 * t - 1.0 + 1.0)) * Math.pow(2.0, -10.0 * (2.0 * t - 1.0)) * 0.5 + 1.0;
    }
    private static double easeInElastic(double t)  {
        return t * t * t * (t * Math.PI) * Math.sin(t * Math.PI);
    }
    private static double easeOutElastic(double t) {
        double u = 1.0 - t;
        return 1.0 - (u * u * u * (u * Math.PI) * Math.sin(u * Math.PI));
    }
    private static double easeInBounce(double t)   { return 1.0 - easeOutBounce(1.0 - t); }
    private static double easeOutBounce(double t) {
        if (t < 1.0 / 2.75)       return 121.0 * t * t / 16.0;
        if (t < 2.0 / 2.75)       return 9.075 * t * t - 9.9 * t + 3.4;
        if (t < 2.5 / 2.75)       return 12.066481994459833 * t * t - 19.63545706371191 * t + 8.898060941828255;
        return 10.8 * t * t - 20.52 * t + 10.72;
    }
    private static double easeInOutBounce(double t) {
        if (t < 0.5) return easeInBounce(t * 2.0) * 0.5;
        return easeOutBounce(t * 2.0 - 1.0) * 0.5 + 0.5;
    }
    private static double easeSineWave(double t)   { return t * t * t - t * Math.PI * t * Math.sin(t * Math.PI); }
}
