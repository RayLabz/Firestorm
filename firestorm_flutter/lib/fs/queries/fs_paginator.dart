import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firestorm/fs/queries/fs_query_result.dart';
import '../../firestorm.dart';
import '../fs.dart';

/// A paginator for Firestore queries that allows fetching objects in pages.
class FSPaginator<T> {

  late String? lastDocumentID;
  final String? subcollection;
  int numOfDocuments;
  Query query;

  /// Creates a paginator for Firestore queries.
  FSPaginator({ this.lastDocumentID, this.numOfDocuments = 10, this.subcollection }) : query = FS.reference.collection(T, subcollection: subcollection) {
    query = query.orderBy("id");
  }

  /// Filters results by equality.
  FSPaginator<T> whereEqualTo(String field, dynamic value) {
    query = query.where(field, isEqualTo: value);
    return this;
  }

  /// Filters results by inequality.
  FSPaginator<T> whereNotEqualTo(String field, dynamic value) {
    query = query.where(field, isNotEqualTo: value);
    return this;
  }

  /// Filters results by value (less than).
  FSPaginator<T> whereLessThan(String field, dynamic value) {
    query = query.where(field, isLessThan: value);
    return this;
  }

  /// Filters results by value (less than or equal to).
  FSPaginator<T> whereLessThanOrEqualTo(String field, dynamic value) {
    query = query.where(field, isLessThanOrEqualTo: value);
    return this;
  }

  /// Filters results by value (greater than).
  FSPaginator<T> whereGreaterThan(String field, dynamic value) {
    query = query.where(field, isGreaterThan: value);
    return this;
  }

  /// Filters results by value (greater than or equal to).
  FSPaginator<T> whereGreaterThanOrEqualTo(String field, dynamic value) {
    query = query.where(field, isGreaterThanOrEqualTo: value);
    return this;
  }

  /// Filters results by array field containing a value.
  FSPaginator<T> whereArrayContains(String field, dynamic value) {
    query = query.where(field, arrayContains: value);
    return this;
  }

  /// Filters results by array field containing a value.
  FSPaginator<T> whereArrayContainsAny(String field, List<dynamic> values) {
    query = query.where(field, arrayContainsAny: values);
    return this;
  }

  /// Filters results by array field containing any of the provided values.
  FSPaginator<T> whereIn(String field, List<dynamic> values) {
    query = query.where(field, whereIn: values);
    return this;
  }

  /// Filters by an array field NOT containing any of the provided values.
  FSPaginator<T> whereNotIn(String field, List<dynamic> values) {
    query = query.where(field, whereNotIn: values);
    return this;
  }

  /// Orders the query results by a field's value.
  FSPaginator<T> orderBy(String field, { bool descending = false }) {
    query = query.orderBy(field, descending: descending);
    return this;
  }

  /// Limits the number of results.
  FSPaginator<T> limit(int limit) {
    query = query.limit(limit);
    return this;
  }

  /// Starts the query at a document where the field values provided are met.
  FSPaginator<T> startAt(List<dynamic> fieldValues) {
    query = query.startAt(fieldValues);
    return this;
  }

  /// Starts the query after a specific document where the field values provided are met.
  FSPaginator<T> startAfter(List<dynamic> fieldValues) {
    query = query.startAfter(fieldValues);
    return this;
  }

  /// Ends the query before a specific document where the field values provided are met.
  FSPaginator<T> endBefore(List<dynamic> fieldValues) {
    query = query.endBefore(fieldValues);
    return this;
  }

  /// Ends the query at a specific document where the field values provided are met.
  FSPaginator<T> endAt(List<dynamic> fieldValues) {
    query = query.endAt(fieldValues);
    return this;
  }

  /// Streams the results of the query as a stream of type T.
  Stream<T> stream() async* {
    Deserializer? deserializer = FS.deserializers[T];
    if (deserializer == null) {
      throw UnsupportedError('No serializer found for type: $T. Consider re-generating Firestorm data classes.');
    }
    Stream<QuerySnapshot<Object?>> snapshots = query.snapshots();
    await for (QuerySnapshot<Object?> snapshot in snapshots) {
      for (QueryDocumentSnapshot<Object?> doc in snapshot.docs) {
        yield deserializer(doc.data() as Map<String, dynamic>);
      }
    }
  }

  /// Fetches the next page of objects.
  Future<FSQueryResult<T>> next() async {
    Deserializer? deserializer = FS.deserializers[T];
    final String? className = FS.classNames[T];
    if (deserializer == null || className == null) {
      throw UnsupportedError('No deserializer/class name found for type: $T. Consider re-generating Firestorm data classes.');
    }

    //If there is a last document ID, start from after that
    if (lastDocumentID != null) {
      dynamic object = await FS.get.one<T>(lastDocumentID!, subcollection: subcollection);
      if (object != null) {
        if (object.id != null) {
          query = query.startAfter([object.id]);
        }
        else {
          throw ArgumentError('FSPaginator: The last document ID provided is not valid: $lastDocumentID');
        }
      }
    }

    //Limit the number of documents to fetch
    query = query.limit(numOfDocuments);

    //Run the query and return results:
    var documents = await query.get();
    List<T> objects = [];

    for (QueryDocumentSnapshot doc in documents.docs) {
      T object = deserializer(doc.data() as Map<String, dynamic>);
      objects.add(object);
    }
    if (objects.isEmpty) {
      return FSQueryResult(objects, null, null);
    }
    else {
      lastDocumentID = documents.docs[documents.docs.length - 1].id;
      return FSQueryResult(objects, documents.docs[documents.docs.length - 1], lastDocumentID);
    }
  }


}