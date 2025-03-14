class RDBTypes {

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