# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\AndroidStudio1.5\SDK/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

   #-------------------------------------------custom----------------------------------------------
   #---------------------------------1.bean---------------------------------
    -keep class com.io.sdchain.bean.** { *; }


   #-------------------------------------------------------------------------

   #-------------------------------------------------------------------------

   #---------------------------------2.Third-party packages-------------------------------

   #Retrofit
   # Platform calls Class.forName on types which do not exist on Android to determine platform.
   -dontnote retrofit2.Platform
   # Platform used when running on Java 8 VMs. Will not be used at runtime.
   -dontwarn retrofit2.Platform$Java8
   # Retain generic type information for use by reflection by converters and adapters.
   -keepattributes Signature
   # Retain declared checked exceptions for use by a Proxy instance.
   -keepattributes Exceptions


   #Glide
   -keep public class * implements com.bumptech.glide.module.GlideModule
   -keep public class * extends com.bumptech.glide.AppGlideModule
   -keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
     **[] $VALUES;
     public *;
   }

   # for DexGuard only
   # -keepresourcexmlelements manifest/application/meta-data@value=GlideModule


   # ProGuard configurations for Bugtags
     -keepattributes LineNumberTable,SourceFile

     -keep class com.bugtags.library.** {*;}
     -dontwarn com.bugtags.library.**
     -keep class io.bugtags.** {*;}
     -dontwarn io.bugtags.**
     -dontwarn org.apache.http.**
     -dontwarn android.net.http.AndroidHttpClient

     # End Bugtags

      #
      -dontwarn com.yanzhenjie.permission.**

      # pyg
#      -libraryjars libs/pgyer_sdk_x.x.jar
      -dontwarn com.pgyersdk.**
      -keep class com.pgyersdk.** { *; }

      #bugly-tinker
      -dontwarn com.tencent.bugly.**
      -keep public class com.tencent.bugly.**{*;}

      # Rxjava2
      #Rxjava RxAndroid
      -dontwarn rx.*
      -dontwarn sun.misc.**
      -keepclassmembers class rx.internal.util.unsafe.*ArrayQuene*Field*{
            long producerIndex;
            long consumerIndex;
      }

      -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
            rx.internal.util.atomic.LinkedQueueNode producerNode;
            rx.internal.util.atomic.LinkedQueueNode consumerNode;
      }
      -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
            rx.internal.util.atomic.LinkedQueueNode consumerNode;
      }

      #Refresh the library
      #app Confuse tags
      -keep class com.github.mmin18.** {*;}

      -dontwarn android.support.v8.renderscript.*
      -keepclassmembers class android.support.v8.renderscript.RenderScript {
        native *** rsn*(...);
        native *** n*(...);
      }

      -keep class com.wang.avi.** { *; }
      -keep class com.wang.avi.indicators.** { *; }

      #PictureSelector 2.0
      -keep class com.luck.picture.lib.** { *; }

      -dontwarn com.yalantis.ucrop**
      -keep class com.yalantis.ucrop** { *; }
      -keep interface com.yalantis.ucrop** { *; }

       #rxjava
      -dontwarn sun.misc.**
      -keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
       long producerIndex;
       long consumerIndex;
      }
      -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
       rx.internal.util.atomic.LinkedQueueNode producerNode;
      }
      -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
       rx.internal.util.atomic.LinkedQueueNode consumerNode;
      }

      #rxandroid
      -dontwarn sun.misc.**
      -keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
         long producerIndex;
         long consumerIndex;
      }
      -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
          rx.internal.util.atomic.LinkedQueueNode producerNode;
      }
      -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
          rx.internal.util.atomic.LinkedQueueNode consumerNode;
      }

      # for DexGuard only
#      -keepresourcexmlelements manifest/application/meta-data@value=GlideModule

      ##---------------Begin: proguard configuration for Gson  ----------
      # Gson uses generic type information stored in a class file when working with fields. Proguard
      # removes such information by default, so configure it to keep all of it.
      -keepattributes Signature

      # For using GSON @Expose annotation
      -keepattributes *Annotation*

      # Gson specific classes
      -dontwarn sun.misc.**
      #-keep class com.google.gson.stream.** { *; }

      # Application classes that will be serialized/deserialized over Gson
      -keep class com.google.gson.examples.android.model.** { *; }

      # Prevent proguard from stripping interface information from TypeAdapterFactory,
      # JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
      -keep class * implements com.google.gson.TypeAdapterFactory
      -keep class * implements com.google.gson.JsonSerializer
      -keep class * implements com.google.gson.JsonDeserializer

      ##---------------End: proguard configuration for Gson  ----------

      ##EventBus
      -keepattributes *Annotation*
      -keepclassmembers class * {
          @org.greenrobot.eventbus.Subscribe <methods>;
      }
      -keep enum org.greenrobot.eventbus.ThreadMode { *; }

      # Only required if you use AsyncExecutor
      -keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
          <init>(java.lang.Throwable);
      }

      #greendao
      -keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
      public static java.lang.String TABLENAME;
      }
      -keep class **$Properties

      # If you do not use SQLCipher:
      -dontwarn org.greenrobot.greendao.database.**
      # If you do not use Rx:
      -dontwarn rx.**

      #permission
      -dontwarn com.yanzhenjie.permission.**



   #-------------------------------------------------------------------------

   #---------------------------------3.class call with js------------------------



   #-------------------------------------------------------------------------

   #---------------------------------4.reflect -----------------------



   #----------------------------------------------------------------------------
   #---------------------------------------------------------------------------------------------------

   #-------------------------------------------base--------------------------------------------
   #---------------------------------base----------------------------------
   -optimizationpasses 5
   -dontskipnonpubliclibraryclassmembers
   -printmapping proguardMapping.txt
   -optimizations !code/simplification/cast,!field/*,!class/merging/*
   -keepattributes *Annotation*,InnerClasses
   -keepattributes Signature
   -keepattributes SourceFile,LineNumberTable
   #----------------------------------------------------------------------------

   #---------------------------------default save---------------------------------
   -keep public class * extends android.app.Activity
   -keep public class * extends android.app.Application
   -keep public class * extends android.app.Service
   -keep public class * extends android.content.BroadcastReceiver
   -keep public class * extends android.content.ContentProvider
   -keep public class * extends android.app.backup.BackupAgentHelper
   -keep public class * extends android.preference.Preference
   -keep public class * extends android.view.View
   -keep public class com.android.vending.licensing.ILicensingService
   -keep class android.support.** {*;}

   -keep public class * extends android.view.View{
       *** get*();
       void set*(***);
       public <init>(android.content.Context);
       public <init>(android.content.Context, android.util.AttributeSet);
       public <init>(android.content.Context, android.util.AttributeSet, int);
   }
   -keepclasseswithmembers class * {
       public <init>(android.content.Context, android.util.AttributeSet);
       public <init>(android.content.Context, android.util.AttributeSet, int);
   }
   -keepclassmembers class * implements java.io.Serializable {
       static final long serialVersionUID;
       private static final java.io.ObjectStreamField[] serialPersistentFields;
       private void writeObject(java.io.ObjectOutputStream);
       private void readObject(java.io.ObjectInputStream);
       java.lang.Object writeReplace();
       java.lang.Object readResolve();
   }
   -keep class **.R$* {
    *;
   }
   -keepclassmembers class * {
       void *(**On*Event);
   }
   #----------------------------------------------------------------------------

   #---------------------------------webview------------------------------------
   -keepclassmembers class fqcn.of.javascript.interface.for.Webview {
      public *;
   }
   -keepclassmembers class * extends android.webkit.WebViewClient {
       public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
       public boolean *(android.webkit.WebView, java.lang.String);
   }
   -keepclassmembers class * extends android.webkit.WebViewClient {
       public void *(android.webkit.WebView, jav.lang.String);
   }

    #lambda reflect and pinyin4j-2.5.0.jar
   -dontwarn java.beans.**
   -dontwarn java.awt.**
   -dontwarn javax.swing.**
   -dontwarn java.lang.invoke.*
   -dontwarn demo.Pinyin4jAppletDemo**
   -keep class demo.Pinyin4jAppletDemo{*;}

   #----------------------------------------------------------------------------
   #---------------------------------------------------------------------------------------------------

-dontwarn cn.bertsir.zbar.**
-keep class cn.bertsir.zbar.** { *; }

#-dontwarn cn.bertsir.zbar.view.**
#-keep class cn.bertsir.zbar.view.** { *; }



#ARouter
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}

# If you get the Service byType, add the following rule to protect the interface
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider

# If a single class injection is used, that is, the interface implementation IProvider is not defined, you need to add the following rules to protect the implementation
-keep class * implements com.alibaba.android.arouter.facade.template.IProvider
-dontwarn javax.lang.model.element.Element

#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewInjector{ *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}