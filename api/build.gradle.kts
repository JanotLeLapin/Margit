plugins {
    id("java-library")
    id("maven-publish")
}

group = "io.margit.api"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    api("com.google.inject:guice:7.0.0")
    annotationProcessor("com.google.inject:guice:7.0.0")
}

publishing {
    publications {
        create<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/JanotLeLapin/Margit")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.token") as String? ?: System.getenv("PAT_GITHUB")
            }
        }
    }
}
