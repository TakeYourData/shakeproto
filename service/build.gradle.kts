plugins {
    id("java")
    kotlin("jvm") version "2.2.20"
}

group = "org.takeyourdata.service"
version = project.ext.get("version")!!

dependencies {
    implementation(project(":protocol"))

    implementation("redis.clients:jedis:7.0.0")
    implementation("org.postgresql:postgresql:42.7.8")
    implementation("io.github.jopenlibs:vault-java-driver:6.2.0")
    implementation("org.bouncycastle:bcprov-jdk18on:1.82")
    implementation("org.jetbrains.exposed:exposed-core:1.0.0-rc-2")
    implementation("org.jetbrains.exposed:exposed-java-time:1.0.0-rc-2")
    implementation("org.jetbrains.exposed:exposed-dao:1.0.0-rc-2")
    implementation("org.jetbrains.exposed:exposed-jdbc:1.0.0-rc-2")
}