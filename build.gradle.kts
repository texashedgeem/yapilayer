// Root build: shared conventions for all Java modules.
// Kept deliberately minimal and readable for contributors — see CONTRIBUTING.md.

plugins {
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependency.management) apply false
}

allprojects {
    group = "io.yapilayer"
    version = "0.1.0-SNAPSHOT"
}

subprojects {
    // Only configure Java modules (directories with src/); container dirs like
    // :platform and :providers are just groupings.
    plugins.withId("java") {
        extensions.configure<JavaPluginExtension> {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(libs.versions.java.get().toInt()))
            }
        }

        dependencies {
            "testImplementation"(platform(libs.junit.bom.get().toString()))
            "testImplementation"(libs.junit.jupiter.get().toString())
            "testImplementation"(libs.assertj.core.get().toString())
            "testRuntimeOnly"(libs.junit.platform.launcher.get().toString())
        }

        tasks.withType<Test>().configureEach {
            useJUnitPlatform()
        }

        // Spring resolves @PathVariable/@RequestParam names reflectively;
        // the Boot plugin adds this flag but plain java-library modules don't.
        tasks.withType<JavaCompile>().configureEach {
            options.compilerArgs.add("-parameters")
        }
    }
}
