plugins {
    java
}

apply(plugin = "project")

subprojects {
    group = "core.demo"
    version = "1.0.0"

    repositories {
        maven {
            url = uri("https://neowu.github.io/maven-repo/")
            content {
                includeGroupByRegex("core\\.framework.*")
            }
        }
    }

    if (childProjects.isEmpty()) {
        sourceSets {
            create("dev") {
                java.srcDir("src/dev/java")
                compileClasspath += sourceSets["main"].runtimeClasspath
                runtimeClasspath += sourceSets["main"].runtimeClasspath
            }
        }
    }
}

val coreNGVersion = "9.0.5"
val hsqlVersion = "2.7.2"
val jacksonVersion = "2.16.1"

configure(subprojects.filter { it.name.endsWith("-db-migration") }) {
    apply(plugin = "db-migration")

    dependencies {
        runtimeOnly("com.mysql:mysql-connector-j:8.3.0")
        runtimeOnly("org.postgresql:postgresql:42.7.1")
    }
}

configure(subprojects.filter { it.name.endsWith("-es-migration") }) {
    apply(plugin = "app")
    dependencies {
        implementation("core.framework:core-ng:${coreNGVersion}")
        implementation("core.framework:core-ng-search:${coreNGVersion}")
    }
    tasks.register("esMigrate") {
        dependsOn("run")
    }
}

configure(subprojects.filter { it.name.endsWith("-mongo-migration") }) {
    apply(plugin = "app")
    dependencies {
        implementation("core.framework:core-ng:${coreNGVersion}")
        implementation("core.framework:core-ng-mongo:${coreNGVersion}")
    }
    tasks.register("mongoMigrate") {
        dependsOn("run")
    }
}

configure(subprojects.filter { it.name.endsWith("-service-interface") }) {
    dependencies {
        implementation("core.framework:core-ng-api:${coreNGVersion}")
    }
}

configure(listOf(project(":demo-service"), project(":demo-site"))) {
    apply(plugin = "app")
    dependencies {
        implementation("core.framework:core-ng:${coreNGVersion}")
        testImplementation("core.framework:core-ng-test:${coreNGVersion}")
    }
}

project(":demo-service") {
    dependencies {
        implementation(project(":demo-service-interface"))
        implementation("core.framework:core-ng-mongo:${coreNGVersion}")
        implementation("core.framework:core-ng-search:${coreNGVersion}")
        implementation("com.squareup.okhttp3:okhttp-sse:4.11.0")
        testImplementation("core.framework:core-ng-mongo-test:${coreNGVersion}")
        testImplementation("core.framework:core-ng-search-test:${coreNGVersion}")
        runtimeOnly("core.framework.mysql:mysql-connector-j:8.3.0")
        runtimeOnly("org.postgresql:postgresql:42.7.1")
        testRuntimeOnly("org.hsqldb:hsqldb:${hsqlVersion}")
    }
}

project(":demo-site") {
}

project(":benchmark") {
    dependencies {
        implementation("org.openjdk.jmh:jmh-generator-annprocess:1.27")
        implementation("core.framework:core-ng:${coreNGVersion}")
        implementation("io.undertow:undertow-core:2.3.10.Final")
        implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${jacksonVersion}")
        implementation("com.fasterxml.jackson.module:jackson-module-afterburner:${jacksonVersion}")
        implementation("com.fasterxml.jackson.module:jackson-module-blackbird:${jacksonVersion}")
    }
}

project(":kibana-generator") {
    dependencies {
        implementation("com.fasterxml.jackson.core:jackson-databind:${jacksonVersion}")
    }
}
