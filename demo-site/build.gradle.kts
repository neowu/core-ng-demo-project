plugins {
    project
    lint
    app
    frontend
}

project.ext["frontendDir"] = "${rootDir}/demo-site/web"

dependencies {
    implementation(libs.coreng)
    testImplementation(libs.coreng.test)
}
