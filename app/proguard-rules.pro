# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

## Overall settings
# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile

-repackageclasses com.yhvictor.discuzclient.internal

## Proguard annotations
-keep,allowobfuscation class proguard.annotation.*
-keepnames @proguard.annotation.KeepName class *
-keepclassmembernames class * {
    @proguard.annotation.KeepName *;
}

## Logging
-assumenosideeffects class com.yhvictor.discuzclient.debug.Logger


## OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform

## Guava
-dontwarn java.lang.ClassValue
-dontwarn sun.misc.Unsafe

## retrofit
# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}