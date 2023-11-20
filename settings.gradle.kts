pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        flatDir {
            dir("libs")
        }
        maven { setUrl("https://maven.aliyun.com/repository/central") }
        maven { setUrl("https://maven.aliyun.com/repository/google") }
        maven { setUrl("https://maven.aliyun.com/repository/gradle-plugin") }
        maven { setUrl("https://maven.aliyun.com/repository/jcenter") }
        maven { setUrl("https://maven.aliyun.com/repository/public") }
        jcenter() // Warning: this repository is going to shut down soon
        maven { setUrl("https://jitpack.io") }
        maven { setUrl("https://oss.jfrog.org/libs-snapshot") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        flatDir {
            dir("libs")
        }
        maven { setUrl("https://maven.aliyun.com/repository/central") }
        maven { setUrl("https://maven.aliyun.com/repository/google") }
        maven { setUrl("https://maven.aliyun.com/repository/gradle-plugin") }
        maven { setUrl("https://maven.aliyun.com/repository/jcenter") }
        maven { setUrl("https://maven.aliyun.com/repository/public") }
        jcenter() // Warning: this repository is going to shut down soon
        maven { setUrl("https://jitpack.io") }
        maven { setUrl("https://oss.jfrog.org/libs-snapshot") }
    }
}

rootProject.name = "YfBaseTool"
include(":app")
include(":lib_common")
