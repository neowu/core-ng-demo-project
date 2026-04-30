plugins {
    project
    lint
    app
}

dependencies {
    implementation(libs.coreng)
    implementation(libs.coreng.search)
}

tasks.register("esMigrate") {
    dependsOn("run")
}
