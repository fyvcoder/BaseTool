plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    id("maven-publish")
}

group = "com.github.fyvcoder"

android {
    namespace = "com.yf.common"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        buildConfig = true
        dataBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //lifecycle
    api("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

    //协程
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    //https://developer.android.google.cn/jetpack/androidx/releases/activity#groovy
    api("androidx.activity:activity-ktx:1.7.0")
    //XUI框架 https://github.com/xuexiangjys/XUI/blob/master/README_ZH.md
    api("com.github.xuexiangjys:XUI:1.2.1")

    // Json 解析框架：https://github.com/google/gson
    api("com.google.code.gson:gson:2.10.1")

    //AndroidAutoSize屏幕适配 https://github.com/JessYanCoding/AndroidAutoSize
    api("com.github.JessYanCoding:AndroidAutoSize:v1.2.1")

    //爱奇艺XCrash https://github.com/iqiyi/xCrash
    api("com.iqiyi.xcrash:xcrash-android-lib:3.1.0")

    //基础功能的工具类 https://github.com/xuexiangjys/XUtil
    api("com.github.xuexiangjys.XUtil:xutil-core:2.0.0")
    //附加功能的工具类 https://github.com/xuexiangjys/XUtil
    api("com.github.xuexiangjys.XUtil:xutil-sub:2.0.0")

    //日志打印 https://github.com/elvishew/XLog/blob/master/README_ZH.md
    api("com.elvishew:xlog:1.10.1")

    //RV万能Adapter,自带上拉加载 https://github.com/CymChad/BaseRecyclerViewAdapterHelper
    api("io.github.cymchad:BaseRecyclerViewAdapterHelper:4.0.1")

    //Rxjava https://github.com/ReactiveX/RxJava
    api("io.reactivex.rxjava3:rxjava:3.1.6")
    //RxAndroid https://github.com/ReactiveX/RxAndroid
    api("io.reactivex.rxjava3:rxandroid:3.0.2")

    //Rxjava生命周期管理 https://github.com/liujingxing/RxLife
    api("com.github.liujingxing.rxlife:rxlife-rxjava3:2.2.2")

    //okhttp3 https://github.com/square/okhttp
    api("com.squareup.okhttp3:okhttp:4.12.0")

    val rxHttp_version = "3.2.2"
    //Rxhttp https://github.com/liujingxing/rxhttp
    api("com.github.liujingxing.rxhttp:rxhttp:$rxHttp_version")
    // ksp/kapt/annotationProcessor 选其一
    ksp("com.github.liujingxing.rxhttp:rxhttp-compiler:$rxHttp_version")

    //Object-Box 用浏览器连接数据查看数据  https://docs.objectbox.io/data-browser
    val objectBox_version = "3.7.1"
    debugApi("io.objectbox:objectbox-android-objectbrowser:$objectBox_version")
    releaseApi("io.objectbox:objectbox-android:$objectBox_version")

    //七牛云 https://developer.qiniu.com/kodo/1236/android
    api("com.qiniu:qiniu-android-sdk:8.6.0") {
        exclude(group = "com.squareup.okhttp3", module = "okhttp")
    }

    //图片加载 https://github.com/coil-kt/coil
    api("io.coil-kt:coil:2.5.0")
    api("io.coil-kt:coil-gif:2.5.0")

    //内存泄漏检测 https://github.com/square/leakcanary
//    debugImplementation 'com.squareup.leakcanary:leakcanary-android:2.10'

    // CameraX core library https://developer.android.google.cn/training/camerax/architecture?hl=zh-cn#kts
    val camerax_version = "1.3.0-alpha05"
    api("androidx.camera:camera-core:$camerax_version")
    // CameraX Camera2 extensions
    api("androidx.camera:camera-camera2:$camerax_version")
    // CameraX Lifecycle library
    api("androidx.camera:camera-lifecycle:$camerax_version")
    // CameraX View class
    api("androidx.camera:camera-view:$camerax_version")

    //串口通信 https://github.com/BASS-HY/EasySerial
//    implementation 'com.github.BASS-HY:EasySerial:1.0.0-beta03'
    //串口通信 https://github.com/GeekBugs/Android-SerialPort
//    implementation 'com.github.F1ReKing:Android-SerialPort:1.5.1'
    //USB串口通信 https://github.com/mik3y/usb-serial-for-android
    api("com.github.mik3y:usb-serial-for-android:3.7.0")

    //https://github.com/dlew/joda-time-android
    api("net.danlew:android.joda:2.12.5")
}

//apply(plugin = "io.objectbox")