plugins {
    id("multiloader-convention")

    alias(libs.plugins.minotaur)
    alias(libs.plugins.curseforgegradle)
    alias(libs.plugins.loom)
}

val mod_id: String by project
version = libs.versions.modversion.get()
val minecraft_version = libs.versions.minecraft.asProvider().get()
val parchment_minecraft_version = libs.versions.parchment.minecraft.get()
val parchment_version = libs.versions.parchment.asProvider().get()
val fabric_version = libs.versions.fabric.asProvider().get()
val fapi_version = libs.versions.fabric.api.get()

java {
    sourceCompatibility =  JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

base {
    archivesName = "${mod_id}-fabric-${minecraft_version}"
}

repositories {
    maven {
        name = "ParchmentMC"
        url = uri("https://maven.parchmentmc.org")
        content {
            includeGroupAndSubgroups("org.parchmentmc")
        }
    }
}


dependencies {
    minecraft( libs.minecraft )
    implementation( libs.jsr305 )
    mappings(loom.layered() {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${parchment_minecraft_version}:${parchment_version}@zip")
    })
    modImplementation( libs.fabric )
    modImplementation( libs.fabric.api )
    compileOnly( project(":common") )
}

loom {
	accessWidenerPath = project(":common").file("src/main/resources/${mod_id}.accesswidener")
    runs {
        named("client") {
            configName = "Fabric Client"

            client()
            ideConfigGenerated(true)
            runDir("runs/" + name)
            programArg("--username=Dev")
        }

        named("server") {
            configName = "Fabric Server"

            server()
            ideConfigGenerated(true)
            runDir("runs/" + name)
        }
    }
}

tasks.withType<JavaCompile>().configureEach {
    source(project(":common").sourceSets.getByName("main").allSource)
}

tasks.named<Jar>("sourcesJar").configure {
    from(project(":common").sourceSets.getByName("main").allSource)
}

tasks.withType<Javadoc>().configureEach {
    source(project(":common").sourceSets.getByName("main").allJava)
}

tasks.withType<ProcessResources>().configureEach {
   from(project(":common").sourceSets.getByName("main").resources)
}


publishing {
    publications {
        create<MavenPublication>(mod_id) {
            from(components["java"])
            artifactId = base.archivesName.get()
        }
    }
}