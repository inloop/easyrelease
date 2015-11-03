easyrelease
===========

Gradle plugin for Android projects that helps with signing release APKs.

It does following in `Project.afterEvaluate()`:

* reads `versionName` and `versionCode` from build.gradle and if not defined there, then it's taken from AndroidManifest.xml
* sets each build variant's output file name to *project_name-${variant}-${versionName}-${versionCode}.apk* Custom filename template can be specified
* sets following release buildType properties:
  * `zipAlignEnabled true`
* updates release signingConfig with proper certificate path, keystore password, key alias and key password defined in property file

### Status

See the [CHANGELOG](CHANGELOG.md). Plugin is in early stages. It is based on what we used in our applications and works well for us, but we are sure there are other use-cases or ways it can be improved. It is already available from Maven Central Repository.

### Usage

Add to __your_app/build.gradle:__
```
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath 'eu.inloop:easyrelease:0.2.3'
    }
}
apply plugin: 'easyrelease'
```

Create __your_app/easyrelease.properties:__
```
KEYSTORE_FILE=path/to/your.keystore
KEYSTORE_PASSWORD=your_keystore_password
KEY_ALIAS=your_key_alias
KEY_PASSWORD=your_key_password
```

It is recommended to add __easyrelease.properties__ to .gitignore (or similar for your VCS).

### Custom apk names

Custom filename pattern can be specified by setting the following key in __your_app/easyrelease.properties:__
```
APK_NAME=your_filename_with_${parameter}_variables
```

An example pattern for the default output file name:
```
APK_NAME=${projectName}-${variantName}-${variantVersionCode}-${variantVersionName}
```

The list of all currently supported parameters is:
```
projectName
variantName
variantVersionName
variantVersionCode
dateYear
dateMonth
dateDay
dateHour (24-hour format)
dateMinute
dateSecond
dateMillisecond
headShortHash
headAuthorName
headAuthorEmail
headCommiterName
headCommiterEmail
branchName
```
