plugins {
    java
}

repositories {
    mavenCentral()
    maven { url = uri("https://papermc.io/repo/repository/maven-releases/") }
}

dependencies {
    implementation("javax.annotation:javax.annotation-api:1.2")
    implementation("com.google.code.gson:gson:2.3.1")
    implementation("com.google.guava:guava:18.0")
    implementation("org.apache.logging.log4j:log4j-core:2.0.2")
    implementation("io.netty:netty-all:4.0.56.Final")
    implementation("org.apache.commons:commons-lang3:3.3.2")
    implementation("com.mojang:authlib:1.5.25")
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
