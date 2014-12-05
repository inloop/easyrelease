package eu.inloop.easyrelease.plugin

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.tasks.TaskContainer

class EasyReleasePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        // Setup properties for our customization, otherwise zipalign will not start
        
        project.android {
            signingConfigs {
                release {
                    storeFile = project.file('.')
                    keyAlias = ''
                    storePassword = ''
                    keyPassword = ''
                }
            }
        }

        project.android {
            buildTypes {
                release {
                    debuggable false
                    zipAlignEnabled true
                    signingConfig project.android.signingConfigs.release
                }
            }
        }

        // change the apk file name and load signing properties
            
        project.afterEvaluate {
            Util.setApkName(project)
            Util.loadProperties(project)
        }

    }

}

