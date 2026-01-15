import 'package:firestorm/annotations/firestorm_object.dart';

import 'invitation_status.dart';
import 'invitation_type.dart';

/// Invitation Data for the PwrGo app.
///
/// Author: Joyce Fazaa
@FirestormObject()
class Invitation {
  String id;
  InvitationStatus status;
  InvitationType type;
  String senderID;
  String receiverID;
  String inviteCode;

  Invitation({
    required this.id,
    this.status = InvitationStatus.pending,
    required this.type,
    required this.senderID,
    required this.receiverID,
    required this.inviteCode,
  });
}
