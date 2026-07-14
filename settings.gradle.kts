plugins {
    // Auto-provisions the JDK 21 toolchain if not installed locally
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "yapilayer"

// Platform core — one module per architectural layer (see adr/0001)
include(
    "platform:platform-domain",
    "platform:platform-application",
    "platform:platform-api",
    "platform:platform-security",
    "platform:platform-persistence",
    "platform:platform-webhooks",
    "platform:platform-bootstrap",
    "platform:platform-observability",
)

// Provider plugin framework — one module per connector (see adr/0004)
include(
    "providers:provider-sdk",
    "providers:mock-bank:mock-bank-simulator",
    "providers:mock-bank:mock-bank-connector",
)

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}
