
# AT&T XACML

AT&amp;T's reference implementation of the OASIS XACML 3.0 Standard. The AT&T framework represents the entire XACML 3.0 object set as a collection of Java interfaces and standard implementations of those interfaces.  The AT&T PDP engine is built on top of this framework and represents a complete implementation of a XACML 3.0 PDP, including all of the multi-decision profiles. In addition, the framework also contains an implementation of the OASIS XACML JSON Profile v1.0 WD 14. The PEP API includes annotation functionality, allowing application developers to simply annotate a Java class to provide attributes for a request. The annotation support removes the need for application developers to learn much of the API.

The AT&T framework also includes interfaces and implementations to standardize development of PIP engines that are used by the AT&T PDP implementation, and can be used by other implementations built on top of the AT&T framework.

# Release

3.1 - is the latest official release
* Upgrading dependencies
* Enhanced the annotations to support Content
* Feature to support On Permit Apply Second combining algorithm

master branch is where all current development resides.

# Requirements

* Java JDK 11, you will need to ensure Eclipse has that JDK installed in your development environment.

*  Apache Maven to compile, install and run the software.

# Building the source code

From the directory you downloaded the source to, just type 'mvn clean install'.

# Instantiating a PDP Engine

Simple start is just to create an engine from the factory with all default properties.

```
            PDPEngineFactory factory = PDPEngineFactory.newInstance();
            PDPEngine engine = factory.newEngine();
```

To start with properties that override the default:

```
            File xacmlPropertiesFile = new File("path/to/xacml.properties");
            System.setProperty(XACMLProperties.XACML_PROPERTIES_NAME, xacmlPropertiesFile.getPath());
            Properties properties = new Properties();
            try (InputStream inStream = new FileInputStream(xacmlPropertiesFile)) {
                properties.load(inStream);
            }
            PDPEngineFactory factory = PDPEngineFactory.newInstance();
            PDPEngine engine = factory.newEngine(properties);
```
