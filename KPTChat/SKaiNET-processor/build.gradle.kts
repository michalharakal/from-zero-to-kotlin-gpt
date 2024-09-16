plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()

}

group = "com.example"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.squareup:javapoet:1.12.1")
    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.22-1.0.17")
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}

