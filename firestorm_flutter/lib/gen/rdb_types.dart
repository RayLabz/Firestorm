class RDBTypes {

  ///Checks if the given variable is a supported Real-Time Database type.
  static bool isSupportedRDBType(variable) {
    if (variable is String) {
      return true;
    } else if (variable is int) {
      return true;
    } else if (variable is double) {
      return true;
    } else if (variable is bool) {
      return true;
    } else if (variable is Map<String, dynamic>) {
      return true;
    } else if (variable is List) {
      return true;
    } else if (variable == null) {
      return true;
    }
    return false;
  }

}