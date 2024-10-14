plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.login_page_project"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.login_page_project"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        viewBinding = true
    }
}



        dependencies {
            implementation("com.google.firebase:firebase-analytics")
            implementation("com.squareup.picasso:picasso:2.71828")
            implementation(platform("com.google.firebase:firebase-bom:33.2.0"))
    implementation("org.postgresql:postgresql:42.7.2")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
            implementation("org.chromium.net:cronet-embedded:119.6045.31")
            implementation("androidx.activity:activity:1.8.0")
            implementation("com.google.firebase:firebase-inappmessaging:21.0.0")
            implementation("com.android.billingclient:billing-ktx:7.0.0")
            implementation("com.google.android.gms:play-services-analytics-impl:18.1.0")
            implementation("com.google.firebase:firebase-auth-ktx:23.0.0")
            implementation("com.google.firebase:firebase-firestore-ktx:25.1.0")
            implementation("com.google.firebase:firebase-storage-ktx:21.0.0")
            implementation("com.google.firebase:firebase-database:21.0.0")
            testImplementation("junit:junit:4.13.2")
    implementation("com.squareup.okhttp3:okhttp:4.7.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")
    implementation("com.android.volley:volley:1.2.1")

            implementation ("com.squareup.retrofit2:retrofit:2.9.0")
            implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
            implementation ("com.squareup.retrofit2:converter-scalars:2.9.0")
            implementation("com.squareup.retrofit2:adapter-rxjava2:2.9.0")
            implementation("com.squareup.okhttp3:okhttp:4.9.0")
            implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

        }