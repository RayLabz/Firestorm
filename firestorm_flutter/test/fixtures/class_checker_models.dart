import 'package:firestorm/annotations/firestorm_object.dart';

class BaseEntity {
  String id = 'base';
}

@FirestormObject()
class ValidEntity extends BaseEntity {
  int count = 0;
}

@FirestormObject()
class MissingIdEntity {
  int count = 0;
}

@FirestormObject()
class UnsupportedEntity {
  String id = 'unsupported';
  Duration ttl = Duration.zero;
}

enum Role { admin, user }

@FirestormObject()
class EnumEntity {
  String id = 'enum';
  Role role = Role.admin;
}

