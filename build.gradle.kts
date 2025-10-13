plugins {
    id("java")
    id("application")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.takeyourdata"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.google.com/")
    }
}

application {
    mainClass = "org.takeyourdata.Main"
}

dependencies {
    compileOnly("org.jetbrains:annotations:26.0.2-1")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "org.takeyourdata.Main"
    }
}

sourceSets {
    main {
        java {
            srcDir("src/main/java")
        }
    }
}

tasks.shadowJar {
    archiveClassifier.set("all") // Optional: set a classifier for the output JAR
    // relocate("com.example", "com.yourproject.shaded.com.example") // Example relocation
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Test> {
    systemProperty("file.encoding", "UTF-8")
}


