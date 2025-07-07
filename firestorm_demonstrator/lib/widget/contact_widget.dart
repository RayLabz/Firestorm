import 'package:flutter/material.dart';

import '../model/contact.dart';
import '../screen/view_contact_screen.dart';

class ContactWidget extends StatelessWidget {

  final Contact contact;

  const ContactWidget(this.contact, {super.key});

  @override
  Widget build(BuildContext context) {
    return ListTile(
      title: Text('${contact.firstname} ${contact.lastname}'),
      subtitle: Text('${contact.email}\n${contact.phone}'),
      leading: CircleAvatar(
        backgroundColor: Colors.deepOrange,
        child: Text(
          contact.firstname.isNotEmpty ? contact.firstname[0].toUpperCase() : '?',
          style: const TextStyle(color: Colors.white, fontSize: 24),
        ),
      ),
      trailing: IconButton(
        icon: const Icon(Icons.call),
        color: Colors.green,
        onPressed: () {
          // Handle delete action
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text('Calling ${contact.firstname}...')),
          );
        },
      ),
      onTap: () {
        Navigator.push(context, MaterialPageRoute(builder: (context) => ViewContactScreen(contact),));
      },

    );
  }
}
