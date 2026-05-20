-keepattributes *Annotation*
-keepattributes Signature

-keep public class org.andengine.** { public protected *; }
-keep public class com.google.android.gms.** { public protected *; }
-keep public class com.dotgears.GameActivity { public protected *; }
-keep public class com.dotgears.flappy.SplashScreen { public protected *; }
-keep class org.andengine.opengl.GLES20Fix { native <methods>; }
-keep class org.andengine.opengl.util.BufferUtils { native <methods>; }

-dontwarn com.google.android.gms.**
-dontwarn androidx.**
