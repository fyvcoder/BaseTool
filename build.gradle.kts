// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.4" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.android.library") version "8.1.4" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.github.dcendents:android-maven-gradle-plugin:2.1")
        classpath("io.objectbox:objectbox-gradle-plugin:3.7.1")
    }
}

ext {
    set("compileSdk", 34)
    set("minSdk", 21)
    set("targetSdk", 34)
    set("versionCode", 1)
    set("versionName", "1.0.0")
}