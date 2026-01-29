import 'package:firestorm/annotations/firestorm_object.dart';
import 'package:flutter_test_app/player_color.dart';


@FirestormObject()
class Player {

  String id;
  String nickname;
  String? avatarUrl;
  int points = 0;
  int wins = 0;
  int losses = 0;
  int draws = 0;
  bool isOnline = false;
  PlayerColor color;
  List<String> friendIDs = [];

  Player(this.id, this.nickname, this.avatarUrl, this.color);

}