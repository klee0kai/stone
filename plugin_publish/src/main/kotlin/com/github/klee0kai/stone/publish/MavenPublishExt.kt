package com.github.klee0kai.stone.publish

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.withType


fun PublishingExtension.stoneToMaven(project: Project) {
    publications.withType<MavenPublication>().configureEach {
        version = project.version.toString()

        pom {
            url.set("https://github.com/klee0kai/stone")
            licenses {
                license {
                    name.set("GNU General Public License, Version 3")
                    url.set("https://github.com/klee0kai/stone/blob/dev/LICENCE.md")
                }
            }
            developers {
                developer {
                    id.set("klee0kai")
                    name.set("Andrei Kuzubov")
                    email.set("klee0kai@gmail.com")
                }
            }
        }
    }
}