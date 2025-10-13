plugins {
    id("java")
}

group = "org.takeyourdata.protocol"
version = project.ext.get("version")!!

dependencies {
    implementation("org.bouncycastle:bcprov-jdk15to18:1.82")
    implementation("org.msgpack:msgpack-core:0.9.10")
    implementation("com.fasterxml.jackson.core:jackson-core:2.20.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.20.0")
    implementation("org.msgpack:jackson-dataformat-msgpack:0.9.10")
}