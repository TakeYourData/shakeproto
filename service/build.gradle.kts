plugins {
    id("java")
}

group = "org.takeyourdata.service"
version = project.ext.get("version")!!

dependencies {
    implementation(project(":protocol"))

    implementation("redis.clients:jedis:7.0.0")
    implementation("org.postgresql:postgresql:42.7.8")
    implementation("org.springframework.vault:spring-vault-core:4.0.0-M3")
    implementation("org.bouncycastle:bcprov-jdk18on:1.82")
}