plugins {
    id("java-library")
    id("maven-publish")
}

group = "com.github.phoenixuhc"
version = "1.0"

repositories {
    mavenCentral()
    maven { url = uri("https://libraries.minecraft.net") }
    maven { url = uri("https://hub.spigotmc.org/nexus/content/groups/public/") }
}

dependencies {
    api("com.google.guava:guava:33.4.6-jre")
    api("org.apache.commons:commons-lang3:3.17.0")
    api("org.yaml:snakeyaml:2.4")
    api("com.googlecode.json-simple:json-simple:1.1.1")
    api("org.avaje.ebean:ebean:9.5.1")
    api("net.sf.trove4j:core:3.1.0")

    api("net.md-5:bungeecord-chat:1.8-SNAPSHOT")

    testImplementation("junit:junit:4.13")
    testImplementation("org.hamcrest:hamcrest:3.0")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(23)
    }
}

tasks.withType<JavaCompile> {
    // options.compilerArgs.add("-Xlint:deprecation")
    options.compilerArgs.add("-nowarn")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "io.margit"
            artifactId = "margit-api"
            version = "1.0"
        }
    }
    repositories {
        mavenLocal()
    }
}
