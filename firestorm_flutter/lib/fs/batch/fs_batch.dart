import '../transactions/fs_transaction_create_delegate.dart';
import '../transactions/fs_transaction_delete_delegate.dart';
import '../transactions/fs_transaction_get_delegate.dart';
import '../transactions/fs_transaction_update_delegate.dart';

/// A class that represents a Firestore batch.
class FSBatch {

  //Delegates:
  late FSTransactionCreateDelegate create;
  late FSTransactionGetDelegate get;
  late FSTransactionUpdateDelegate update;
  late FSTransactionDeleteDelegate delete;

}