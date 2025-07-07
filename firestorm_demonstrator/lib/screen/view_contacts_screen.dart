import 'dart:async';

import 'package:firestorm/fs/fs.dart';
import 'package:firestorm/fs/queries/fs_query_result.dart';
import 'package:flutter/material.dart';
import '../model/contact.dart';
import '../widget/contact_widget.dart';
import 'add_contact_screen.dart';

/// Screen to view contacts
class ViewContactsScreen extends StatefulWidget {
  const ViewContactsScreen({super.key});

  @override
  State<ViewContactsScreen> createState() => _ViewContactsScreenState();
}

class _ViewContactsScreenState extends State<ViewContactsScreen> {

  Map<String, StreamSubscription> contactSubscriptions = {};

  Future<void> fetchContacts() async {
    print("Fetching contacts...");
    Contact.allContacts.clear();
    List<Contact> items = await FS.list.allOfClass<Contact>(Contact);
    for (Contact contact in items) {
      Contact.allContacts[contact.id] = contact;
      StreamSubscription<Contact?> subscription = FS.listen.toObject<Contact>(
        contact,
        onChange: (Contact updatedContact) {
          setState(() {
            Contact.allContacts[updatedContact.id] = updatedContact;
          });
        },
        onDelete: () {
          setState(() {
            Contact.allContacts.remove(contact.id);
            contactSubscriptions[contact.id]?.cancel();
            contactSubscriptions.remove(contact.id);
          });
        },
      );
      contactSubscriptions[contact.id] = subscription;
    }
  }

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.deepOrange,
        title: const Text('Contacts'),
        actions: [
          IconButton(
            icon: const Icon(Icons.add),
            onPressed: () {
              Navigator.push(context,
                MaterialPageRoute(
                  builder: (context) => AddContactScreen(_refreshContacts),
                ),
              );
            },
          ),
        ],
      ),

      body: FutureBuilder(
        future: fetchContacts(),
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return Center(child: Text('Error: ${snapshot.error}'));
          } else {
            return Center(
              child: NotificationListener<ScrollNotification>(
                onNotification: (notification) {
                  if (notification is ScrollEndNotification && notification.metrics.extentAfter == 0) {
                    fetchContacts();
                  }
                  return false;
                },
                child: Contact.allContacts.isNotEmpty ? ListView.builder(
                  itemCount: Contact.allContacts.length,
                  itemBuilder: (context, index) {
                    if (index >= Contact.allContacts.length) {
                      return const Center(child: CircularProgressIndicator());
                    }
                    return ContactWidget(Contact.allContacts.values.elementAt(index));
                  },
                ) : Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    const Text(
                      'No contacts available.',
                      style: TextStyle(fontSize: 24),
                    ),
                    const SizedBox(height: 20),
                    ElevatedButton(
                      onPressed: () {
                        Navigator.push(context,
                          MaterialPageRoute(
                            builder: (context) => AddContactScreen(_refreshContacts),
                          ),
                        );
                      },
                      child: const Text('Add Contact'),
                    ),
                  ],
                ),
              ),
            );
          }
        },
      )
    );

  }

  _refreshContacts() {
    setState(() {
      fetchContacts();
    });
  }

  @override
  void dispose() {
    // Cancel all subscriptions
    for (var subscription in contactSubscriptions.values) {
      subscription.cancel();
    }
    contactSubscriptions.clear();
    super.dispose();
  }

}
