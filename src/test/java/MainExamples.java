import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.raylabz.firestorm.*;

import java.io.IOException;
import java.util.ArrayList;

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

        //Instantiate object:
        Person person = new Person("firstname", "lastname", 50, "excluded");

        //Save to Firestore:
        Firestorm.create(person);

        String personID = person.getId();

        //Change object attributes:
        person.setAge(51);

        //Update object:
        Firestorm.update(person);

        FirestormTransaction transaction = new FirestormTransaction() {
            @Override
            public void execute() {
                create(person1);
                update(person2);
                delete(person3);
            }

            @Override
            public void onSuccess() {
                System.out.println("Transaction successful!");
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("Transaction failed.");
            }
        };

        Firestorm.runTransaction(transaction);

        Firestorm.runTransaction(new FirestormTransaction() {
            @Override
            public void execute() {
                create(person1);
                get(person2);
                update(person2);
                delete(person3);
                list(Person.class);
            }

            @Override
            public void onSuccess() {
                System.out.println("Transaction successful!");
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("Transaction failed.");
            }
        });

        FirestormBatch batch = new FirestormBatch() {
            @Override
            public void execute() {
                create(person);
                update(person2);
                delete(person3);
            }

            @Override
            public void onSuccess() {
                System.out.println("Transaction successful!");
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("Transaction failed.");
            }
        };

        Firestorm.runBatch(batch);

        String lastDocumentID = null;
        Paginator<Person> paginator = Paginator.next(Person.class, lastDocumentID);


        final QueryResult<Person> result = paginator.fetch();
        final ArrayList<Person> items = result.getItems();
        lastDocumentID = result.getLastDocumentID();

        paginator = Paginator.next(Person.class, lastDocumentID).whereEqualTo("firstName", "John").whereGreaterThan("age", 5).orderBy("age");


        final QueryResult<Person> result = Firestorm.filter(Person.class)
                .whereEqualTo("firstName", "John")
                .whereGreaterThan("age", 10)
                .orderBy("age")
                .limit(5)
                .fetch();

        final ArrayList<Person> items = result.getItems();

        //Get query snapshot:
        final QueryDocumentSnapshot snapshot = result.getSnapshot();

        //Get the last item's ID:
        final String lastItemID = result.getLastDocumentID();

        //Check if the result has returned and items:
        final boolean hasItems = result.hasItems();




//        // -------- CREATE -------- //
//
//        //Instantiate a custom object:
//        Person person = new Person("MyFirstname", "MyLastname", 50);
//
//        //Write the object in Firestore using Firestorm:
//        Firestorm.create(person);
//
//        // -------- UPDATE -------- //
//
//        //Update the person data locally:
//        person.setAge(51);
//
//        //Update the object in Firestore:
//        Firestorm.update(person);
//
//        // -------- GET -------- //
//
//        //To get a document from Firestore, you need its ID. Ideally, you have saved this from previous use.
//        final String personID = person.getId();
//
//        //Get the document from Firestorm.
//        Person retrievedPerson = Firestorm.get(Person.class, personID);



    }

}
