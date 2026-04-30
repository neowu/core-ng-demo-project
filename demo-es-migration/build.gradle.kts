plugins {
    project
    lint
    app
}

dependencies {
    implementation("core.framework:core-ng")
    implementation("core.framework:core-ng-search")
}

tasks.register("esMigrate") {
    dependsOn("run")
}
