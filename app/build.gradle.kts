val retrofitVersion: String by project
val okhttpVersion: String by project
val daggerHiltVersion: String by project
val roomVersion: String by project
val pagingVersion: String by project
val hiltNavigationComposeVersion: String by project
val lifecycleVersion: String by project

plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("com.google.devtools.ksp")
  id("com.google.dagger.hilt.android")
}

android {
  namespace = "com.kenkoro.taurus.mobile_client"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.kenkoro.taurus.mobile_client"
    minSdk = 24
    targetSdk = 34
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
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_1_8.toString()
  }
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.3"
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

dependencies {
  implementation("com.google.dagger:dagger-android:$daggerHiltVersion")
  implementation("com.google.dagger:hilt-android:$daggerHiltVersion")
  ksp("com.google.dagger:hilt-android-compiler:$daggerHiltVersion")
  kspAndroidTest("com.google.dagger:hilt-android-compiler:$daggerHiltVersion")

  implementation("androidx.room:room-paging:$roomVersion")
  implementation("androidx.room:room-runtime:$roomVersion")
  implementation("androidx.room:room-ktx:$roomVersion")
  annotationProcessor("androidx.room:room-compiler:$roomVersion")
  ksp("androidx.room:room-compiler:$roomVersion")

  implementation("androidx.core:core-ktx:1.12.0")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
  implementation("androidx.activity:activity-compose:1.8.2")
  implementation("androidx.hilt:hilt-navigation-compose:$hiltNavigationComposeVersion")
  implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycleVersion")
  implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")

  implementation(platform("androidx.compose:compose-bom:2023.10.01"))
  implementation("androidx.compose.ui:ui")
  implementation("androidx.compose.ui:ui-graphics")
  implementation("androidx.compose.ui:ui-tooling-preview")
  implementation("androidx.compose.material3:material3")

  testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.5")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

  implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
  implementation("com.squareup.retrofit2:converter-moshi:$retrofitVersion")
  implementation("com.squareup.okhttp3:logging-interceptor:$okhttpVersion")
  implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
  implementation("androidx.paging:paging-runtime-ktx:$pagingVersion")
  implementation("androidx.paging:paging-compose:$pagingVersion")

  androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.01"))
  androidTestImplementation("androidx.compose.ui:ui-test-junit4")
  debugImplementation("androidx.compose.ui:ui-tooling")
  debugImplementation("androidx.compose.ui:ui-test-manifest")
}