import 'fs_batch_create_delegate.dart';
import 'fs_batch_delete_delegate.dart';
import 'fs_batch_update_delegate.dart';

/// A class that handles Firestorm batches.
abstract class FSBatchHandler {

  //Lower-level delegates for operations
  late final FSBatchCreateDelegate create;
  late final FSBatchUpdateDelegate update;
  late final FSBatchDeleteDelegate delete;

  /// Handles the batch logic.
  handleBatch();

}