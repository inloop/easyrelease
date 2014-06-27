easyrelease
===========

Gradle plugin for Android projects that helps with signing release APKs

To use this plugin add this to your build.gradle:
```
apply plugin: 'easyrelease'
```

and create __easyrelease.properties__ file in you project directory with following content:
```
KEYSTORE_FILE=path/to/your.keystore
KEYSTORE_PASSWORD=your_keystore_password
KEY_ALIAS=your_key_alias
KEY_PASSWORD=your_key_password
```

Then after calling __gradle assembleRelease__ you'll get signed and zipaligned apk named *project_name-release-x.y.z-v.apk* where __x.y.x__ is versionName and __v__ is versionCode taken from AndroidManifest.xml 
