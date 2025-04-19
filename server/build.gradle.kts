plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenCentral()
    maven { url = uri("https://libraries.minecraft.net") }
    maven { url = uri("https://hub.spigotmc.org/nexus/content/groups/public/") }
}

dependencies {
    // implementation(project(":appender"))
    implementation(project(":api"))

    implementation("io.netty:netty-all:4.1.119.Final")
    implementation("commons-io:commons-io:2.18.0")
    implementation("net.sf.jopt-simple:jopt-simple:5.0.4")
    implementation("org.fusesource.jansi:jansi:2.4.1")
    implementation("org.jline:jline:3.29.0")
    implementation("org.apache.logging.log4j:log4j-api:2.24.3")
    implementation("org.apache.logging.log4j:log4j-core:2.24.3")

    implementation("org.lz4:lz4-java:1.8.0")
    implementation("com.github.luben:zstd-jni:1.5.0-4")

    // implementation("com.mojang:authlib:3.4.40")
    implementation("com.mojang:authlib:1.5.21") {
        exclude(group = "org.apache.logging.log4j")
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(23)
    }
}

tasks {
    test {
        useJUnitPlatform()
    }
}

tasks.withType<JavaCompile> {
    // options.compilerArgs.add("-Xlint:deprecation")
    options.compilerArgs.add("-nowarn")
}

tasks.jar {
    manifest {
        attributes(mapOf("Manifest-Version" to "1.0", "Main-Class" to "org.bukkit.craftbukkit.Main", "Multi-Release" to true))
    }
}

tasks.shadowJar {
    transform(com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer::class.java)
    mergeServiceFiles()
}
