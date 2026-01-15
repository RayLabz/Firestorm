## v0.5.0
Release date: 15/01/2026
- Added support for named parameters in constructors within inheritance hierarchies.

## v0.4.0
Release date: 07/11/2025
- Added support for providing `SetOptions` and `GetOptions` (optional parameters) in various operations for Firestore.

## v0.3.0
Release date: 17/10/2025
- Added support for Localstore, a local database similar to Firebase's databases which can acts as a local cache.

## v0.2.4 - Bug Fixes and Improvements
Release date: ?

- Fixed issue with extension_generator which prevented fields not in the constructor from being serialized/deserialized.

## v0.2.3 - Various updates
Release date: 16/07/2025

- Upgraded `analyzer` to version 7.3.0 to resolve static analysis issues.
- Adjusted README for Flutter only.

## v0.2.2 - Various updates
Release date: 15/07/2025

- Switched to `logger` for debug prints rather than `colorful_text`.
- Downgraded `analyzer` to version 6.2.0 to avoid deprecation issues.
- License update to point to correct file.
- Added code-comparison.md to the documentation directory, linked to the README.
- Improved examples.

## v0.2.1 - Bug Fixes
Release date: 07/07/2025**

- Fixed an issue with listener data deserialization causing runtime errors.

## v0.2.0 - Named parameter support
Release date: 05/07/2025

- Added support for named parameters in custom data classes.
- Fixed instructions to include step to add build_runner as a dependency before running code generation.
- Added license field in pubspec.yaml.

## v0.1.0 - Initial release (Beta)
Release date: 30/06/2025

- Initial release of Firestorm for Flutter and Dart.
- Supports Firestore and Realtime Database.
- Includes basic functionality for querying, adding, updating, and deleting documents.
- Provides support for custom data classes with code generation.
- Includes comprehensive documentation and examples for getting started.
- Supports both Flutter and plain Dart applications.
- Support for inheritance in custom data classes.
- Support for custom data class serialization and deserialization.
- Includes support for real-time updates, offline persistence, transactions, and batch writes.
