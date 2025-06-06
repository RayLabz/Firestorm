import 'package:firestorm/fs/transactions/fs_transaction_get_delegate.dart';
import 'package:firestorm/fs/transactions/fs_transaction_update_delegate.dart';

import 'fs_transaction_create_delegate.dart';
import 'fs_transaction_delete_delegate.dart';

/// A class that represents a Firestore transaction.
class FSTransaction {

  //Delegates:
  late FSTransactionCreateDelegate create;
  late FSTransactionGetDelegate get;
  late FSTransactionUpdateDelegate update;
  late FSTransactionDeleteDelegate delete;

}