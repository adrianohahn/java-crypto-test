plugins {
    id("hahn.crypto.java-application-conventions")
}

dependencies {
    implementation(project(":utilities"))
}

application {
    mainClass.set("hahn.crypto.aes.decrypt.Main")
}
