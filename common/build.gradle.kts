plugins {
    id("multiloader-convention")

    alias(libs.plugins.vanillagradle)
}

val mod_id: String by project
version = libs.versions.modversion.get()
val minecraft_version = libs.versions.minecraft.asProvider().get()

base {
    archivesName = "${mod_id}-common-${minecraft_version}"
}

minecraft {
    version(minecraft_version)
    accessWideners(file("src/main/resources/${mod_id}.accesswidener"))
}

dependencies {
    implementation( libs.jsr305 )
    compileOnly( libs.mixin )
    compileOnly( libs.mixinextras.common )
}

publishing {
    publishing {
        publications {
            create<MavenPublication>(mod_id) {
                from(components["java"])
                artifactId = base.archivesName.get()
            }
        }
    }
}

sourceSets.main.get().resources.srcDir(project(":common").file("src/generated/resources"))