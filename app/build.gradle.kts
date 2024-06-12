import java.util.*

plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    namespace = "io.github.takusan23.newradiosupporter"
    compileSdk = 34

    defaultConfig {
        applicationId = "io.github.takusan23.newradiosupporter"
        minSdk = 30
        targetSdk = 34
        versionCode = 12
        versionName = "3.1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }

    // APK作成と署名の設定
    signingConfigs {
        // 環境変数から取りに行く
        create("release_signing_config") {
            // 存在しない場合はとりあえずスルーする
            if (System.getenv("ENV_SIGN_KEYSTORE_BASE64") != null) {
                // GitHubActionsの環境変数に入れておいた署名ファイルがBase64でエンコードされているので戻す
                System.getenv("ENV_SIGN_KEYSTORE_BASE64").let { base64 ->
                    val decoder = Base64.getDecoder()
                    // ルートフォルダに作成する
                    File("keystore.jks").also { file ->
                        file.createNewFile()
                        file.writeBytes(decoder.decode(base64))
                    }
                }
                // どうやら appフォルダ の中を見に行ってるみたいなのでプロジェクトのルートフォルダを指定する
                storeFile = File(rootProject.projectDir, "keystore.jks")
                keyAlias = System.getenv("ENV_SIGN_KEY_ALIAS")
                keyPassword = System.getenv("ENV_SIGN_KEY_PASSWORD")
                storePassword = System.getenv("ENV_SIGN_STORE_PASSWORD")
            }
        }
    }
    buildTypes {
        release {
            // 署名の設定を適用する
            signingConfig = signingConfigs.getByName("release_signing_config")
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.core:core-splashscreen:1.0.1")

    implementation("androidx.glance:glance-appwidget:1.0.0")
    implementation("androidx.glance:glance-material3:1.0.0")

    val composeBom = platform("androidx.compose:compose-bom:2023.10.01")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:2.7.5")
    androidTestImplementation(composeBom)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    // collectAsStateWithLifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    // てすとこーど
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    testImplementation("io.mockk:mockk:1.13.3")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}
