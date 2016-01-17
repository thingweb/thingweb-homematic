# thingweb-homematic
Simple legacy mapping from thingweb to HomeMatic, requires a CCU2.
To get it to work you need to install a plugin called "XML API" on your CCU2.

For a first steps, look at de.johanneshund.thingweb.homematic.launchers.HomeMaticThingServient
You'll need to adjust the IP address.

## TS;DU (Too short - didn't understand)

This demonstrates a legacy mapping for the WoT model, in the concrete example for a HomeMatic system (popular home sutomation vendor in Germany).
The WoT ("Web of Things") model is an open and vendor-independent application layer for the IoT, discussed in the Web of Things interest group of the W3C.
One of the exploratory implementations is called thingweb, and is a Java-based framework to create applications using the WoT model.
This hobby project demonstrates how you can adapt an existing (legacy) system and adapt it to the current state of WoT discussion.

## How to build and run

Build using ``gradle assemble``, run the class de.thingweb.homematic.launchers.HomeMaticThingServient
The jar provides no main attribute in the manifest (yet).

Preferred way is to use the gradle application plugin.
you can run the application using ``gradle run``.
Use ``gradle assembleDist`` to build a distributable application.
It contains a zip and a tar file with run scripts (under ./bin) and all dependencies.

We'll soon provide a nightly build.

## Open To-dos
[ ] add application plugin to gradle
[ ] unit-tests
[ ] more device types
[ ] package names to current location


