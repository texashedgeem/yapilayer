plugins {
    `java-library`
}

dependencies {
    api(project(":platform:platform-domain"))
    implementation(project(":platform:platform-application"))
    implementation(libs.jackson.databind)
    implementation(libs.slf4j.api)
}
