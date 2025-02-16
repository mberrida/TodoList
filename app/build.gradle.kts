plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services") // 🔹 Ajout pour Firebase

}

android {
    namespace = "com.example.todolistapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.todolistapp"
        minSdk = 24
        targetSdk = 35
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
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
        implementation (libs.androidx.navigation.compose.v272)
        implementation (libs.material3)
        implementation(libs.androidx.material3.v120) // 🔹 Assurez-vous d’avoir la dernière version
        implementation(libs.ui)
        implementation(libs.androidx.material.v150)




// 🔹 Firebase SDK (🔥 Authentication & Firestore)
    implementation(platform(libs.firebase.bom.v3270)) // ✅ Mettre Firebase à jour
    implementation(libs.google.firebase.auth.ktx)
    implementation(libs.google.firebase.firestore.ktx)

// 🔹 Navigation
    implementation(libs.androidx.navigation.compose.v275)

// 🔹 Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit.v115)
    androidTestImplementation(libs.androidx.espresso.core.v351)

    implementation (libs.androidx.material)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

}
apply(plugin = "com.google.gms.google-services")