// import 'package:cloud_firestore/cloud_firestore.dart';
//
// import '../fs.dart';
// import 'converter.dart';
//
// class QueryDocumentToObjectConverter<T> implements Converter<QueryDocumentSnapshot<Map<String, dynamic>>, T> {
//
//   @override
//   T convertSingle(QueryDocumentSnapshot<Map<String, dynamic>> item) {
//     final deserializer = FS.deserializers[T];
//     if (deserializer == null) {
//       throw UnsupportedError('No deserializer found for type: $T. Consider re-generating Firestorm data classes.');
//     }
//     return deserializer(item.data());
//   }
//
//   @override
//   List<T> convertMultiple(List<QueryDocumentSnapshot<Map<String, dynamic>>> items) {
//     List<T> objectsList = [];
//     //TODO - Consider the use of Multithreading.
//     for (QueryDocumentSnapshot<Map<String, dynamic>> item in items) {
//       objectsList.add(convertSingle(item));
//     }
//     return objectsList;
//   }
//
// }