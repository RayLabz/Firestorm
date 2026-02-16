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

<div align="center">
    <h1> Firestorm for Dart & Flutter</h1>
</div>

<p align="center">
    <i style="font-size: large; color: darkslategrey">An object-oriented data access API and ODM for Firebase's Firestore and the Realtime Database</i>
</p>

<p align="center">
   <img src="https://raw.githubusercontent.com/RayLabz/Firestorm/refs/heads/master/firestorm_flutter/media/firestorm-logo.png" alt="Firestorm logo" width="200" height="200"/>
</p>

Firestorm for Flutter is a data access and ODM (Object-Document Mapping) tool for Firebase's <a target="_blank" href="https://firebase.google.com/docs/firestore">Firestore</a>
and <a target="_blank" href="https://firebase.google.com/docs/database">Realtime Database</a>. This is supplemented by a local NoSQL database called Localstore, meant for use for local device storage.
Firestorm is designed to enable the rapid development of cross-platform Flutter applications using these two datastores, by providing a simple object-oriented API for interacting with data with minimal to no overheads.

The primary aim of Firestorm is to _reduce development effort and time_ by providing an easy to use API for interacting with
these datastores in an _object-oriented_ way, and with _minimal to no overheads_ in terms of performance or flexibility.

Developed by <a target="_blank" href="https://github.com/nkasenides">Dr Nicos Kasenides</a> 
at <a target="_blank" href="https://www.uclancyprus.ac.cy/">UCLan Cyprus</a>.

**Not convinced? Check out the [code comparison](code-comparison.md).**

[![pub package](https://img.shields.io/pub/v/firestorm.svg)](https://pub.dev/packages/firestorm)
[![GitHub issues](https://img.shields.io/github/issues/RayLabz/Firestorm.svg)]()
![GitHub stars](https://img.shields.io/github/stars/RayLabz/Firestorm.svg?style=social&label=Star)
![GitHub license](https://img.shields.io/github/license/RayLabz/Firestorm.svg)

---

## Contents
- [Features](#features)
- [Getting started](#getting-started)
  - [Installation](#installation)
- [Getting started with Firestorm for Flutter](#getting-started-with-firestorm-for-flutter)
- [API Guide](#api-guide)
- [Feature support](#feature-support)
- [Platform support](#platform-support)
- [Performance](#performance)
- [Information](#information)
  - [Requirements](#requirements)
  - [Known issues](#known-issues)
  - [Bug reports and feature requests](#bug-reports-and-feature-requests)
- [License](#license)

## Features

1. An easy-to-use data access API.
2. Object-oriented data access with support for custom classes and inheritance.
3. Built-in datastore type safety.
4. Automatic serialization and deserialization of data from/to objects.
5. Easy access to advanced features such as real-time updates, offline persistence, transactions, batch writes, and more.
6. Support for both Firestore and Realtime Database.

## Getting started

### Installation

Install/import Firestorm by running the following command in your project folder terminal:

```bash
flutter pub add firestorm
```

--- OR ---

By adding it to your `pubspec.yaml` file:

```yaml
dependencies:
  firestorm: ^0.7.4
```

## Getting started with Firestorm for Flutter

After importing Firestorm in your project, you can start using it by following these steps:
1. **Configure the `google-services.json` file in your project.**
   - For Android, place it in `android/app/`.
   - For iOS, place it in `ios/Runner/`.
   - For web, place it in `web/`.
   - For desktop, place it in `linux/`, `macos/`, or `windows/` as appropriate.
   - For more information on how to generate this file, refer to the official Firebase documentation for [Android](https://firebase.google.com/docs/android/setup), [iOS](https://firebase.google.com/docs/ios/setup), and [Web](https://firebase.google.com/docs/web/setup).
   - You also need to configure each sub-project to use Firebase with the **correct dependencies**.
2. **Create your custom data classes**
    - <a target="_blank" href="defining-classes.md">Follow the guide</a> on how to define your data classes.
3. **Install and run build_runner to perform checks and generate `firestorm_models.dart`**:
    - To install, run:
        ```bash
        flutter pub add build_runner 
        ```

    - To generate, run:
      ```bash
      dart run build_runner build --delete-conflicting-outputs
      ```
This will generate a file called `firestorm_models.dart` in the `lib/generated/` directory of your project.
This file contains the generated code for your custom classes and is required for Firestorm to work with them.

4. **Initialize Firestorm in your application.**
     ```dart
     import 'package:firebase_core/firebase_core.dart';
     import 'package:firestorm/firestorm.dart';

     void main() async {
       WidgetsFlutterBinding.ensureInitialized(); //Ensures Flutter is initialized before Firebase
       await FS.init(); //Initialize Firestorm to use Firestore
       await RDB.init(); //Initialize Firestorm to use Realtime Database
       registerClasses(); //Registers custom classes. Imported from generated file [firestorm_models.dart].
       
       runApp(MyApp()); //Run your app normally here.
     }
     ```
     
> [!TIP]
> If you are using Firestorm with both Firestore and Realtime Database, you need to initialize both `FS` and `RDB`. If you only need one of them, you can initialize only the one you need.

5. **Import the generated `firestorm_models.dart` file in your code (main.dart)**:
   - This file contains the generated code for your custom classes and is required for Firestorm to work with them.
   - You can import it like this:
     ```dart
     import '<your_project>/generated/firestorm_models.dart';
     ```
6. **Start using Firestorm in your code**
    - You can now start using Firestorm to interact with Firestore or Realtime Database.
    - For example, you can create, read, update, and delete documents in Firestore or Realtime Database using your custom classes.
    - For more information on how to use Firestorm, refer to the [API Guide](api-guide.md).

### Summary
<img src="https://raw.githubusercontent.com/RayLabz/Firestorm/refs/heads/master/firestorm_flutter/media/overview-flow.png" alt="Firestorm overview" style="max-width: 100%;"/>

---

## API Guide
For a detailed guide on how to use Firestorm, refer to the [API Guide](api-guide.md).
The guide covers the following topics:
- How to create, read, update, and delete documents in Firestore and Realtime Database.
- How to use real-time updates with Firestorm.
- How to use transactions and batch writes for Firestore.
- How to use advanced features such as offline persistence, queries, and more.

## Feature support

Even though Firestorm provides a unified API for both Firestore and Realtime Database, it is important to note that the two databases have different capabilities and limitations. Firestorm aims to provide a consistent interface while respecting the unique features of each database.
- **Firestore**: A NoSQL document database that allows for complex queries, offline support, and real-time updates.
- **Realtime Database**: A cloud-hosted NoSQL database that provides real-time synchronization and is optimized for low-latency data access.
- **Localstore**: A local (on-device) NoSQL datastore.

| Feature          | Firestore | Realtime Database | Localstore |
|------------------|---------|-----------------|----------------|
| Basic operations (CRUD) | ✅       | ✅               | ✅ |
| Subcollections    | ✅       | ✅               | ✅ |
| Real-time Listeners | ✅       | ✅               | ❌ |
| Offline Support  | ✅       | ✅               | ✅ | 
| Batch Writes     | ✅       | ❌               | ❌ |
| Queries          | Complex | Basic only      | ❌ |
| Transactions     | ✅       | ❌               | ❌ |
| Pagination       | ✅       | ❌               | ❌ |


## Platform support
| Platform | Firestore | Realtime Database | Localstore |
|----------|-----------|-------------------|------------|
| Android  | ✅         | ✅                 | ✅          |
| iOS      | ✅       | ✅                 | ✅          |
| Web      | ✅       | ✅                 | ✅          | 
| Windows  | ✅       | ❌                 | ✅          |
| macOS    | ✅       | ✅                 | ✅
| Linux    | ❌        | ❌                | ✅ |


## Performance
A primary consideration for Firestorm is efficient and minimal overheads.
It uses the official Firebase packages for Dart, which are optimized for performance. Where
necessary, it utilizes various techniques such as multithreading and multiplexing to ensure that
the API is efficient and does not introduce unnecessary overheads. Firestorm's API is designed to
be easy to use while its operations run at high performance.

You can find the performance benchmarks and comparisons in the [performance](performance.md) section.

## Information


### Requirements

- Dart 3.2 or higher

Firestorm is designed to be used with Dart and Flutter, and is built on top of the official Firebase and Localstore packages for Dart:

- <a href="https://pub.dev/packages/firebase_core">firebase_core</a>
- <a href="https://pub.dev/packages/firebase_database">firebase_database</a>
- <a href="https://pub.dev/packages/cloud_firestore">cloud_firestore</a>
- <a href="https://pub.dev/packages/localstore">localstore</a>

You do not need to add these packages manually, as Firestorm will automatically add them as dependencies when you install it.

### Known issues

No known issues.

> [!CAUTION]
> Firestorm is still an experimental tool and may contain bugs or issues. Please report any issues on our [GitHub repository](https://github.com/RayLabz/Firestorm/issues).

### Bug reports and feature requests

If you find a bug or have a feature request, [please report it on our GitHub repository](https://github.com/RayLabz/Firestorm/issues).

View the [change log](CHANGELOG.md) for the latest updates and changes to Firestorm.

## License

Firestorm is open-source and is released under the Apache 2.0 License. See the [LICENSE](../LICENSE) file for more information.

