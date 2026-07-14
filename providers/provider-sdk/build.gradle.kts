plugins {
    `java-library`
}

dependencies {
    // Domain types appear in port signatures, so they are part of this API
    api(project(":platform:platform-domain"))
}
