# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in F:\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#推送所需
-dontoptimize
-dontpreverify
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

#反射
-keepattributes Signature
-keepattributes EnclosingMethod

#移除部分log代码
-assumenosideeffects class android.util.Log {
    public static *** v(...);
    public static *** i(...);
    public static *** d(...);
    public static *** w(...);
    public static *** e(...);
    public static *** a(...);
}

-keep interface com.doormaster.vphone.inter.DMModelCallBack$DMCallback {
    public <methods>;
}
-keep class com.doormaster.vphone.inter.DMVPhoneModel{
    public <fields>;
    public <methods>;
    }
-keep class com.doormaster.vphone.exception.DMException{
    public <methods>;
}
-keep class org.linphone.LinphoneService{
    public <fields>;
    public <methods>;
}
#-keep class org.linphone.KeepAliveHandler
#-keep class org.linphone.BluetoothManager
#-keep class org.linphone.BootReceiver
#-keep class org.linphone.NetworkManager
#-keep class org.linphone.PhoneStateChangedReceiver
#-keep class org.linphone.gcm.**
-keep class org.linphone.**{ *; }
-dontwarn org.linphone.**
-keep class de.timroes.**{ *; }
-dontwarn de.timroes.**
-keep class com.doormaster.vphone.service.**
-keep class com.doormaster.vphone.receiver.**
-keep class com.doormaster.vphone.activity.Act_Call
-keep class com.doormaster.vphone.activity.Act_CallIncoming
-keep class com.doormaster.vphone.activity.Act_CallOutgoing

-dontwarn okio.**
-keep class okio.** { *; }

-dontwarn qihoo.appstore.**
-keep class qihoo.appstore.** { *; }
-dontwarn qihoo360.mobilesafe.util.**
-keep class qihoo360.mobilesafe.util.** { *; }

-dontwarn android.app.Notification

 -keep class com.baidu.appsearch.patchupdate.**{*;}
 -keep class com.baidu.android.common.**{*;}
 -keep class com.baidu.autoupdatesdk.**{*;}