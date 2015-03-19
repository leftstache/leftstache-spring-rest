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

 * Implement the `RestfulService` interface on any Service to be exposed
 * Add methods to service for various operations, annotate with `@RestEndpoint`

 Available Endpoints:

 * CREATE takes an Entity and returns its ID.
 * DELETE takes an ID, returns void.
 * SEARCH takes a `Map<String, String>` (query string parameters) and a `Map<String, Object>` (query map. The request body, or if none, the 'query' query string parameter deserialized as json if present).
 * SEARCH can optional be automatically implemented by implementing `JpaSpecificationExecutor` on the Respository associated with the service.
 * GET takes an ID and returns the Entity. Get is always exposed automatically, and if no method is declared, then the associated repository's `findOne` method is called instead.
 * EDIT takes a `Map<String, Object>` representing the changed fields and returns void. This should do a patch. (This isn't currently implemented)
 * REPLACE takes an ID and an Entity (with its ID populated as well) and returns void. Replacing the object if it already existed. (This isn't currently implemented)


