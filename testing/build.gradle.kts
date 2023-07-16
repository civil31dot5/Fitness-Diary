@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.civil31dot5.fitnessdiary.testing"
    compileSdk = 33

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {

    implementation(project(":domain"))
    implementation(project(":data"))

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)

    api(libs.junit)
    api(libs.androidx.test.core)
    api(libs.androidx.junit)
    api(libs.espresso)
    api(libs.androidx.test.runner)
    api(libs.androidx.test.rules)
    api(libs.kotlinx.coroutines.test)
    api(libs.work.testing)
    api(libs.room.testing)
    api(libs.navigation.testing)
    api(libs.hilt.android.testing)
    api(libs.kotlin.test)
    api(libs.turbine)

    implementation(platform(libs.compose.bom))
    api(libs.ui.test.junit4)

}