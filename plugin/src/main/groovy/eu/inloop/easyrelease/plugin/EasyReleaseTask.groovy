package eu.inloop.easyrelease.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class EasyReleaseTask extends DefaultTask {

    static final String TAG = '[easyrelease]'

    @TaskAction
    def sign() {
        setupSigningConfig()
        setupBuildTypes()
    }
    
    void setupBuildTypes() {
        def buildType = project.android.buildTypes.release
        buildType.debuggable = false
        buildType.zipAlign = true
        buildType.signingConfig = project.android.signingConfigs.release
        project.android.applicationVariants.all { variant ->
            def file = variant.outputFile
            def versionName = getVersionName()
            def fileName = "${project.name}-release-${versionName}.apk"
            variant.outputFile = new File(file.parent, fileName) 
        }
    }
    
    void setupSigningConfig() {
        def projectDir = project.projectDir
        def propFileName = 'easyrelease.properties'
        def propFile = new File("$projectDir/$propFileName")
        println "$TAG Reading signing info from $projectDir/$propFileName"
        if (propFile.canRead()) {
            def props = new Properties()
            props.load(new FileInputStream(propFile))
                
            def buildType = project.android.signingConfigs.release
            buildType.storeFile = project.file(props["KEYSTORE_FILE"])
            buildType.storePassword = props["KEYSTORE_PASSWORD"]
            buildType.keyAlias = props["KEY_ALIAS"]
            buildType.keyPassword = props["KEY_PASSWORD"]
        } else {
            System.err.println "$TAG ERROR: Missing $projectDir/$propFileName"
        }
    }
    
    def getVersionName() {
        def androidManifestPath = android.sourceSets.main.manifest.srcFile
        def manifestText = file(androidManifestPath).getText()
        def patternVersionNumber = java.util.regex.Pattern.compile("versionName=\"(\\d+)\\.(\\d+)\\.(\\d+)\"")
        def matcherVersionNumber = patternVersionNumber.matcher(manifestText)
        matcherVersionNumber.find()
        def majorVersion = Integer.parseInt(matcherVersionNumber.group(1))
        def minorVersion = Integer.parseInt(matcherVersionNumber.group(2))
        def pointVersion = Integer.parseInt(matcherVersionNumber.group(3))
        def versionName = majorVersion + "." + minorVersion + "." + pointVersion
        return versionName
    }
    
}