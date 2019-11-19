# Validations
This repository contains a module bpmn2choreography-validator. 
The bpmn2choreography-validator is a java library that validates a BPMN2 Choreography diagram with respect to the BPMN2 validation rules.

## Requirements

* [Apache Maven 3.3.3+](https://maven.apache.org/install.html)
* [Java 8+](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

### Building

To build the project and generate the bundle use the follwing Maven command

    mvn clean package

If everything checks out, the jar should be available in the `bpmn2choreography-validator/target` folder

## Usage
Inside the pom.xml file add the following dependency
```
<dependencies>
	<dependency>
		<groupId>eu.chorevolution.validations</groupId>
		<artifactId>bpmn2choreography-validator</artifactId>
		<version>2.2.0</version>
	</dependency>
</dependencies>
```
