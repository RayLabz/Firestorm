# Firestorm

_An object-oriented data access API for Google's Firestore._

<p style="text-align: justify">
Firestorm is an object-oriented data access API for Google's Firestore. It enables developers to create 
applications that utilize Firestore's capabilities by interacting with it in an object-oriented way. Firestorm uses
 standardized functions and complements the full flexibility of Google's Firestore API. It organizes classes as Firestore
  collections and objects of these classes as documents in these collections. Its aims are to reduce code, 
  improve its readability, and support rapid application development.
</p>

<p>
 Firestorm is currently available as a <b>Java</b> library, with plans to expand to Flutter and Dart.
</p>

<p>
 The current <b>stable version is 1.4.0</b>, which supports only Firestore operations. The latest <b>experimental version
(2.2.0-alpha)</b> is based on a new API and also supports Real-time database. 
</p>

## Guide

View the guide to learn how Firestorm works and how to use it:

[View the guide](API%20Guide.md)

## Change log

[View the change log](CHANGELOG.md)

## Download and import

You can import Firestorm using Maven, Gradle or by downloading it as a .jar file:

### Maven

Stable (Firestore only):

```xml
<dependency>
  <groupId>com.raylabz</groupId>
  <artifactId>firestorm</artifactId>
  <version>1.4.0</version>
</dependency>
```

Experimental (Firestore + Real-time database):

```xml
<dependency>
  <groupId>com.raylabz</groupId>
  <artifactId>firestorm</artifactId>
  <version>2.0.0-alpha</version>
</dependency>
```

### Gradle

Stable (Firestore only):
```xml
implementation 'com.raylabz:firestorm:1.4.0'
```

Experimental (Firestore + Real-time database):
```xml
implementation 'com.raylabz:firestorm:2.2.0-alpha'
```

### Download as .jar file

Stable (Firestore only):

[Download here (1.4.0)](https://repo1.maven.org/maven2/com/raylabz/firestorm/1.4.0/firestorm-1.4.0.jar)

Experimental (Firestore + Real-time database):

[Download here (2.0.0-alpha)](https://repo1.maven.org/maven2/com/raylabz/firestorm/2.0.0-alpha/firestorm-2.0.0-alpha.jar)

## Documentation

[View the documentation](https://raylabz.github.io/com.raylabz.firestorm.Firestorm/javadoc)

## License
Firestorm free to use and is available under the [MIT license](https://github.com/RayLabz/com.raylabz.firestorm.Firestorm/blob/master/LICENSE).

## Contributing to Firestorm

If you are interested in collaborating to improve Firestorm, [send us a message](mailto:raylabzg@gmail.com).