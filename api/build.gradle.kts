plugins {
    id("java-library")
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
