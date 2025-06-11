import 'package:firebase_database/firebase_database.dart';

///Helps with the deserialization of data from the Realtime database by converting
///first into a Map<String, dynamic> to conform to requirements.
class RDBDeserializationHelper {

  /// Converts a [DataSnapshot] to a [Map<String, dynamic>].
  static Map<String, dynamic> snapshotToMap(final DataSnapshot snapshot) {
    final Map<dynamic, dynamic> rawData = Map<dynamic, dynamic>.from(snapshot.value as Map);
    final Map<String, dynamic> data = {};
    for (var key in rawData.keys) {
      // Convert all keys to String
      data[key.toString()] = rawData[key];
    }
    return data;
  }

}