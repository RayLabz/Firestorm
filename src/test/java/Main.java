import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.raylabz.firestorm.*;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        try {

            FileInputStream serviceAccount = new FileInputStream("raylabz-06d78bb1d9a9.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://raylabz.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        Firestorm.init();

        //--------------------------------------------------------------------------------------------------------------
        System.out.println("Press to create using object");
        new Scanner(System.in).nextLine();

        Person person = new Person("nicos", "kasenides", 27, -1);
        Person person2 = new Person("xx", "yy", 45, -1);
        final String id = Firestorm.create(person, new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
        Firestorm.create(person2);
        System.out.println(person.getId() + " created!");
        //--------------------------------------------------------------------------------------------------------------
        System.out.println("Press to update");
        new Scanner(System.in).nextLine();

        person.setAge(28);
        Firestorm.update(person);
        //--------------------------------------------------------------------------------------------------------------
        System.out.println("Press to attach listener on object");
        new Scanner(System.in).nextLine();

        Firestorm.attachListener(new OnObjectUpdateListener(person) {
            @Override
            public void onSuccess() {
                System.out.println(person);
            }

            @Override
            public void onFailure(String failureMessage) {
                System.err.println(failureMessage);
            }
        });
        System.out.println("Listener attached");
        //--------------------------------------------------------------------------------------------------------------
        System.out.println("Press to attach listener on reference");
        new Scanner(System.in).nextLine();
        Firestorm.attachListener(new OnReferenceUpdateListener(Person.class, person2.getId()) {
            @Override
            public void onSuccess(Object object) {
                System.out.println(object);
            }

            @Override
            public void onFailure(String failureMessage) {
                System.err.println(failureMessage);
            }
        });
        //--------------------------------------------------------------------------------------------------------------
        System.out.println("Press to get using object");
        new Scanner(System.in).nextLine();

        final Person retrievedPerson = Firestorm.get(Person.class, id);
        System.out.println(retrievedPerson);
        //--------------------------------------------------------------------------------------------------------------
        System.out.println("Press to delete using object");
        new Scanner(System.in).nextLine();

        Firestorm.delete(person);
        //--------------------------------------------------------------------------------------------------------------
        System.out.println("Press to delete using reference");
        new Scanner(System.in).nextLine();

        Firestorm.delete(Person.class, person2.getId());
        //--------------------------------------------------------------------------------------------------------------
        System.out.println("Press to batch write 20 people");
        new Scanner(System.in).nextLine();

        ArrayList<Person> personsToCreate = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            personsToCreate.add(new Person("person" + (i + 1), "lastname" + (i + 1), i, -1));
        }

        Firestorm.runBatch(new FirestormBatch() {
            @Override
            public void execute() {
                for (Person p : personsToCreate) {
                    create(p);
                }
            }

            @Override
            public void onFailure(Exception e) {
                System.err.println(e);
            }

            @Override
            public void onSuccess() {
                System.out.println("Batch success!");
            }
        });
        //--------------------------------------------------------------------------------------------------------------
        System.out.println("Press to make transaction");
        new Scanner(System.in).nextLine();

        Firestorm.runTransaction(new FirestormTransaction() {
            @Override
            public void execute() {
                Person transactionPerson = new Person("Special", "Person", 10, -1);
                create(transactionPerson);
                personsToCreate.get(0).setAge(-10);
                update(personsToCreate.get(0));
                delete(transactionPerson);
            }

            @Override
            public void onFailure(Exception e) {
                System.err.println(e);
            }

            @Override
            public void onSuccess() {
                System.out.println("Transaction success!");
            }
        });
        //--------------------------------------------------------------------------------------------------------------
        System.out.println("Press to batch delete 20 people");
        new Scanner(System.in).nextLine();

        Firestorm.runBatch(new FirestormBatch() {
            @Override
            public void execute() {
                for (Person p : personsToCreate) {
                    delete(p);
                }
            }

            @Override
            public void onFailure(Exception e) {
                System.err.println(e);
            }

            @Override
            public void onSuccess() {
                System.out.println("Batch success!");
            }
        });


    }

}
