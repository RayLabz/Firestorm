import 'package:firestorm/annotations/firestorm_object.dart';

@FirestormObject()
class ScheduledWorkout {
  String id;
  String userId;
  String workoutId;
  String? originalWorkoutId;
  DateTime scheduledDate;
  bool isCompleted;
  DateTime? completedDate;
  int? totalDuration;
  int? currentExerciseIndex;
  int? currentSet;
  int? elapsedSeconds;
  int? remainingSeconds;
  int? aerobicStartSeconds;
  String? currentPhase;
  bool? isInProgress;


  ScheduledWorkout({
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