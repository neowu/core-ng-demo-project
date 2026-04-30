plugins {
    project
    `db-migration`
}

dependencies {
    runtimeOnly("com.mysql:mysql-connector-j:8.4.0")
    runtimeOnly(libs.postgresql)
}
