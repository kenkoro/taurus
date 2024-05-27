import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

val daggerHilt: String by project
val roomDb: String by project
val paging3: String by project
val hiltNavigationCompose: String by project
val lifecycle: String by project
val truth: String by project
val ktor: String by project
val splashScreen: String by project
val materialIconsExtended: String by project

plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("com.google.devtools.ksp")
  id("com.google.dagger.hilt.android")
  id("org.jlleitschuh.gradle.ktlint")
  id("org.jetbrains.kotlin.plugin.serialization")
}

subprojects {
  apply(plugin = "org.jlleitschuh.gradle.ktlint")

  configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    android.set(true)
    verbose.set(true)
    reporters {
      reporter(ReporterType.PLAIN)
      reporter(ReporterType.CHECKSTYLE)
      reporter(ReporterType.JSON)
    }
  }
}

tasks["check"].finalizedBy(tasks["ktlintCheck"])

android {
  namespace = "com.kenkoro.taurus.client"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.kenkoro.taurus.client"
    minSdk = 24
    targetSdk = 34
    versionCode = 1
    versionName = "0.1.0-mvp"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary = true
    }

    javaCompileOptions {
      annotationProcessorOptions {
        arguments["room.schemaLocation"] = "$projectDir/schemas"
      }
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("debug")
      isDebuggable = false
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
  implementation("com.google.dagger:dagger-android:$daggerHilt")
  implementation("com.google.dagger:hilt-android:$daggerHilt")
  implementation("io.ktor:ktor-client-cio:$ktor")
  implementation("io.ktor:ktor-client-logging:$ktor")
  ksp("com.google.dagger:hilt-android-compiler:$daggerHilt")
  kspAndroidTest("com.google.dagger:hilt-android-compiler:$daggerHilt")

  implementation("androidx.room:room-paging:$roomDb")
  implementation("androidx.room:room-runtime:$roomDb")
  implementation("androidx.room:room-ktx:$roomDb")
  annotationProcessor("androidx.room:room-compiler:$roomDb")
  ksp("androidx.room:room-compiler:$roomDb")

  implementation("androidx.core:core-ktx:1.12.0")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
  implementation("androidx.activity:activity-compose:1.8.2")
  implementation("androidx.hilt:hilt-navigation-compose:$hiltNavigationCompose")
  implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycle")
  implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle")
  implementation("androidx.core:core-splashscreen:$splashScreen")

  implementation(platform("androidx.compose:compose-bom:2023.10.01"))
  implementation("androidx.compose.ui:ui")
  implementation("androidx.compose.ui:ui-graphics")
  implementation("androidx.compose.ui:ui-tooling-preview")
  implementation("androidx.compose.material3:material3")

  implementation("io.ktor:ktor-client-core:$ktor")
  implementation("io.ktor:ktor-client-content-negotiation:$ktor")
  implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor")
  implementation("androidx.paging:paging-runtime-ktx:$paging3")
  implementation("androidx.paging:paging-compose:$paging3")
  implementation("androidx.compose.material:material-icons-extended:$materialIconsExtended")

  testImplementation("junit:junit:4.13.2")
  testImplementation("com.google.truth:truth:$truth")
  androidTestImplementation("androidx.test.ext:junit:1.1.5")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

  androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.01"))
  androidTestImplementation("androidx.compose.ui:ui-test-junit4")
  debugImplementation("androidx.compose.ui:ui-tooling")
  debugImplementation("androidx.compose.ui:ui-test-manifest")
}