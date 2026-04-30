plugins {
    project
    lint
    app
    dev
}

dependencies {
    implementation(libs.coreng)
    implementation(libs.coreng.mongo)
    implementation(libs.coreng.search)

    implementation(project(":demo-service-interface"))
    runtimeOnly(libs.mysql)
    runtimeOnly(libs.postgresql)

    testImplementation(libs.coreng.test)
    testImplementation(libs.coreng.mongo.test)
    testImplementation(libs.coreng.search.test)
    testRuntimeOnly(libs.hsqldb)
}
