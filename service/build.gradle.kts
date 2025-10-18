plugins {
    id("java")
}

group = "org.takeyourdata.service"
version = project.ext.get("version")!!

dependencies {
    implementation(project(":protocol"))

    implementation("redis.clients:jedis:7.0.0")
    implementation("org.postgresql:postgresql:42.7.8")
    implementation("io.github.jopenlibs:vault-java-driver:6.2.0")
    implementation("org.bouncycastle:bcprov-jdk18on:1.82")
}