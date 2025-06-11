// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.2" apply false // Replace with the latest stable version
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false // Replace with the latest stable version
    id("com.google.gms.google-services") version "4.4.1" apply false // Replace with the latest stable version
}

buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.1") // Ensure this matches the plugins version
    }
}