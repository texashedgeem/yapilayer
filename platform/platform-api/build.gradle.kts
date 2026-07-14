plugins {
    `java-library`
    alias(libs.plugins.spring.dependency.management)
}

dependencyManagement {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}

dependencies {
    implementation(project(":platform:platform-domain"))
    implementation(project(":platform:platform-application"))
    implementation(project(":providers:provider-sdk"))
    implementation("org.springframework.boot:spring-boot-starter-web")
}
