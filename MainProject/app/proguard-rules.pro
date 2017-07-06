# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/MaShiZhao/Android/sdk/tools/proguard/proguard-android.txt
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
# 代码混淆压缩比，在0~7之间，默认为5，一般不做修改
-optimizationpasses 5

# 混合时不使用大小写混合，混合后的类名为小写
-dontusemixedcaseclassnames

# 指定不去忽略非公共库的类
-dontskipnonpubliclibraryclasses

# 这句话能够使我们的项目混淆后产生映射文件
# 包含有类名->混淆后类名的映射关系
-verbose

# 指定不去忽略非公共库的类成员
-dontskipnonpubliclibraryclassmembers

# 不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。
-dontpreverify

# 保留Annotation不混淆
-keepattributes *Annotation*,InnerClasses

# 避免混淆泛型
-keepattributes Signature

# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable

# 指定混淆是采用的算法，后面的参数是一个过滤器
# 这个过滤器是谷歌推荐的算法，一般不做更改
-optimizations !code/simplification/cast,!field/*,!class/merging/*

# 保留我们使用的四大组件，自定义的Application等等这些类不被混淆
# 因为这些子类都有可能被外部调用
#-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
#-keep public class * extends android.app.Service
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.app.backup.BackupAgentHelper
#-keep public class * extends android.preference.Preference
#-keep public class * extends android.view.View
#-keep public class com.android.vending.licensing.ILicensingService


# 保留support下的所有类及其内部类
#-keep class android.support.** {*;}

# 保留继承的
#-keep public class * extends android.support.v4.**
#-keep public class * extends android.support.v7.**
#-keep public class * extends android.support.annotation.**

# 保留R下面的资源
-keep class **.R$* {*;}

# 保留本地native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保留在Activity中的方法参数是view的方法，
# 这样以来我们在layout中写的onClick就不会被影响
#-keepclassmembers class * extends android.app.Activity{
#    public void *(android.view.View);
#}

# 保留枚举类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保留Parcelable序列化类不被混淆
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# 保留Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

## 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
#-keepclassmembers class * {
#    void *(**On*Event);
#    void *(**On*Listener);
#}

# webView处理，项目中没有使用到webView忽略即可
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
    public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}

#使用Gson时需要配置Gson的解析对象及变量都不混淆。不然Gson会找不到变量。
# 将下面替换成自己的实体类
-keep class com.coder.kzxt.**.beans.** { *; }


#腾讯im https://www.qcloud.com/document/product/269/9227
-keep class com.tencent.**{*;}
-dontwarn com.tencent.**

-keep class tencent.**{*;}
-dontwarn tencent.**

-keep class qalsdk.**{*;}
-dontwarn qalsdk.**

#高德 http://lbs.amap.com/api/android-location-sdk/guide/create-project/dev-attention
##2D地图
#-keep class com.amap.api.maps2d.**{*;}
#-keep class com.amap.api.mapcore2d.**{*;}
##搜索
#-keep   class com.amap.api.services.**{*;}
##定位
#-keep class com.amap.api.location.**{*;}
#-keep class com.amap.api.fence.**{*;}
#-keep class com.autonavi.aps.amapapi.model.**{*;}
##导航
#-keep class com.amap.api.navi.**{*;}
#-keep class com.autonavi.**{*;}

-dontwarn com.amap.api.**
-dontwarn com.amap.apis.**
-dontwarn com.a.a.**
-dontwarn com.autonavi.**
-keep class com.amap.api.** {*;}
-keep class com.amap.apis.** {*;}
-keep class com.autonavi.** {*;}
-keep class com.a.a.** {*;}

#百度推送 http://push.baidu.com/doc/android/api
-dontwarn com.baidu.**
-keep class com.baidu.**{*; }

##华为
#-dontwarn com.huawei.android.pushagent.**
#-keep class com.huawei.android.pushagent.** { *; }
#-dontwarn com.huawei.android.pushselfshow.**
#-keep class com.huawei.android.pushselfshow.** { *; }
#-dontwarn com.huawei.android.microkernel.**
#-keep class com.huawei.android.microkernel.** { *; }
##小米
#-keep public class * extends android.content.BroadcastReceiver
##这里com.xiaomi.mipushdemo.DemoMessageRreceiver改成app中定义的完整类名
#-keep class com.coder.kzxt.broadcast.BaiduMessageReceiver {*;}

#百度统计 https://mtj.baidu.com/web/help/article?id=76&type=0
-keep class com.baidu.bottom.** { *; }
-keep class com.baidu.kirin.** { *; }
-keep class com.baidu.mobstat.** { *; }

#百度播放器 https://cloud.baidu.com/doc/MCT/Android-Player-SDK-2.0.html#.E9.98.B2.E6.B7.B7.E6.B7.86.E8.AE.BE.E7.BD.AE
-keep class com.baidu.cloud.media.**{ *;}

#okhttp
-dontwarn okhttp3.**
-dontwarn okio.*
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}

#share SDK
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-keep class com.mob.**{*;}
-dontwarn com.mob.**
-dontwarn cn.sharesdk.**
-dontwarn **.R$*

#zxing
-keep class com.google.zxing.**{*;}
-dontwarn com.google.zxing.**
-dontwarn com.intouch.dev.alacards.**

-keepclasseswithmembernames class * { native <methods>; }
-keep class com.android.internal.telephony.** {*;}

#UtoVR
-dontwarn com.google.**
-dontwarn android.media.**
-keepattributes InnerClasses, Signature, *Annotation*
-keep class com.utovr.** {*;}
-keep class com.google.** {*;}
-keep class android.media.** {*;}

#支付宝支付 https://docs.open.alipay.com/204/105296/
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep class com.alipay.**{ public *;}
-dontwarn android.net.SSLCertificateSocketFactory

#Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#xutils
-dontwarn com.lidroid.xutils.**
-keep class com.lidroid.xutils.** { *; }