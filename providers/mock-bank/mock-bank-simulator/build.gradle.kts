plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    // End-to-end test drives the real connector against this simulator
    testImplementation(project(":providers:mock-bank:mock-bank-connector"))
    testImplementation(project(":providers:provider-sdk"))
}
