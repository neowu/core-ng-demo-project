plugins {
    project
    lint
    app
    dev
}

dependencies {
    implementation("core.framework:core-ng")
    implementation("core.framework:core-ng-mongo")
    implementation("core.framework:core-ng-search")

    implementation(project(":demo-service-interface"))
    runtimeOnly("core.framework.mysql:mysql-connector-j:8.4.0-r6")
    runtimeOnly("org.postgresql:postgresql:42.7.10")

    testImplementation("core.framework:core-ng-test")
    testImplementation("core.framework:core-ng-mongo-test")
    testImplementation("core.framework:core-ng-search-test")
    testRuntimeOnly("org.hsqldb:hsqldb:2.7.4")
}
