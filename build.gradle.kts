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
    version = "17.0.1"
    modules("javafx.controls", "javafx.graphics", "javafx.base", "javafx.swing")
}

jagr {
    graders {
        val graderPublic by getting {
            graderName.set("H13-Public")
            rubricProviderName.set("h13.H13_RubricProvider")
        }
        val graderPrivate by creating {
            parent(graderPublic)
            graderName.set("H13-Private")
        }
    }
}
