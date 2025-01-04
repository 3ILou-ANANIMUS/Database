plugins {
    id("java")
}

group = "org.ananimus"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("io.lettuce:lettuce-core:6.2.6.RELEASE")
}

tasks.test {
    useJUnitPlatform()
}