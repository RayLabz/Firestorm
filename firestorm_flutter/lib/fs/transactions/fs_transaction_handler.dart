import 'package:firestorm/fs/transactions/fs_transaction_create_delegate.dart';

import 'fs_transaction_delete_delegate.dart';
import 'fs_transaction_get_delegate.dart';
import 'fs_transaction_update_delegate.dart';

/// A class that handles Firestorm transactions.
abstract class FSTransactionHandler {

  //Lower-level delegates for operations
  late final FSTransactionCreateDelegate create;
  late final FSTransactionGetDelegate get;
  late final FSTransactionUpdateDelegate update;
  late final FSTransactionDeleteDelegate delete;

  /// Handles the transaction logic.
  Future<void> handleTransaction();

}