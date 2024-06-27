plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenCentral()
    maven { url = uri("https://libraries.minecraft.net") }
}

dependencies {
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.google.guava:guava:33.2.1-jre")
    implementation("io.netty:netty-all:4.1.111.Final")
    implementation("org.apache.commons:commons-lang3:3.14.0")
    implementation("commons-io:commons-io:2.16.1")
    implementation("com.mojang:authlib:3.4.40")

    implementation("org.jline:jline:3.26.2")

    compileOnly("org.apache.logging.log4j:log4j-api:2.23.1")
    implementation("org.apache.logging.log4j:log4j-core:2.23.1")
    implementation("org.apache.logging.log4j:log4j-slf4j18-impl:2.18.0") // com.mojang.authlib
    implementation(project(":appender"))
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(22)
    }
}

tasks {
    test {
        useJUnitPlatform()
    }
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-Xlint:deprecation")
}

tasks.jar {
    manifest {
        attributes(mapOf("Manifest-Version" to "1.0", "Main-Class" to "net.minecraft.server.MinecraftServer"))
    }
}

tasks.shadowJar {
    transform(com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer::class.java)
    mergeServiceFiles()
}
