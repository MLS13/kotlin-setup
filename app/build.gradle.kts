import java.util.Base64
import java.io.File

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
    description = "Task para converter Base64 em arquivo e ler usando file()."
    doLast {
        // Secret 1: Texto simples
        val secret = System.getenv("MY_GITHUB_SECRET") ?: "A secret 'MY_GITHUB_SECRET' não foi encontrada."
        println("-----------------------------------------")
        println("Valor da Secret (Simples): $secret")
        
        // Secret 2: Base64 -> Arquivo -> Leitura
        val secretBase64 = System.getenv("MY_GITHUB_SECRET_BASE_64")
        if (secretBase64 != null) {
            try {
                // 1. Decodifica o Base64
                val decodedBytes = Base64.getDecoder().decode(secretBase64)
                
                // 2. Cria um arquivo temporário
                val tempFile = File(project.projectDir, "temp_secret.txt")
                tempFile.writeBytes(decodedBytes)
                println("Arquivo temporário criado em: ${tempFile.absolutePath}")

                // 3. Lê o arquivo usando a sintaxe file() do Gradle (que retorna um objeto File)
                // Nota: Em Kotlin DSL, project.file() ou apenas file() resolve o caminho relativo ao projeto
                val fileToRead = file("temp_secret.txt")
                
                if (fileToRead.exists()) {
                    println("Conteúdo lido do arquivo via file():")
                    println(fileToRead.readText(Charsets.UTF_8))
                }

                // Opcional: Remover o arquivo após o teste (comentado para você poder validar se quiser)
                // tempFile.delete()
                
            } catch (e: Exception) {
                println("Erro no processo de arquivo: ${e.message}")
            }
        } else {
            println("A secret 'MY_GITHUB_SECRET_BASE_64' não foi encontrada.")
        }
        println("-----------------------------------------")
    }
}
