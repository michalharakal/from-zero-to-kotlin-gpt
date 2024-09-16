plugins {
    kotlin("jvm")
}

group = "de.jugda"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":SKaiNET"))
    // https://mvnrepository.com/artifact/org.nd4j/nd4j-native-platform
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("junit:junit:4.13.2")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}