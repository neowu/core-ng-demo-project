plugins {
    project
    lint
    app
    frontend
}

project.ext["frontendDir"] = "${rootDir}/demo-site/web"

dependencies {
    implementation("core.framework:core-ng")
    testImplementation("core.framework:core-ng-test")
}
