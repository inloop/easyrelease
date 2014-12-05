## 0.2.2 (2014-12-05)

Features:

  - removes `minifyEnabled` and `shrinkResources` release properties (seems more reasonable to handle by actual client projects, especially for older projects, that didn't have ProGuard configured properly)

## 0.2.1 (2014-12-03)

Features:

  - sets Java 7 source and target compatibility

## 0.2.0 (2014-12-01)

Features:

  - [#1 Do not ignore build script based versionCode and versionName properties](https://github.com/inloop/easyrelease/issues/1)
  - sets `minifyEnabled` and `shrinkResources` release properties to true

Bugfixes:

  - [#2 Not working with Android Studio RC releases](https://github.com/inloop/easyrelease/issues/3)
