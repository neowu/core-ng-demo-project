plugins {
    project
    lint
    app
}

dependencies {
    implementation("core.framework:core-ng")
    implementation("core.framework:core-ng-mongo")
}

tasks.register("mongoMigrate") {
    dependsOn("run")
}
