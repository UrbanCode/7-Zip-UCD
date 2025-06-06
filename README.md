# IBM UrbanCode Deploy 7-Zip Plugin
---
Note: This is not the plugin distributable! This is the source code. To find the installable plugin, go into the 'Releases' tab, and download a stable version.

### License
This plug-in is protected under the [Eclipse Public 1.0 License](http://www.eclipse.org/legal/epl-v10.html)

### Compatibility
	The IBM UrbanCode Deploy automation plug-in works with 7-Zip v14 and higher
	This plug-in requires version 6.1.1 or later of IBM UrbanCode Deploy.
    
### Installation
	The packaged zip is located in the releases folder. No special steps are required for installation. See Installing plug-ins in UrbanCode Deploy. Download this zip file if you wish to skip the manual build step. Otherwise, download the entire 7-Zip-UCD project and run the `gradlew` command in the top level folder. This should compile the code and create 	a new distributable zip within the `build/distributions` folder. Use this command if you wish to make your own changes to the plugin.

### History
    Version 1
        Community GitHub Release
    Version 2
        Added support for Groovy 3 version

### How to build the plugin from command line:

1. Navigate to the base folder of the project through command line.
2. Make sure that there is build.gradle file there, and then execute 'gradlew' command.
3. The built plugin is located at build/distributions/7-Zip-UCD-vdev.zip
