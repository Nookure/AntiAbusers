dependencies {
    compileOnly(libs.paper)
    compileOnly(libs.worldguard)
    compileOnly(project(":AntiAbusers-API"))
}

tasks.processResources {
    filesMatching("paper-plugin.yml") {
        expand("version" to (parent?.version ?: project.version))
    }
}