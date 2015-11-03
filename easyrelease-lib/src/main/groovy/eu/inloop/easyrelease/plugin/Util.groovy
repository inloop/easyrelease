package eu.inloop.easyrelease.plugin
import groovy.text.SimpleTemplateEngine
import org.ajoberstar.grgit.Grgit

class Util {

    static final String TAG = '[easyrelease]'

    static final String DEFAULT_APK_STRUCTURE = '$projectName-$variantName-$variantVersionName-$variantVersionCode'

    /**
     * Modifies output apk file name for all application variants
     */
    static def setApkName(project) {
        project.android.applicationVariants.all { variant ->

            def template = getNameString(project)
            def binding = getBinding(project, variant)

            def engine = new SimpleTemplateEngine()
            def fileName = engine.createTemplate(template).make(binding).toString() + ".apk"

            try {
                variant.outputs.each { output ->
                    output.outputFile = new File(output.outputFile.parent, fileName)
                    println "$TAG Setting $output.name variant output name to $fileName"
                }
            } catch (all) {
                println "$TAG Exception raised during build: " + all
            }
        }
    }

    static def getNameString(project) {
        def projectDir = project.projectDir
        def propFileName = 'easyrelease.properties'
        def propFile = new File("$projectDir/$propFileName")
        if (propFile.canRead()) {
            def props = new Properties()
            props.load(new FileInputStream(propFile))

            if (props["APK_NAME"] != null) {
                return props["APK_NAME"]
            }
            println "$TAG APK_NAME is null, using the default one "
        }
        return DEFAULT_APK_STRUCTURE
    }

    static def getBinding(project, variant) {
        def now = Calendar.getInstance()
        def binding = [
                projectName : project.name,
                variantName : variant.name,
                // first check version code and name from Gradle build script, then from AndroidManifest.xml
                variantVersionCode : variant.versionCode ? variant.versionCode : getVersionCode(project),
                variantVersionName : variant.versionName ? variant.versionName : getVersionName(project),
                dateYear : now.get(Calendar.YEAR),
                dateMonth : now.get(Calendar.MONTH) + 1,
                dateDay : now.get(Calendar.DAY_OF_MONTH),
                dateHour : now.get(Calendar.HOUR_OF_DAY),
                dateMinute : now.get(Calendar.MINUTE),
                dateSecond : now.get(Calendar.SECOND),
                dateMillisecond : now.get(Calendar.MILLISECOND)
        ]

        try {
            def repo = Grgit.open(project.file('..'))
            // buildNumber based on branch commit count might crash when git path is
            // set incorrectly, leaving out until further research
            // binding.buildNumber = 'git rev-list --count HEAD'.execute().text.trim()
            binding.headShortHash = repo.head().abbreviatedId
            binding.headAuthorName = repo.head().author.getName()
            binding.headAuthorEmail = repo.head().author.getEmail()
            binding.headCommiterName = repo.head().committer.getName()
            binding.headCommiterEmail = repo.head().committer.getEmail()
            def branchFullName = repo.branch.current.name.split("//")
            binding.branchName = branchFullName[branchFullName.length-1]
            binding.each {
                println (it)
            }
            return binding
        } catch (all) {
            println "Exception raised: " + all
        }

        return binding
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