plugins {
    `java-library`
}

dependencies {
    implementation(project(":providers:provider-sdk"))
    implementation(libs.jackson.databind)
}
