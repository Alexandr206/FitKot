plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //id("androidx.room")
    id("org.jetbrains.kotlin.kapt")
    //id("com.google.devtools.ksp")
    //id("com.google.devtools.ksp") version "1.9.24-1.0.20"
    //kotlin("jvm")
}

android {
    namespace = "com.example.fitkot"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.fitkot"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")//
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")//
    implementation("androidx.activity:activity-compose:1.9.0")//
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))//
    implementation("androidx.compose.ui:ui")//
    implementation("androidx.compose.ui:ui-graphics")//
    implementation("androidx.compose.ui:ui-tooling-preview")//
    implementation("androidx.compose.material3:material3")//
    implementation("androidx.appcompat:appcompat:1.6.1")//
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")//
    implementation("androidx.test:core-ktx:1.5.0")
    implementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.test.espresso:espresso-intents:3.5.1")
    implementation("androidx.test.ext:junit-ktx:1.1.5")
    implementation("com.google.android.material:material:1.12.0")//

    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    //ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    implementation("androidx.room:room-rxjava2:$room_version")
    implementation("androidx.room:room-rxjava3:$room_version")
    implementation("androidx.room:room-guava:$room_version")
    testImplementation("androidx.room:room-testing:$room_version")
    implementation("androidx.room:room-paging:$room_version")



}