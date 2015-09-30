## 0.2.3 (2015-11-03)

Features:

  - updated build tools version to 1.3.0 and altered the folder structure to comply with build tools requirements
  - it is now possible to set a custom output file naming convention using available parameters in the easyrelease.properties

## 0.2.2 (2014-12-05)

Features:

  - removed `minifyEnabled` and `shrinkResources` release properties (seems more reasonable to handle by actual client projects, especially for older projects, that didn't have ProGuard configured properly)

## 0.2.1 (2014-12-03)

Features:

  - sets Java 7 source and target compatibility

## 0.2.0 (2014-12-01)

Features:

  - [#1 Do not ignore build script based versionCode and versionName properties](https://github.com/inloop/easyrelease/issues/1)
  - sets `minifyEnabled` and `shrinkResources` release properties to true

Bugfixes:

  - [#2 Not working with Android Studio RC releases](https://github.com/inloop/easyrelease/issues/3)
