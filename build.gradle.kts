plugins {
    id("java")
}

group = "org.m2k"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
}

dependencies {
    compileOnly("org.bukkit:bukkit:1.8.8-R0.1-SNAPSHOT")
}