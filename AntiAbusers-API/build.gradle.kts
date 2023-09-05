dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(libs.nookcore)
    compileOnly(libs.paper)
}

tasks.test {
    useJUnitPlatform()
}