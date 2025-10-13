plugins {
    id("java")
}

group = "org.takeyourdata.service"
version = project.ext.get("version")!!

dependencies {
    implementation(project(":protocol"))

    implementation("redis.clients:jedis:7.0.0")
}