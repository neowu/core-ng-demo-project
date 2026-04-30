plugins {
    project
    lint
    app
}

dependencies {
    implementation(libs.coreng)
    implementation(libs.coreng.mongo)
}

tasks.register("mongoMigrate") {
    dependsOn("run")
}
