plugins {
    project
    lint
    app
}

dependencies {
    implementation(libs.coreng)

    implementation("com.google.adk:google-adk:1.2.0")

    testImplementation(libs.coreng.test)
}
