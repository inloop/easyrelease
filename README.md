easyrelease
===========

Gradle plugin for Android projects that helps with signing release APKs.

It does following in `Project.afterEvaluate()`:

* reads `versionName` and `versionCode` from AndroidManifest.xml
* sets each build variant's output file name to *project_name-${variant}-${versionName}-${versionCode}.apk*
* updates release signingConfig with proper certificate path, keystore password, key alias and key password defined in property file

### Status

Plugin is in early stages and we are looking for a feedback (issues, pull requests). It is based on what we used in our applications and works well for us, but we are sure there are other use-cases or ways it can be improved.

### Usage

To use this plugin add this to your build.gradle:
```
apply plugin: 'easyrelease'
```

and create __easyrelease.properties__ file in you project directory next to the __build.gradle__ with following content:
```
KEYSTORE_FILE=path/to/your.keystore
KEYSTORE_PASSWORD=your_keystore_password
KEY_ALIAS=your_key_alias
KEY_PASSWORD=your_key_password
```

It is recommended to add __easyrelease.properties__ to .gitignore (or similar for your VCS).

### Installation

This plugin is not yet available in any public repository, so you'll have to clone it locally and install plugin in local Maven repository: 
```
gradle uploadArchives
```

and then add this to your __build.gradle__:
```
buildscript {
    repositories {
        mavenLocal()
        ...
```
