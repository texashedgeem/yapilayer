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

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
