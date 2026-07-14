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
    api(project(":platform:platform-domain"))
    implementation(project(":platform:platform-application"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}
