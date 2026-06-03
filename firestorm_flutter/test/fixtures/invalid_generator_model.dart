import 'package:firestorm/annotations/exclude.dart';
import 'package:firestorm/annotations/firestorm_object.dart';

@FirestormObject()
class InvalidEntity {
  String id;
  @Exclude()
  String secret;

  InvalidEntity(this.id, this.secret);
}

