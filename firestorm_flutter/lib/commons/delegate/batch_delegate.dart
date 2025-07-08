/// Contains the operations for batch delegates.
abstract class BatchDelegate<Handler> {

  /// Runs a batch with the provided handler.
  Future<void> run(void Function(Handler batch) batchHandler);

}