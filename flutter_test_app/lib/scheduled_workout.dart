import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firestorm/annotations/firestorm_object.dart';

@FirestormObject()
class ScheduledWorkout {
  String id;
  String userId;
  String workoutId;
  String? originalWorkoutId;
  Timestamp scheduledDate;
  bool isCompleted;
  Timestamp? completedDate;
  int? totalDuration;
  int? currentExerciseIndex;
  int? currentSet;
  int? elapsedSeconds;
  int? remainingSeconds;
  int? aerobicStartSeconds;
  String? currentPhase;
  bool? isInProgress;
  GeoPoint geoPoint;

  ScheduledWorkout(this.geoPoint, {
    required this.id,
    required this.userId,
    required this.workoutId,
    this.originalWorkoutId,
    required this.scheduledDate,
    required this.isCompleted,
    this.completedDate,
    this.totalDuration,
    this.currentExerciseIndex,
    this.currentSet,
    this.elapsedSeconds,
    this.remainingSeconds,
    this.aerobicStartSeconds,
    this.currentPhase,
    this.isInProgress,
  });

}