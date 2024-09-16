plugins {
    kotlin("jvm")
    id("maven-publish")
}

group = "sk.ai.net"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":SKaiNET"))

    implementation("com.jakewharton.picnic:picnic:0.7.0")


    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("junit:junit:4.13.2")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(8)
}

publishing {
    publications {
        // Create a publication named 'myLibrary'
        create("skainet", MavenPublication::class) {
            // Set the artifact ID
            artifactId = "reflection"

            // Include components from the 'java' plugin
            from(components["java"])
        }
    }

    repositories {
        // Publish to the local Maven repository
        mavenLocal()
    }
}