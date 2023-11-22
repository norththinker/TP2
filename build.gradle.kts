val pkg = "org.example"
val main = "Main"

plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin") version "0.0.14"
}

group = pkg
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks {
    javadoc {
        options.encoding = "UTF-8"
    }
    compileJava {
        options.encoding = "UTF-8"
    }
    compileTestJava {
        options.encoding = "UTF-8"
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

javafx {
    version = "20"
    modules = listOf("javafx.controls", "javafx.media")
}

application {
    mainClass.set("${pkg}.${main}")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}