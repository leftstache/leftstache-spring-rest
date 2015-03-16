Automatic Spring Boot rest controller for repositories and services.

Requirements
------------

 * Spring Boot Application
 * Java 8

To Use
------

 * mvn install
 * include dependency:

```xml
	<dependency>
		<groupId>com.leftstache.spring</groupId>
		<artifactId>leftstache-spring-rest</artifactId>
		<version>1.0-SNAPSHOT</version>
	</dependency>
```

 * Implement the `Restful` interface on any exposed repositories
 * Implement the appropriate `*Logic` interfaces on any exposed services
 * Exposed service methods take precidence to the exposed repositories
 
Available Service Interfaces:

 * CreateLogic
 * GetLogic
 * DeleteLogic
 * PatchLogic