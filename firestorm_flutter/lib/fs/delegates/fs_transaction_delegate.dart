import 'package:firestorm/fs/transactions/fs_transaction_inliner.dart';

import '../fs.dart';
import '../transactions/fs_transaction_create_delegate.dart';
import '../transactions/fs_transaction_delete_delegate.dart';
import '../transactions/fs_transaction_get_delegate.dart';
import '../transactions/fs_transaction_handler.dart';
import '../transactions/fs_transaction_update_delegate.dart';

/// A delegate to run transactions in Firestore.
class FSTransactionDelegate {

  /// Runs a transaction in Firestore.
  Future<void> run(Future<void> Function(FSTransactionHandler tx) handler,
      {int maxAttempts = 5, Duration timeout = const Duration(seconds: 30)}) async {
    await FS.firestore.runTransaction((tx) async {
      final inliner = FSTransactionInliner(handler);
      inliner.create = FSTransactionCreateDelegate.init(tx);
      inliner.get = FSTransactionGetDelegate.init(tx);
      inliner.update = FSTransactionUpdateDelegate.init(tx);
      inliner.delete = FSTransactionDeleteDelegate.init(tx);
      await inliner.handleTransaction();
    }, maxAttempts: maxAttempts, timeout: timeout);
  }

}