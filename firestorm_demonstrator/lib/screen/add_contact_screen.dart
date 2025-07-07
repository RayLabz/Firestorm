import 'package:firestorm/firestorm.dart';
import 'package:firestorm/fs/fs.dart';
import 'package:flutter/material.dart';

import '../model/address.dart';
import '../model/contact.dart';

class AddContactScreen extends StatefulWidget {

  Function() _refreshContacts;

  AddContactScreen(this._refreshContacts, {super.key});

  @override
  State<AddContactScreen> createState() => _AddContactScreenState();
}

class _AddContactScreenState extends State<AddContactScreen> {

  // Controllers for the text fields can be added here if needed
  final TextEditingController firstNameController = TextEditingController();
  final TextEditingController lastNameController = TextEditingController();
  final TextEditingController emailController = TextEditingController();
  final TextEditingController phoneController = TextEditingController();
  final TextEditingController streetController = TextEditingController();
  final TextEditingController cityController = TextEditingController();

  //Nodes
  final FocusNode firstNameFocusNode = FocusNode();
  final FocusNode lastNameFocusNode = FocusNode();
  final FocusNode emailFocusNode = FocusNode();
  final FocusNode phoneFocusNode = FocusNode();
  final FocusNode streetFocusNode = FocusNode();
  final FocusNode cityFocusNode = FocusNode();

  //Global key for the form
  GlobalKey<FormState> formKey = GlobalKey<FormState>();

  @override
  Widget build(BuildContext context) {

    Contact contact = Contact.createRandomContact();

    // Set the initial values for the text fields
    firstNameController.text = contact.firstname;
    lastNameController.text = contact.lastname;
    emailController.text = contact.email;
    phoneController.text = contact.phone;
    streetController.text = contact.address.street;
    cityController.text = contact.address.city;

    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.deepOrange,
        title: const Text('Add Contact'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: formKey,
          child: SingleChildScrollView(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: <Widget>[

                TextFormField(
                  controller: firstNameController,
                  decoration: const InputDecoration(
                    labelText: 'Firstname',
                    border: OutlineInputBorder(),
                  ),
                  validator: (value) => value == null || value.isEmpty
                      ? 'Please enter your first name'
                      : null,
                ),
                const SizedBox(height: 16),

                TextFormField(
                  controller: lastNameController,
                  decoration: const InputDecoration(
                    labelText: 'Lastname',
                    border: OutlineInputBorder(),
                  ),
                  validator: (value) => value == null || value.isEmpty
                      ? 'Please enter your last name'
                      : null,
                ),
                const SizedBox(height: 16),

                TextFormField(
                  controller: emailController,
                  decoration: const InputDecoration(
                    labelText: 'Email',
                    border: OutlineInputBorder(),
                  ),
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      return 'Please enter your email';
                    }
                    // Simple email validation
                    final emailRegex = RegExp(r'^[^@]+@[^@]+\.[^@]+$');
                    if (!emailRegex.hasMatch(value)) {
                      return 'Please enter a valid email address';
                    }
                    return null;
                  },
                ),
                const SizedBox(height: 16),

                TextFormField(
                  controller: phoneController,
                  decoration: const InputDecoration(
                    labelText: 'Phone',
                    border: OutlineInputBorder(),
                  ),
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      return 'Please enter your phone number';
                    }
                    return null;
                  },
                ),
                const SizedBox(height: 16),

                Text("Address"),

                TextFormField(
                  controller: streetController,
                  decoration: const InputDecoration(
                    labelText: 'Street',
                    border: OutlineInputBorder(),
                  ),
                  validator: (value) => value == null || value.isEmpty
                      ? 'Please enter your street address'
                      : null,
                ),
                const SizedBox(height: 16),

                TextFormField(
                  controller: cityController,
                  decoration: const InputDecoration(
                    labelText: 'City',
                    border: OutlineInputBorder(),
                  ),
                  validator: (value) => value == null || value.isEmpty
                      ? 'Please enter your city'
                      : null,
                ),

                const SizedBox(height: 20),

                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    ElevatedButton(
                      onPressed: () {

                        if (formKey.currentState?.validate() ?? false) {
                          final String firstName = firstNameController.text;
                          final String lastName = lastNameController.text;
                          final String email = emailController.text;
                          final String phone = phoneController.text;
                          final String street = streetController.text;
                          final String city = cityController.text;

                          createContact(
                            firstName,
                            lastName,
                            email,
                            phone,
                            street,
                            city,
                          ).then((success) {
                            if (success) {
                              //Clear the text fields after saving
                              firstNameController.clear();
                              lastNameController.clear();
                              emailController.clear();
                              phoneController.clear();
                              streetController.clear();
                              cityController.clear();

                              //Show success message
                              ScaffoldMessenger.of(context).showSnackBar(
                                const SnackBar(content: Text('Contact saved successfully')),
                              );

                              //Navigate back or clear the form
                              Navigator.pop(context);
                            } else {
                              ScaffoldMessenger.of(context).showSnackBar(
                                const SnackBar(content: Text('Failed to save contact')),
                              );
                            }
                          });

                        } else {
                          ScaffoldMessenger.of(context).showSnackBar(
                            const SnackBar(content: Text('Please fill all fields correctly')),
                          );
                        }

                      },
                      child: const Text('Save Contact'),
                    ),
                  ],
                ),
              ],
            ),
          ),

        ),
      ),
    );
  }

  // FIRESTORM: Creates a contact on the Firestore database
  Future<bool> createContact(String firstname, String lastname, String email, String phone, String street, String city) async {
    //Create object:
    final Contact contact = Contact(
      id: Firestorm.randomID(),
      firstname: firstname,
      lastname: lastname,
      email: email,
      phone: phone,
      address: Address(Firestorm.randomID(), street, city),
      createdAt: DateTime.now().millisecondsSinceEpoch,
    );

    try {
      await FS.create.one(contact);
      widget._refreshContacts();
      return true;
    }
    catch (e) {
      return false;
    }
  }

  @override
  void dispose() {
    // Dispose of the controllers and focus nodes
    firstNameController.dispose();
    lastNameController.dispose();
    emailController.dispose();
    phoneController.dispose();
    streetController.dispose();
    cityController.dispose();
    firstNameFocusNode.dispose();
    lastNameFocusNode.dispose();
    emailFocusNode.dispose();
    phoneFocusNode.dispose();
    streetFocusNode.dispose();
    cityFocusNode.dispose();
    super.dispose();
  }

}
