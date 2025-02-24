<!-- 
This README describes the package. If you publish this package to pub.dev,
this README's contents appear on the landing page for your package.

For information about how to write a good package README, see the guide for
[writing package pages](https://dart.dev/tools/pub/writing-package-pages). 

For general information about developing packages, see the Dart guide for
[creating packages](https://dart.dev/guides/libraries/create-packages)
and the Flutter guide for
[developing packages and plugins](https://flutter.dev/to/develop-packages). 
-->

# Firestorm for Dart & Flutter

Firestorm is a data access and ORM tool for Firebase's <a href="https://firebase.google.com/docs/firestore">Firestore</a>
and <a href="https://firebase.google.com/docs/database">Real-Time Database</a>. It is designed to enable the rapid 
development of applications using these two datastores by providing a simple and intuitive API for interacting with data.

Firestorm is designed to be used with Dart and Flutter, and is built on top of the official Firebase packages for Dart:

- <a href="https://pub.dev/packages/firebase_core">firebase_core</a>
- <a href="https://pub.dev/packages/firebase_database">firebase_database</a>
- <a href="https://pub.dev/packages/cloud_firestore">cloud_firestore</a>

The primary aim of Firestorm is to _reduce development effort and time_ by providing an easy to use API for interacting with
data in these datastores in an _object-oriented_ way, with _minimal to no overheads_ in terms of performance or flexibility.

## Features

1. An easy-to-use data access API.
2. Object-oriented data access. 
3. Automatic serialization and deserialization of data.
4. Easy access to advanced features such as real-time updates, offline persistence, transactions, batch writes, and more.
5. Support for both Firestore and Real-Time Database.
6. Support for both plain Dart and Flutter projects.

## Getting started

### Installation

Install Firestorm by using the command line:

```bash
dart pub add firestorm
```

-- OR --

By adding it to your `pubspec.yaml` file:

```yaml
dependencies:
  firestorm: ^0.0.1
```

## Using Firestorm

TODO

---

## Information

### Requirements

- Dart 3.2 or higher

### Known issues

No known issues.

> [!CAUTION]
> Firestorm is currently under development and may contain bugs or issues. Please report any issues on our [GitHub repository](https://github.com/RayLabz/Firestorm/issues).

### Bug reports and feature requests

If you find a bug or have a feature request, [please report it on our GitHub repository](https://github.com/RayLabz/Firestorm/issues).

## License

Firestorm is released under the Apache 2.0 License. See the [LICENSE](../LICENSE) file for more information.

