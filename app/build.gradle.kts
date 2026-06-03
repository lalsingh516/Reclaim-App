plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.google.devtools.ksp)
  alias(libs.plugins.roborazzi)
  alias(libs.plugins.secrets)
}

android {
  namespace = "com.example"
  compileSdk { version = release(36) { minorApiLevel = 1 } }

  defaultConfig {
    applicationId = "com.redlionstudio.reclaim"
    minSdk = 24
    targetSdk = 36
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  signingConfigs {
    create("release") {
      val keystorePath = System.getenv("KEYSTORE_PATH") ?: "${rootDir}/my-upload-key.jks"
      storeFile = file(keystorePath)
      storePassword = System.getenv("STORE_PASSWORD")
      keyAlias = "upload"
      keyPassword = System.getenv("KEY_PASSWORD")
    }
    create("debugConfig") {
      storeFile = file("${rootDir}/debug.keystore")
      storePassword = "android"
      keyAlias = "androiddebugkey"
      keyPassword = "android"
    }
  }

  buildTypes {
    release {
      isCrunchPngs = false
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("release")
    }
    debug {
      signingConfig = signingConfigs.getByName("debugConfig")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  buildFeatures {
    compose = true
    buildConfig = true
  }
  testOptions { unitTests { isIncludeAndroidResources = true } }
}

// Configure the Secrets Gradle Plugin to use .env and .env.example files
// to match the convention used in Web projects.
secrets {
  propertiesFileName = ".env"
  defaultPropertiesFileName = ".env.example"
}

// Some unused dependencies are commented out below instead of being removed.
// This makes it easy to add them back in the future if needed.
dependencies {
  implementation(platform(libs.androidx.compose.bom))
  implementation(platform(libs.firebase.bom))
  implementation("com.google.firebase:firebase-auth")
  implementation("com.google.firebase:firebase-firestore")
  // implementation(libs.accompanist.permissions)
  implementation(libs.androidx.activity.compose)
  // implementation(libs.androidx.camera.camera2)
  // implementation(libs.androidx.camera.core)
  // implementation(libs.androidx.camera.lifecycle)
  // implementation(libs.androidx.camera.view)
  implementation(libs.androidx.compose.material.icons.core)
  implementation(libs.androidx.compose.material.icons.extended)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.datastore.preferences)
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.room.ktx)
  implementation(libs.androidx.room.runtime)
  implementation(libs.coil.compose)
  implementation(libs.converter.moshi)
  // implementation(libs.firebase.ai)
  implementation(libs.kotlinx.coroutines.android)
  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.logging.interceptor)
  implementation(libs.moshi.kotlin)
  implementation(libs.okhttp)
  // implementation(libs.play.services.location)
  implementation(libs.retrofit)
  testImplementation(libs.androidx.compose.ui.test.junit4)
  testImplementation(libs.androidx.core)
  testImplementation(libs.androidx.junit)
  testImplementation(libs.junit)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.robolectric)
  testImplementation(libs.roborazzi)
  testImplementation(libs.roborazzi.compose)
  testImplementation(libs.roborazzi.junit.rule)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.runner)
  debugImplementation(libs.androidx.compose.ui.test.manifest)
  debugImplementation(libs.androidx.compose.ui.tooling)
  "ksp"(libs.androidx.room.compiler)
  "ksp"(libs.moshi.kotlin.codegen)
}

tasks.register("copyApkToApkDevelopment") {
  // Capture values at configuration time
  val buildDirFile = layout.buildDirectory.get().asFile
  val apkFile = File(buildDirFile, "outputs/apk/debug/app-debug.apk")
  val rootDirFile = project.rootDir
  
  val destDirDev = File(rootDirFile, "apk development")
  val destFileDev = File(destDirDev, "reclaim-app-debug.apk")
  
  val destDirBuild = File(rootDirFile, ".build-outputs")
  val destFileBuild = File(destDirBuild, "app-debug.apk")
  
  val destDirDownload = File(rootDirFile, "APK_DOWNLOAD")
  val destFileDownload = File(destDirDownload, "app-debug.apk")

  // Declare inputs/outputs so Gradle knows task relationships
  inputs.file(apkFile)
  outputs.files(destFileDev, destFileBuild, destFileDownload)

  doLast {
    listOf(
      destDirDev to destFileDev,
      destDirBuild to destFileBuild,
      destDirDownload to destFileDownload
    ).forEach { (dir, file) ->
      if (!dir.exists()) {
        dir.mkdirs()
      }
      if (apkFile.exists()) {
        val sizeBytes = apkFile.length()
        println("Source APK size: $sizeBytes bytes")
        if (sizeBytes > 1024 * 1024) {
          apkFile.copyTo(file, overwrite = true)
          println("Successfully copied full APK to ${file.absolutePath} ($sizeBytes bytes)")
        } else if (sizeBytes > 0) {
          apkFile.copyTo(file, overwrite = true)
          println("WARNING: Copied APK, but size is less than 1MB: $sizeBytes bytes")
        } else {
          println("WARNING: Source APK file exists but is 0 bytes.")
        }
      } else {
        println("ERROR: Source APK file does not exist at ${apkFile.absolutePath}")
      }
    }
  }
}

project.afterEvaluate {
  tasks.findByName("assembleDebug")?.finalizedBy("copyApkToApkDevelopment")
  tasks.findByName("copyApkToApkDevelopment")?.finalizedBy("verifyApkSizes")
}

tasks.register("verifyApkSizes") {
  val rootDirFile = project.rootDir
  doLast {
    listOf(
      File(rootDirFile, "apk development/reclaim-app-debug.apk"),
      File(rootDirFile, ".build-outputs/app-debug.apk"),
      File(rootDirFile, "APK_DOWNLOAD/app-debug.apk")
    ).forEach { file ->
      if (file.exists()) {
        val len = file.length()
        println("VERIFICATION: ${file.path} size is: $len bytes (${len / (1024 * 1024.0)} MB)")
      } else {
        println("VERIFICATION FAILED: ${file.path} does not exist!")
      }
    }
  }
}

