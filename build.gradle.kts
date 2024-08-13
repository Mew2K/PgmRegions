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
    maven {
        url = uri("https://maven.enginehub.org/repo/")
    }
}

dependencies {
    compileOnly("org.bukkit:bukkit:1.8.8-R0.1-SNAPSHOT")
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:6.1")
}

// This was only added so that I can build the .jar to my localhost
// "plugins" folder in IntelliJ. It can simply be ignored or deleted
// for anyone who is wondering about it.
val localGradleFile = file("local.gradle.kts")
if (localGradleFile.exists()) {
    apply(from = localGradleFile)
}