import java.util.Base64

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.kotlinsetup"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.kotlinsetup"
        minSdk = 27
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

tasks.register("printGitHubSecret") {
    group = "custom"
    description = "Task para exibir secrets vindas do GitHub Actions."
    doLast {
        // Secret 1: Texto simples
        val secret = System.getenv("MY_GITHUB_SECRET") ?: "A secret 'MY_GITHUB_SECRET' não foi encontrada."
        println("-----------------------------------------")
        println("Valor da Secret (Simples): $secret")
        
        // Secret 2: Base64 (.txt)
        val secretBase64 = System.getenv("MY_GITHUB_SECRET_BASE_64")
        if (secretBase64 != null) {
            try {
                val decodedBytes = Base64.getDecoder().decode(secretBase64)
                val decodedString = String(decodedBytes, Charsets.UTF_8)
                println("Conteúdo do arquivo Base64 decodificado:")
                println(decodedString)
            } catch (e: Exception) {
                println("Erro ao decodificar a secret Base64: ${e.message}")
            }
        } else {
            println("A secret 'MY_GITHUB_SECRET_BASE_64' não foi encontrada.")
        }
        println("-----------------------------------------")
    }
}
