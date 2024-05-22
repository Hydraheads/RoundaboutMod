plugins {
    alias(libs.plugins.minotaur) apply false
    alias(libs.plugins.curseforgegradle) apply false

    // Required for NeoGradle, it's not in this template but it's here if you want to add it.
    alias(libs.plugins.ideaext)
}

subprojects {

    repositories {
        maven("https://maven.blamejared.com") { name = "BlameJared Maven" }
    }

}