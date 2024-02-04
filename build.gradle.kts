plugins {
    alias(libs.plugins.jagr)
    alias(libs.plugins.algomate)
    alias(libs.plugins.style)
    alias(libs.plugins.javafxplugin)
}

version = file("version").readLines().first()

exercise {
    assignmentId.set("h13")
}

submission {
    // ACHTUNG!
    // Setzen Sie im folgenden Bereich Ihre TU-ID (NICHT Ihre Matrikelnummer!), Ihren Nachnamen und Ihren Vornamen
    // in Anführungszeichen (z.B. "ab12cdef" für Ihre TU-ID) ein!
    studentId = "ab12cdef"
    firstName = "sol_first"
    lastName = "sol_last"

    // Optionally require own tests for mainBuildSubmission task. Default is false
    requireTests = false
}

configurations.all {
    resolutionStrategy {
        configurations.all {
            resolutionStrategy {
                force(
                    libs.algoutils.student,
                    libs.algoutils.tutor,
                    libs.junit.pioneer,
                )
            }
        }
    }
}

javafx {
    version = libs.versions.javafx.get()
    modules("javafx.controls", "javafx.graphics", "javafx.base", "javafx.swing")
}

jagr {
    graders {
        val graderPublic by getting {
            graderName.set("H13-Public")
            rubricProviderName.set("h13.H13_RubricProviderPublic")
            configureDependencies {
                implementation(libs.bundles.testfx)
            }
            config.set(
                org.sourcegrade.jagr.launcher.env.Config(
                    executor = org.sourcegrade.jagr.launcher.env.Executor(
                        jvmArgs = listOf(
                            "-Dtestfx.robot=glass",
                            "-Dprism.order=sw",
                            "-Dprism.lcdtext=false",
                            "-Dprism.subpixeltext=false",
                            "-Dglass.win.uiScale=100%",
                            "-Dprism.text=t2k"
                        )
                    )
                )
            )
        }
        val graderPrivate by creating {
            parent(graderPublic)
            graderName.set("H13-Private")
            rubricProviderName.set("h13.H13_RubricProviderPrivate")
        }
    }
}
