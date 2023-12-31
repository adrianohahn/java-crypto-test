/*
 * Common build configuration for Java application modules
 */

plugins {
    // Apply the common convention plugin for shared build configuration between library and application projects.
    id("hahn.crypto.java-common-conventions")

    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

tasks.register<Jar>("execJar") {
    dependsOn.addAll(listOf("compileJava", "processResources"))
    archiveClassifier.set("standalone")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest { attributes(mapOf("Main-Class" to application.mainClass)) }
    val sourcesMain = sourceSets.main.get()
    val contents = configurations.runtimeClasspath.get()
        .map { if (it.isDirectory) it else zipTree(it) } +
            sourcesMain.output
    from(contents)
}
