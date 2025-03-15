import 'package:flutter_test_app/computing_student.dart';
import 'package:flutter_test_app/generated/firestorm_models.dart';

main() {
  ComputingStudent student = ComputingStudent(
    "123",
    "John",
    "Smith",
    21,
    1.77,
    true,
    ["Jane", "Doe"],
    "ABC123",
    "School of Computing",
    "Software Engineering"
  );

  student.toMap();

}