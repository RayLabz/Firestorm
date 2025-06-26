import 'dart:async';

import 'package:flutter/foundation.dart' show compute, kIsWeb;

import '../fs/fs.dart';

class MT {

  static Future<R> computeSafe<Q, R>(
      FutureOr<R> Function(Q message) fn,
      Q message,
      ) async {
    if (kIsWeb) return fn(message);
    try {
      return await compute(fn, message);
    } catch (_) {
      return fn(message);
    }
  }

  static List<Map<String, dynamic>> serializeObjects<T>(Map<String, dynamic> args) {
    final List<T> objects = List<T>.from(args['objects']);
    final serializer = FS.serializers[T];
    if (serializer == null) {
      throw UnsupportedError('No serializer found for type: ${T.toString()}');
    }
    return objects.map((obj) => serializer(obj)).toList();
  }

}

