plugins {
    id("java")
    id("com.modrinth.minotaur") version "2.10.0"
}

group = "fr.korastrip.sharedstats"
version = "1.0.2"

modrinth {
    projectId.set("c9FDN1zY")
    versionNumber.set(project.version.toString())
    versionType.set("release")
    uploadFile.set(tasks.jar)
    changelog.set(providers.environmentVariable("MODRINTH_CHANGELOG").orElse("Release ${project.version}"))

    gameVersions.addAll("26.2")
    loaders.add("paper")
}

repositories {
    mavenCentral()
    maven(url = "https://repo.papermc.io/repository/maven-public/") {
        name = "papermc"
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    compileOnly("io.papermc.paper:paper-api:26.3.build.+")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

tasks.test {
    useJUnitPlatform()
}
