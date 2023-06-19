buildscript {
    dependencies {
        classpath(libs.agp)
        classpath(libs.kgp)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.detekt)
    id("org.jetbrains.kotlin.android") version "1.8.20" apply false
}

allprojects {
    group = PUBLISHING_GROUP
}

private val detektFormatting = libs.detekt.formatting

subprojects {
    apply {
        plugin("io.gitlab.arturbosch.detekt")
    }

    detekt {
        config = rootProject.files("config/detekt/detekt.yml")
    }

    dependencies {
        detektPlugins(detektFormatting)
    }
}