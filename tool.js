var System = java.lang.System;
var DocumentBuilderFactory = javax.xml.parsers.DocumentBuilderFactory;
var File = java.io.File;

var factory = DocumentBuilderFactory.newInstance();
var pom = new File("pom.xml");
var builder = factory.newDocumentBuilder();
var doc = builder.parse(pom);

var root = doc.getDocumentElement();
var version = root.getElementsByTagName("version");
var name = root.getElementsByTagName("name");

version = version.item(0).getFirstChild().getNodeValue();
name = name.item(0).getFirstChild().getNodeValue();

if(arguments.length) {
	var command = arguments[0];
	if(command == "name") {
		System.out.println(name);
	}
	if(command == "version") {
		System.out.println(version);
	}
} else {
	// We don't have arguments, let's just print the version then
	System.out.println(version);
}
