import 'package:firestorm/annotations/firestorm_object.dart';
import 'package:firestorm_demonstrator/status.dart';

@FirestormObject()
class ContactStatus {

  String id;
  String contactID;
  Status status;

  ContactStatus({
    required this.id,
    required this.contactID,
    required this.status,
  });

}
