Zutubi Android Ant Tasks
========================

Introduction
------------

The zutubi-android-ant project provides a collection of Ant tasks that
are useful for Android (http://www.android.com/) projects.  These
tasks are intended to complement the Ant support provided by the
Android SDK.

Home Page
---------

The zutubi-android-ant project has home on the web at:

http://zutubi.com/source/projects/zutubi-android-ant/

Check it out for full project documentation.

License
-------

This code is licensed under the Apache License, Version 2.0.  See the
LICENSE file for details.

Quick Start
-----------

To use zutubi-android-ant in your projects, you can:

* Download the latest jar from:
    http://github.com/jsankey/zutubi-android-ant/downloads
  and add it to your source tree.
* Define a namespace on the root element of your build file as follows:
    <project name="MyProject" xmlns:zaa="antlib:com.zutubi.android.ant">
* Nested within this root element, add the following taskdef:
    <taskdef uri="antlib:com.zutubi.android.ant"
             resource="com/zutubi/android/ant/antlib.xml"
             classpath="path/to/zutubi-android-ant.jar"/>
* Reference the tasks in under the zaa: namespace, e.g.:
    <target name="run-lint">
        <zaa:lint>
            <arg line="--check MissingPrefix /src/astrid/">
        </zaa:lint>
    </target>

Available Tasks
---------------

Currently, zutubi-android-ant contains the following tasks:

* addusedpermission    : adds <uses-permission> elements to a manifest
* bumpversion          : bumps the project versionCode (optionally versionName)
* getversion           : captures the versionCode and versionName to properties
* jsonversion          : generates a JSON file usable with cwac-updater
* libproperties        : makes it easy to link library sources in Eclipse
* lint                 : convenience task for running Android lint
* ndkbuild             : convenience task for running native builds
* removeusedpermission : removes <uses-permission> elements from a manifest
* setmanifestattributes: sets attributes on the <manifest> element
* setversion           : sets the project versionCode and/or versionName

Full documentation for the tasks is available online:

http://zutubi.com/source/projects/zutubi-android-ant/documentation/#tasks

More Information
----------------

For more details check out:

* The project home page (includes full documentation):
    http://zutubi.com/source/projects/zutubi-android-ant/
* The example projects in the examples/ subdirectory (requires a built jar).
* The GitHub project page:
    https://github.com/jsankey/zutubi-android-ant/

Building
--------

To build the task jar, use ant in the root directory of this source
tree:

$ ant jar

More ant targets are available, for details run:

$ ant -p

The build uses the Ant Script Library (http://www.exubero.com/asl/)
for those that wish to understand further.

Feedback
--------

If you have any thoughts, questions etc about the tasks, you can
contact me at:

  jason@zutubi.com

All feedback is welcome.
