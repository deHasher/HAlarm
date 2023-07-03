plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "ru.dehasher.halarm"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://jitpack.io/")
}

dependencies {
    implementation("net.java.dev.jna", "jna", "5.13.0")
    implementation("net.java.dev.jna", "jna-platform", "5.13.0")
    implementation("com.github.umjammer", "jlayer", "1.0.2")
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "ru.dehasher.halarm.HAlarm"
        }
    }
    shadowJar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        archiveFileName.set("HAlarm.jar")
        destinationDirectory.set(file("$rootDir/"))

        dependencies {
            include(dependency("net.java.dev.jna:.*"))
            include(dependency("com.github.umjammer:.*"))
        }
    }

    build {
        dependsOn(shadowJar)
    }
}