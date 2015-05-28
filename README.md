#RemoteAppControl [![Release](https://img.shields.io/github/tag/Netural/RemoteAppControl.svg?label=JitPack%20Maven)](https://jitpack.io/#Netural/RemoteAppControl)
========

A Android library to compare local version number with remote version number and asks users to update

How to add RemoteAppControl
--------

```
repositories { 
  maven { url "https://jitpack.io" }
}

dependencies {
  compile 'com.github.Netural:RemoteAppControl:1.0.0
}
```

How to use RemoteAppControl
--------

1. Create a JSON file according to the specification and upload it somewhere
1. Add jitpack to your repositories and add the dependency as described above
2. Add this code in the onCreate or onResume method in the Activity of your choice:
	
	```
	new RemoteAppControl(this, BuildConfig.VERSION_CODE,
                "https://raw.githubusercontent.com/Netural/RemoteAppControl/develop/text.json",
                this)
                .withStyle(R.style.Theme_AppCompat_Dialog_Alert).check();
	```

3. Start App and enjoy

For more info check out the sample app

JSON specification
--------

Here is an example of a JSON configuration file

```
{
  "android": {
    "minVersion": "1.0.0",
    "minVersionCode": 10,
    "currentVersion": "1.2.0",
    "currentVersionCode": 12,
    "languages": [
      {
        "language": "en",
        "title": "warning",
        "message": "please update your app!",
        "url": "https://play.google.com/store/apps/details?id=com.whatsapp"
      }
      ]
  },
  "iOS": {
    "minVersion": "1.0.0",
    "minVersionCode": 10,
    "currentVersion": "1.2.0",
    "currentVersionCode": 12,
    "languages": [
      {
        "language": "en",
        "title": "warning",
        "message": "please update your app!",
        "url": "https://itunes.apple.com/at/app/whatsapp-messenger/id31063399"
      }
      ]
  }
}
```

The "iOS" part of the file is optional (a library for iOS will follow soon).

What is
- minVersionCode: if the version code of your app is smaller than this one, your user is forced to update (dialog is not dismissable)
- currentVersionCode: if the version code of your app is smaller than this one, your user gets informed about an update but can dismiss the dialog
- languages: you can define multiple languages, if the current system language of your user is not found it will default to "en"
- url: the url where the user can find the update, does not have to be a play store url

Proguard exceptions
--------

TBA

License
=======

    Copyright 2015 Netural GmbH

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.