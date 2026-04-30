plugins {
    java
}

sourceSets {
    create("dev") {
        java.srcDir("src/dev/java")
        compileClasspath += sourceSets["main"].runtimeClasspath
        runtimeClasspath += sourceSets["main"].runtimeClasspath
    }
}
