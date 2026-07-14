plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

dependencies {
    implementation(project(":platform:platform-domain"))
    implementation(project(":platform:platform-application"))
    implementation(project(":platform:platform-api"))
    implementation(project(":platform:platform-security"))
    implementation(project(":platform:platform-persistence"))
    implementation(project(":platform:platform-webhooks"))
    implementation(project(":platform:platform-observability"))
    implementation(project(":providers:provider-sdk"))
    implementation(project(":providers:mock-bank:mock-bank-connector"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(libs.testcontainers.junit)
    testImplementation(libs.testcontainers.postgresql)
    // Full-journey test launches the simulator as a second process-local app
    testImplementation(project(":providers:mock-bank:mock-bank-simulator"))
}
