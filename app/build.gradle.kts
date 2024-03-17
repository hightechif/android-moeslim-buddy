@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinKapt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.oppo.moeslimbuddy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.oppo.moeslimbuddy"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("int", "DB_VERSION", "\'${project.property("apps_db_version")}\'")
        buildConfigField("String", "DB_NAME", "\"${project.property("apps_db_name")}\"")
        buildConfigField("String", "DB_PASS", "\"${project.property("apps_db_pass_phrase")}\"")
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    // Specifies one flavor dimension.
    flavorDimensions += "version"
    productFlavors {
        create("development") {
            // Assigns this product flavor to the "version" flavor dimension.
            // If you are using only one dimension, this property is optional,
            // and the plugin automatically assigns all the module's flavors to
            // that dimension.
            dimension = "version"
            applicationIdSuffix = ".demo"
            versionNameSuffix = "-demo"

            buildConfigField(
                "String",
                "BUILD_NUMBER",
                "\"${project.property("apps_staging_build_number")}\""
            )
            buildConfigField(
                "String",
                "BASE_API_URL",
                "\"${project.property("api_base_url_staging")}\""
            )
            buildConfigField("String", "VERSION_NAME", "\"${project.property("apps_version")}\"")
            resValue("string", "app_name", "Moeslim Buddy Dev")
        }
        create("production") {
            dimension = "version"
            applicationIdSuffix = ".full"
            versionNameSuffix = "-full"

            buildConfigField(
                "String",
                "BUILD_NUMBER",
                "\"${project.property("apps_production_build_number")}\""
            )
            buildConfigField(
                "String",
                "BASE_API_URL",
                "\"${project.property("api_base_url_production")}\""
            )
            buildConfigField("String", "VERSION_NAME", "\"${project.property("apps_version")}\"")
            resValue("string", "app_name", "Moeslim Buddy")
        }
    }
}

dependencies {
    /* basic */
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.activity)
    /* location & maps */
    implementation(libs.smartlocation)
    implementation(libs.google.places)
    implementation(libs.google.maps)
    implementation(libs.compass.qibla)
    /* persistence */
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    kapt(libs.room.compiler)
    androidTestImplementation(libs.room.testing)
    /* networking */
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logger)
    /* dependencies injection */
    implementation(libs.koin)
    implementation(libs.koin.nav)
    /* mapper */
    implementation(libs.mapstruct)
    kapt(libs.mapstruct.processor)
    api(libs.mapstruct.kotlin)
    kapt(libs.mapstruct.kotlin.processor)
    /* support lib */
    implementation(libs.timber)
    implementation(libs.gson)
    implementation(libs.splash)
    implementation(libs.webkit)
    /* security */
    implementation(libs.security.crypto)
    implementation(libs.secure.preferences)
    implementation(libs.sqlcipher)
    implementation(libs.sqlite.ktx)
    /* testing */
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}