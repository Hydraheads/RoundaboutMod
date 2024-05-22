pluginManagement {
    repositories {
        gradlePluginPortal()
        maven {
            name = "Forge"
            url = uri("https://maven.minecraftforge.net/")
            content {
                includeGroupAndSubgroups("net.minecraftforge")
            }
        }
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
            content {
                includeGroupAndSubgroups("net.fabricmc")
                includeGroup("fabric-loom")
            }
        }
        maven {
            name = "Parchment"
            url = uri("https://maven.parchmentmc.org")
            content {
                includeGroupAndSubgroups("org.parchmentmc")
            }
        }
        maven {
            name = "Sponge"
            url = uri("https://repo.spongepowered.org/repository/maven-public/")
            content {
                includeGroupAndSubgroups("org.spongepowered")
            }
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "roundabout_mod"

include("common")
include("fabric")
include("forge")
