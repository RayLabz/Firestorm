// import 'package:firestorm/annotations/exclude.dart';
// import 'package:firestorm/annotations/firestorm_object.dart';
//
// enum Status { active, inactive }
//
// @FirestormObject()
// class Address {
//   String id;
//   String street;
//
//   Address(this.id, this.street);
// }
//
// @FirestormObject()
// class User {
//   String id;
//   double score;
//   Status status;
//   Address address;
//   @Exclude()
//   String? token;
//   List<int> tags;
//   Map<String, bool> flags;
//
//   User(
//     this.id, {
//     this.score = 0,
//     this.status = Status.active,
//     required this.address,
//     this.token,
//     List<int>? tags,
//     Map<String, bool>? flags,
//   })  : tags = tags ?? [],
//         flags = flags ?? {};
// }
//
