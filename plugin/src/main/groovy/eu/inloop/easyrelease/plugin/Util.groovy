package eu.inloop.easyrelease.plugin

class Util {

    static final String TAG = '[easyrelease]'

    /**
     * Modifies output apk file name for all application variants
     */
    static def setApkName(project) {
        project.android.applicationVariants.all { variant ->
            def versionName = getVersionName(project)
            def versionCode = getVersionCode(project)
            def fileName = "$project.name-$variant.name-$versionName-${versionCode}.apk"
            variant.outputFile = new File(variant.outputFile.parent, fileName)
            println "$TAG Setting $variant.name variant apk name to $fileName"
        }
    }

    /**
     * Parsing AndroidManifest.xml to return versionName
     */
    static def getVersionName(project) {
        def androidManifestPath = project.android.sourceSets.main.manifest.srcFile
        def manifestText = project.file(androidManifestPath).getText()
        def patternVersionNumber = java.util.regex.Pattern.compile("versionName=\"(\\d+)\\.(\\d+)\\.(\\d+)\"")
        def matcherVersionNumber = patternVersionNumber.matcher(manifestText)
        matcherVersionNumber.find()
        def majorVersion = Integer.parseInt(matcherVersionNumber.group(1))
        def minorVersion = Integer.parseInt(matcherVersionNumber.group(2))
        def pointVersion = Integer.parseInt(matcherVersionNumber.group(3))
        def versionName = majorVersion + "." + minorVersion + "." + pointVersion
        return versionName
    }
 
    /**
     * Parsing AndroidManifest.xml to return versionCode
     */
    static def getVersionCode(project) {
        def androidManifestPath = project.android.sourceSets.main.manifest.srcFile
        def manifestText = project.file(androidManifestPath).getText()
        def patternVersionNumber = java.util.regex.Pattern.compile("versionCode=\"(\\d+)\"")
        def matcherVersionNumber = patternVersionNumber.matcher(manifestText)
        matcherVersionNumber.find()
        def version = Integer.parseInt(matcherVersionNumber.group(1))
        return version
    }
 
    /**
     * Loads signing properties from file and sets them in release signingConfig
     */
    static def loadProperties(project) {
        def projectDir = project.projectDir
        def propFileName = 'easyrelease.properties'
        def propFile = new File("$projectDir/$propFileName")
        println "$TAG Reading $projectDir/$propFileName"
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
    
}