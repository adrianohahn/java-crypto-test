/*
 * aes-cli build
 */

plugins {
    id("hahn.crypto.java-application-conventions")
}

dependencies {
    implementation(project(":utilities"))
    implementation("commons-cli:commons-cli:1.6.0")
}

application {
    // Define the main class for the application.
    mainClass.set("hahn.crypto.aes.cli.Main")
}
