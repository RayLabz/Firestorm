import 'package:firestorm_demonstrator/screen/view_contacts_screen.dart';
import 'package:firestorm_demonstrator/theme/theme.dart';
import 'package:flutter/material.dart';

/// Main entry point for the Contacts App
class ContactsApp extends StatelessWidget {

  const ContactsApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Contacts App',
      home: ViewContactsScreen(), //Main screen to view contacts
      theme: appTheme
    );
  }

}
