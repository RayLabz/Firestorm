import 'package:firestorm/commons/delegate/batch_delegate.dart';

import '../batch/fs_batch_create_delegate.dart';
import '../batch/fs_batch_delete_delegate.dart';
import '../batch/fs_batch_handler.dart';
import '../batch/fs_batch_inliner.dart';
import '../batch/fs_batch_update_delegate.dart';
import '../fs.dart';

/// A delegate to run batches in Firestore.
class FSBatchDelegate implements BatchDelegate<FSBatchHandler> {

  /// Runs a batch in Firestore.
  @override
  Future<void> run(void Function(FSBatchHandler batch) batchHandler) async {
    final batch = FS.instance.batch();
    final inliner = FSBatchInliner(batchHandler);
    inliner.create = FSBatchCreateDelegate.init(batch);
    inliner.update = FSBatchUpdateDelegate.init(batch);
    inliner.delete = FSBatchDeleteDelegate.init(batch);
    await inliner.handleBatch();
    await batch.commit();
  }

}