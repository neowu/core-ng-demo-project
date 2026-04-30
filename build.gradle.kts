plugins {
    java
}

subprojects {
    group = "core.demo"
    version = "1.0.0"

    repositories {
        maven {
            url = uri("https://neowu.github.io/maven-repo/")
            content {
                includeGroup("core.framework")
                includeGroup("core.framework.mysql")
                includeGroup("core.framework.elasticsearch.module")
            }
        }
    }
}

tasks.named<Wrapper>("wrapper") {
    gradleVersion = "latest"
}
