import 'package:logger/logger.dart';
import 'package:uuid/uuid.dart';

typedef Serializer = Map<String, dynamic> Function(dynamic); //Used to dynamically serialize objects
typedef Deserializer = dynamic Function(Map<String, dynamic> map); //Used to dynamically deserialize objects

class Firestorm {

  ///Sets debugging messages on/off:
  static bool debug = false;

  /// Logs warnings and errors on the console.
  static Logger log = Logger(
    printer: PrettyPrinter(
      methodCount: 0,
      errorMethodCount: 5,
      lineLength: 80,
      colors: true,
      printEmojis: true,
      printTime: false,
    ),
    output: ConsoleOutput(),
    level: Level.trace,
    filter: ProductionFilter(),
  );

  /// Returns a random ID using UUID v8.
  static String randomID() {
    return const Uuid().v8();
  }

}