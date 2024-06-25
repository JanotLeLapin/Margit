plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenCentral()
    maven { url = uri("https://papermc.io/repo/repository/maven-releases/") }
}

dependencies {
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.google.guava:guava:33.2.1-jre")
    implementation("io.netty:netty-all:4.1.111.Final")
    implementation("org.apache.commons:commons-lang3:3.14.0")
    implementation("com.mojang:authlib:1.5.25")
    implementation("org.apache.logging.log4j:log4j-api:2.23.1")
    implementation("org.apache.logging.log4j:log4j-core:2.23.1")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(8)
    }
}

tasks {
    test {
        useJUnitPlatform()
    }
}

tasks.jar {
    manifest {
        attributes(mapOf("Manifest-Version" to "1.0", "Main-Class" to "net.minecraft.server.MinecraftServer"))
    }
}
