plugins {
    project
    // id("me.champeau.jmh") version "0.7.3"
}


dependencies {
    // jmh("core.framework:core-ng:${coreNGVersion}")

    implementation("org.openjdk.jmh:jmh-generator-annprocess:1.37")
    implementation("core.framework:core-ng")
    implementation("io.undertow:undertow-core:2.3.23.Final")
    implementation("tools.jackson.module:jackson-module-afterburner:3.1.2")
    implementation("tools.jackson.module:jackson-module-blackbird:3.1.2")
}
