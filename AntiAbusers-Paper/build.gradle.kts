dependencies {
    compileOnly(libs.paper)
    compileOnly(libs.worldguard)
    compileOnly(libs.spigot)
    compileOnly(project(":AntiAbusers-API"))
}

tasks.processResources {
    filesMatching("paper-plugin.yml") {
        expand("version" to (parent?.version ?: project.version))
    }
}