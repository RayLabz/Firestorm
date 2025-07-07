import 'package:firestorm/firestorm.dart';
import 'package:firestorm/fs/fs.dart';
import 'package:flutter/material.dart';

import '../model/address.dart';
import '../model/contact.dart';

class ViewContactScreen extends StatefulWidget {

  final Contact contact;

  const ViewContactScreen(this.contact, {super.key});


  @override
  State<ViewContactScreen> createState() => _ViewContactScreenState();
}

class _ViewContactScreenState extends State<ViewContactScreen> {

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

    Contact contact = widget.contact;

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
        title: Text('${contact.firstname} ${contact.lastname}'),
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
                          contact.firstname = firstNameController.text;
                          contact.lastname = lastNameController.text;
                          contact.email = emailController.text;
                          contact.phone = phoneController.text;
                          contact.address.street = streetController.text;
                          contact.address.city = cityController.text;

                          updateContact(contact).then((success) {
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
                      child: const Text('Update Contact'),
                    ),

                    const SizedBox(width: 16),

                    ElevatedButton(
                      style: ElevatedButton.styleFrom(
                        backgroundColor: Colors.red, // Set the button color to red
                      ),
                      onPressed: () {
                        // Show confirmation dialog before deleting
                        AlertDialog dialog = AlertDialog(
                          title: const Text('Delete Contact'),
                          content: const Text('Are you sure you want to delete this contact?'),
                          actions: [
                            TextButton(
                              onPressed: () {
                                Navigator.pop(context); // Close the dialog
                              },
                              child: const Text('Cancel'),
                            ),
                            TextButton(

                              onPressed: () {
                                //Call the delete function
                                deleteContact(contact).then((success) {
                                  if (success) {
                                    ScaffoldMessenger.of(context).showSnackBar(
                                      const SnackBar(content: Text('Contact deleted successfully')),
                                    );
                                    Navigator.pop(context); //Go back to the previous screen
                                  } else {
                                    ScaffoldMessenger.of(context).showSnackBar(
                                      const SnackBar(content: Text('Failed to delete contact')),
                                    );
                                  }
                                });

                                //Go back to main screen:
                                Navigator.pop(context);
                              },
                              child: const Text('Delete'),
                            ),
                          ],
                        );
                        showDialog(context: context, builder: (BuildContext context) => dialog);

                      },
                      child: const Text('Delete contact'),
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

  // FIRESTORM: Updates a contact on the Firestore database
  Future<bool> updateContact(Contact contact) async {
    try {
      await FS.update.one(contact);
      return true;
    }
    catch (e) {
      return false;
    }
  }

  // FIRESTORM: Deletes a contact from the Firestore database
  Future<bool> deleteContact(Contact contact) async {
    try {
      await FS.delete.one(contact);
      return true;
    } catch (e) {
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
