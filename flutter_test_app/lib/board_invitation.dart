import 'package:firestorm/annotations/firestorm_object.dart';

import 'invitation.dart';

/// Board Invitation Data for the PwrGo app.
///
/// Author: Joyce Fazaa
@FirestormObject()
class BoardInvitation extends Invitation {

  String? boardID;

  BoardInvitation({
    required super.id,
    this.boardID,
    required super.type,
    required super.senderID,
    required super.receiverID,
    required super.inviteCode,
  });
}
