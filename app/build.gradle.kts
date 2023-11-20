plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.yf.tool"
    compileSdk = rootProject.ext.get("compileSdk").toString().toInt()

    defaultConfig {
        applicationId = "com.yf.tool"
        minSdk = rootProject.ext.get("minSdk").toString().toInt()
        targetSdk = rootProject.ext.get("targetSdk").toString().toInt()
        versionCode = rootProject.ext.get("versionCode").toString().toInt()
        versionName = rootProject.ext.get("versionName").toString()
        buildConfigField(type = "String", name = "VERSION_NAME", value = "\"${rootProject.ext.get("versionName").toString()}\"")
        buildConfigField(type = "int", name = "VERSION_CODE", value = "${rootProject.ext.get("versionCode").toString().toInt()}")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
        dataBinding = true
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    ksp {
        arg("rxhttp_rxjava", "3.1.6")
        arg("rxhttp_package", "rxHttp")  //指定RxHttp类包名，可随意指定
    }

    sourceSets {
        getByName("main") {
            jniLibs.srcDir("libs")
        }
    }
    // 自定义多渠道打包 apk名称
    applicationVariants.all {
        val buildType = buildType.name
        outputs.all {
            if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                if (buildType == "release") {
                    outputFileName = "锎元售货机_${defaultConfig.versionName}_${defaultConfig.versionCode}.apk"
                }
            }
        }
    }
}

dependencies {
    api(fileTree("libs").include("*.jar", "*.aar"))
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation(project(mapOf("path" to ":lib_common")))
}
apply(plugin = "io.objectbox")