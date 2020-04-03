import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.raylabz.firestorm.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class MainExamples {

    public static final String FIREBASE_SERVICE_ACCOUNT_JSON = "{\n" +
            "  \"type\": \"service_account\",\n" +
            "  \"project_id\": \"raylabz\",\n" +
            "  \"private_key_id\": \"06d78bb1d9a92b622f2c448895af3e5334d6ebf4\",\n" +
            "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCUu+X6iPbs+MWi\\nU96ud3QdsThKMlDReXbAweBH7szwZ5z15fMuFaf/LHJP+VsW7+DXbGME8XMDvDEQ\\nf9MW3qwipZMx0NlOrcUnKezIW4dUGSMmWzZC7qltEVOR9Tw/tOQMbKE17yNuMzGq\\ni4BsAr3sy63+8x+3yfbh+aPWBraMzwNn6auunJRTsEaui67zYvETEF80mgYWTLvL\\nFlw/Y1ITxijKplri/NpOJZGx6CMwapPvhwdwGnmoKCqYF8LwFGjhroarIqP9GDA/\\nKTMvOawWJMn8N8oiU2yrjjHXWNhiQxjK1lvba8sMswbs4emmTxDvg2Bx7vbEvpLP\\n/8ZHGCBdAgMBAAECggEAHBdpFmqsCJHr8f7ZPiZ9neYM9aaPMSE51qpHjA6EUpuf\\nP6pQxdF9S2q/pG86Txi7ZLSp9xMLYBc5RR5/ZgBkjp6rMGt7v6p+ZCk8+ZKf+rhM\\ntT/g/b9J7LmrCRRCbt3hfJ8/y4oBLR0qMkXq2XujF4hYsqCEyxewM3oNAa1+6RUe\\nnCkUhKkclhiMOFpOnICk3ByXwkl1fnG5NtNJu9A1RqPqcGHLtn1yQFJF8awFki8r\\nwouQS8P1k+Np8zxbjM0I4cA9QizF9WhmwZ8Ym972/LFRaj59UwJk1DP6ClU1ITlY\\nQlRZf/WEuaK/m4BbUMe/aOhJgko0URI+zGLZ4k63gQKBgQDHBRguIjP4FAagFsxZ\\nw3oa+NYM6p8ckfrh8yyMYMTyJY0q40FhX5T6Ro8W0Rk7BZpbyAupt58q5GAQ7tfv\\ngSj24d6s5hQfiI/puv1wnbwAOL24HeGo4+5xvk0qlJm7QnE4IjZMEVG7gXnx8fdZ\\n6f5vZ8bbaOqxnxuD3b0lk7c9kQKBgQC/USmUwkWXQpN5KCMfmrncQAETpWj8TEnA\\nL8JsE9reB+0bwyer80/ea9e0f7pykOI2tl27dvTOdSuKvG+LGwWilds0bjE4AuYV\\nv31TErhOrfO+3E/EzmMp3Lu1RX36HyXwd2PMXDf+b89KC2vOYMvkEiVJC8CrVJK4\\n8ChwXSoADQKBgDIeXlB/3IaExIZcN2oDiCRytbTbAOa4/G0aSAWx+Obh/gLm5/gV\\nkfbU/gPI9Imgh5bFcepzi2nJyUd6Xna6GA7UIrd32Wl3goZhyKErl8SWhXBwVfuq\\nKsoKGnS2p2A5AzoXo4TUd7i4inUbcib6flCf2bw3V6MTZmJ23MMxx9uBAoGBAI98\\nT7NCAO9w/yne8HjDkqGdXzFc3WBb1vRM3YQxX/Ul/K7VrutHkJ9AQocdRLin8QEX\\nxTTUAsbVGdLzgLTTj2Zg/gZUnpVjlyy4cUXcxF4+GJEMqsbwA29iHWXWuKTet5Wh\\n9TdP8q7hLJZb+n7OGhzzfwNJzTayN1h8xdeACAABAoGACE5yt8H5ARNx/2Q3UkDU\\nuM0C8svuHWIjCu6wIa0lcBjzvgj4DJMFBNuyQl4LaFo3h5RrkCwmF3W8sB/3rdwg\\nAX5lvLzgY7l7lIks3V7F4WvaYbOMEJnuV8JY7YzqBHJvtUB+qa0Gt7KMSJV29KYi\\n6hdnVJ206DXgpOCSrsL3tCg=\\n-----END PRIVATE KEY-----\\n\",\n" +
            "  \"client_email\": \"raylabz@appspot.gserviceaccount.com\",\n" +
            "  \"client_id\": \"116043340128911688418\",\n" +
            "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
            "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
            "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
            "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/raylabz%40appspot.gserviceaccount.com\"\n" +
            "}\n";

    public static void main(String[] args) {

        //Initialize Firebase:

        try {

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(FirestormConfig.fromString(FIREBASE_SERVICE_ACCOUNT_JSON).toInputStream()))
                    .setDatabaseUrl("https://raylabz.firebaseio.com/")
                    .build();

            FirebaseApp.initializeApp(options);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Initialize Firestorm:
        Firestorm.init();

//        //Instantiate object:
//        Person person = new Person("firstname", "lastname", 50, "excluded");
//
//        //Save to Firestore:
//        Firestorm.create(person, error -> {
//            System.out.println("Write failed.");
//        });
//
//        person.setAge(52);
//
//        Firestorm.update(person, error -> {
//            System.out.println("Update failed.");
//        });
//
//        Person person1 = Firestorm.get(Person.class, "X", error -> {
//            System.out.println("Get failed.");
//        });
//
//        Firestorm.delete(person, error -> {
//            System.out.println("Delete failed.");
//        });
//
//        ArrayList<Person> list = Firestorm.list(Person.class, 10, e -> {
//            System.out.println("List failed.");
//        });
//
//        ArrayList<Person> list = Firestorm.listAll(Person.class, e -> {
//            System.out.println("List failed.");
//        });
//
//        Firestorm.delete(person, e -> {
//            System.out.println("Delete failed.");
//        });

//        ApiFuture<QuerySnapshot> future = firestore.collection(Person.class.getSimpleName()).get();
//        try {
//            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
//            for (DocumentSnapshot document : documents) {
//                Person person = document.toObject(Person.class);
//            }
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//
//        ApiFuture<WriteResult> writeResult = firestore.collection(Person.class.getSimpleName()).document("myPerson").delete();
//        try {
//            writeResult.get();
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }

//        ApiFuture<QuerySnapshot> future = firestore.collection(Person.class.getSimpleName())
//                .whereEqualTo("age", 50)
//                .get();
//        try {
//            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
//            for (DocumentSnapshot document : documents) {
//                Person person = document.toObject(Person.class);
//            }
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }


//
//        QueryResult<Person> result = Firestorm.filter(Person.class)
//                .whereEqualTo("age", 50)
//                .fetch();
//        ArrayList<Person> items = result.getItems();

        Firestore firestore;

        DocumentReference docRef = firestore.collection(Person.class.getSimpleName()).document("myPerson");
        ApiFuture<Void> futureTransaction = firestore.runTransaction(transaction -> {
            DocumentSnapshot snapshot = transaction.get(docRef).get();
            long oldAge = snapshot.getLong("age");
            transaction.update(docRef, "age", oldAge + 1);
            return null;
        });
        try {
            futureTransaction.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        WriteBatch batch = firestore.batch();
        DocumentReference docRef = firestore.collection(Person.class.getSimpleName()).document("person1");
        batch.set(docRef, person1);
        DocumentReference docRef2 = firestore.collection(Person.class.getSimpleName()).document("person2");
        batch.update(docRef2, "age", 15);
        DocumentReference docRef3 = firestore.collection(Person.class.getSimpleName()).document("person3");
        batch.delete(docRef3);
        ApiFuture<List<WriteResult>> future = batch.commit();
        try {
            for (WriteResult result :future.get()) {
                //TODO
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        final Person person = new Person();

        Firestorm.runTransaction(new FirestormTransaction() {
            @Override
            public void execute() {
                get(Person.class, person.getId());
                person.setAge(20);
                update(person);
            }
            @Override
            public void onFailure(Exception e) {
                System.out.println("Transaction failed.");
            }
            @Override
            public void onSuccess() {
                System.out.println("Transaction success.");
            }
        });


        Person person = new Person("MyPerson", "MyPerson", 10, "X");
        Firestorm.create(person, new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                System.out.println("FAILED");
            }
        });

        Firestorm.attachListener(new FirestormEventListener<Person>(person) {
            @Override
            public void onSuccess() {
                System.out.println("Object updated!");
            }

            @Override
            public void onFailure(String failureMessage) {
                System.out.println("Failed to update object.");
            }
        });

        for (int i = 1; i < 6; i++) {
            new Scanner(System.in).nextLine();
            person.setAge(-i);
            Firestorm.update(person);
        }

        System.out.println("FINALLY:");
        System.out.println("AGE = " + person.getAge());

        Firestorm.delete(person);

        Firestorm.runBatch(new FirestormBatch() {
            @Override
            public void execute() {
                create(person1);
                update(person2);
                delete(person3);
            }
            @Override
            public void onFailure(Exception e) {
                System.out.println("Batch write failed.");
            }
            @Override
            public void onSuccess() {
                System.out.println("Batch write success.");
            }
        });

        try {
            //First page:
            CollectionReference people = firestore.collection(Person.class.getSimpleName());
            Query firstPage = people.orderBy("age").limit(25);
            ApiFuture<QuerySnapshot> future = firstPage.get();
            List<QueryDocumentSnapshot> docs = future.get().getDocuments();
            for (QueryDocumentSnapshot snapshot : docs) {
                //TODO
            }
            //Next page (and so on):
            QueryDocumentSnapshot lastDoc = docs.get(docs.size() - 1);
            Query secondPage = people.orderBy("age").startAfter(lastDoc).limit(25);
            future = secondPage.get();
            docs = future.get().getDocuments();
            for (QueryDocumentSnapshot snapshot : docs) {
                //TODO
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        //First page:
        QueryResult<Person> result;
        String lastDocumentID = null;
        result = Paginator.next(Person.class, lastDocumentID, 5).orderBy("age").fetch();
        for (Person p : result.getItems()) {
            //TODO
        }
        lastDocumentID = result.getLastDocumentID();
        //Next page (and so on):
        result = Paginator.next(Person.class, lastDocumentID, 5).orderBy("age").fetch();
        for (Person p : result.getItems()) {
            //TODO
        }
        lastDocumentID = result.getLastDocumentID();







    }

}
