import 'fs_batch_handler.dart';

/// A class that inlines Firestorm transactions.
class FSBatchInliner extends FSBatchHandler {
  final Function(FSBatchHandler handler) _logic;

  FSBatchInliner(this._logic);

  @override
  handleBatch() => _logic(this);

}