plugins {
    id("java-library")
}

group = "io.margit.appender"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    api("org.apache.logging.log4j:log4j-core:2.23.1")
    implementation("org.jline:jline:3.26.2")
    annotationProcessor("org.apache.logging.log4j:log4j-core:2.23.1")
}
