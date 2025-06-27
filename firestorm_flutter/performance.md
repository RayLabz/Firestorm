# Firestorm for Flutter: Performance Guide

Firestorm is designed to be lightweight and efficient, but there are some 
performance overheads to consider when using it in your applications.
This document outlines the key performance considerations and how to mitigate
them.

## Contents
*  [Performance Considerations](#performance-considerations)
* [Performance Data](#performance-data)
  * [Firestorm vs Firestore](#firestorm-vs-firestore)
  * [Firestorm vs Realtime Database](#firestorm-vs-realtime-database)

## Performance Considerations
1. **Serialization/Deserialization**: Firestorm automatically serializes and deserializes objects to/from the database format. This process can introduce some overhead, especially for large objects or complex data structures. To minimize this, keep your objects as simple as possible and avoid unnecessary nesting.
2. **Real-time Updates**: Firestorm supports real-time updates, which can be very useful but may also lead to performance issues if not managed properly. If you have a large number of listeners or frequently changing data, consider using batch updates or limiting the number of active listeners.
3. **Network Latency**: Firestorm relies on network calls to interact with Firestore and Realtime Database. Network latency can affect the performance of your application, especially in regions with poor connectivity. To mitigate this, consider using offline persistence features and caching strategies.
4. **Batch Operations**: Firestorm supports batch operations for writing data, which can significantly improve performance when writing multiple documents at once. Use batch writes whenever possible to reduce the number of network calls and improve overall performance.
5. **Indexing**: Ensure that your Firestore collections are properly indexed. Firestorm will not automatically create indexes for you, and missing indexes can lead to slow queries and performance issues. Refer to the Firestore documentation for guidance on indexing.
6. **Memory Usage**: Firestorm's object-oriented approach may lead to higher memory usage compared to raw database operations. Monitor your application's memory usage and optimize your data structures as needed.
7. **Garbage Collection**: Dart's garbage collector may not immediately reclaim memory used by Firestorm objects, especially if they are still referenced in your code. Be mindful of object references and nullify them when they are no longer needed to help the garbage collector free up memory.
8. **Testing and Profiling**: Regularly test and profile your application to identify performance bottlenecks. Use tools like the Dart DevTools to monitor performance and memory usage, and optimize your code accordingly.
9. **Concurrency**: Firestorm operations are generally asynchronous, which can lead to concurrency issues if not handled properly. Use `async` and `await` keywords to manage asynchronous operations and ensure that your code runs in the expected order.
10. **Error Handling**: Proper error handling is crucial for maintaining performance. Ensure that you handle exceptions gracefully and avoid unnecessary retries or operations that could lead to performance degradation.
11. **Data Structure Design**: Design your data structures with performance in mind. Avoid deeply nested objects and large arrays, as these can lead to increased serialization/deserialization times and memory usage.
12. **Use of Streams**: When using Firestorm's real-time updates, be cautious with the number of active streams. Each stream consumes resources, so consider using a single stream for multiple listeners or batching updates where possible.
13. **Avoiding Unnecessary Writes**: Be mindful of how often you write to the database. Frequent writes can lead to increased latency and performance issues. Use Firestorm's built-in caching and offline capabilities to minimize unnecessary writes.
14. **Testing on Multiple Devices**: Performance can vary across different devices and platforms. Test your application on a range of devices to ensure consistent performance and identify any device-specific issues.
15. **Documentation and Community Support**: Stay updated with the latest Firestorm documentation and community discussions. Performance optimizations and best practices may evolve over time, so it's important to keep abreast of new developments.

## Performance Data

The following data shows the performance of Firestorm compared to raw Firestore and Realtime Database operations. The tests were conducted on a sample application with various data access patterns.
Average times are measured in milliseconds (ms) for each operation type.
The data was collected using the Dart `Stopwatch` class to measure the time taken for each operation.
Firebase's emulator suite was used for testing to ensure consistent results across different runs, and to negate network latency effects.

### Firestorm vs Firestore

| Operation Type      | Firestorm (ms) | Firestore API (ms) | Impact                  |
|---------------------|----------------|--------------------|-------------------------|
| Single object write | 32.5           | 32.9               | Negligible              |
| Single object read  | 57.6           | 57.1               | Negligible              |
| Single update       | 43.5           | 42.1               | Negligible              |
| Single delete       | 45.9           | 45.9               | Equal                   |
| Batch write (100)   | 153.6          | 184.8              | Significantly better ✅  |
| Batch read (100)    | 476.2          | 477.2              | Negligible              |
| Batch update (100)  | 219.3          | 4481.7             | Overwhelmingly better ✅ |
| Batch delete (100)  | 486.3          | 488.3              | Negligible              |

### Firestorm vs Realtime Database

| Operation Type      | Firestorm (ms) | Realtime Database API (ms) | Impact                   |
|---------------------|----------------|----------------------------|--------------------------|
| Single object write | 6.1            | 5.8                        | Marginally worse ❌       |
| Single object read  | 5.0            | 5.1                        | Negligible               |
| Single update       | 4.9            | 4.9                        | Equal                    |
| Single delete       | 5.0            | 5.0                        | Equal                    |
| Batch write (100)   | 29.7           | 187.2                      | Overwhelmingly better ✅  |
| Batch read (100)    | 160.3          | 159.5                      | Negligible               |
| Batch update (100)  | 15.86          | 187.8                      | Overwhelmingly better ✅  |
| Batch delete (100)  | 13.1           | 163.3                      | Overwhelmingly better ✅  |

