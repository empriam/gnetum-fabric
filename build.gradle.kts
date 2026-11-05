plugins {
    id("fabric-loom") version "1.5-SNAPSHOT"
}

val modId: String = project.property("mod_id") as String
val modVersion: String by project
val mcVersion: String by project
val loaderVersion: String by project
val fabricApiVersion: String by project
val modMenuVersion: String by project

group = project.property("mod_group_id") as String
version = "$modVersion+$mcVersion-fabric"

base {
    archivesName.set(modId)
}

repositories {
    maven("https://maven.terraformersmc.com/releases/") {
        name = "TerraformersMC"
        content {
            includeGroup("com.terraformersmc")
        }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:$mcVersion")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")
    modCompileOnly("com.terraformersmc:modmenu:$modMenuVersion")

    implementation("io.github.llamalad7:mixinextras-fabric:0.5.4")
    annotationProcessor("io.github.llamalad7:mixinextras-common:0.5.4")
    include("io.github.llamalad7:mixinextras-fabric:0.5.4")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.processResources {
    inputs.property("version", project.version)
    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_$modId" }
    }
}
