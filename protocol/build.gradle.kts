plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.takeyourdata"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("org.bouncycastle:bcprov-jdk15to18:1.82")
    implementation("org.msgpack:msgpack-core:0.9.10")
    implementation("com.fasterxml.jackson.core:jackson-core:2.20.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.20.0")
    implementation("org.msgpack:jackson-dataformat-msgpack:0.9.10")
}

tasks.test {
    useJUnitPlatform()
}

tasks.shadowJar {
    dependencies {
        include(dependency("org.msgpack:.*:.*"))
    }
}