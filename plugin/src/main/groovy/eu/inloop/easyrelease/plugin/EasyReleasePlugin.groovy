package eu.inloop.easyrelease.plugin

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.tasks.TaskContainer

class EasyReleasePlugin implements Plugin<Project> {

    static final String TAG = '[easyrelease]'
        
    @Override
    void apply(Project project) {

        def taskName = 'validateReleaseSigning'
            
        // TODO: I wanted to use this to detect whether $taskName task was found, 
        // but for some reason, whenReady is not called.
        // It is not possible to put that check simply after whenTaskAdded, because it is async
        
        // project.gradle.taskGraph.whenReady { taskGraph ->
        // }
            
        project.tasks.whenTaskAdded { task ->
            if (task.name == taskName) {
                project.task('easyRelease', type: EasyReleaseTask) {
                }
                task.dependsOn project.tasks.getByName('easyRelease')
                println "$TAG Signing task added succesfuly."
            }
        }
        
        
    }

    
}

