import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(Deps.gradlePlugin)
        classpath(Deps.Plugins.androidMaven)
        classpath(kotlin(Deps.Kotlin.gradlePlugin, Versions.kotlin))
    }
}

plugins {
    idea
    id(Deps.Plugins.detekt) version Versions.detekt
    id(Deps.Plugins.ktlint) version Versions.ktlint
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
}

subprojects {
    apply(plugin = Deps.Plugins.ktlint)

    ktlint {
        version.set(Versions.ktlintExtension)
        ignoreFailures.set(true)
        android.set(true)
        outputToConsole.set(true)
        reporters.set(setOf(ReporterType.PLAIN, ReporterType.CHECKSTYLE))
    }
}

detekt {
    version = Versions.detekt
    input = files("app/src/main/java", "mvvm/src/main/java", "dagger/src/main/java")
    filters = ".*/resources/.*,.*/build/.*"
    config = files("detekt.yml")
}
