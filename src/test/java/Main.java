import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteBatch;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.gson.Gson;
import com.raylabz.firestorm.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

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

        try {

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(FirestormConfig.fromString(FIREBASE_SERVICE_ACCOUNT_JSON).toInputStream()))
                    .setDatabaseUrl("https://raylabz.firebaseio.com/")
                    .build();

            FirebaseApp.initializeApp(options);
            Firestorm.init();

//            Person person = new Person("Nicos", "Kasenides", 26);
//            Person person2 = new Person("Panayiota", "Michaelide", 24);
//
//            System.out.println("-- CREATE --");
//
//            //Create:
//            long before = System.currentTimeMillis();
//            Firestorm.create(person);
//            long after = System.currentTimeMillis();
//            System.out.println(after - before);
//
//            new Scanner(System.in).nextLine();
//
//            System.out.println("-- CREATE 2 --");
//
//            //Create:
//            before = System.currentTimeMillis();
//            Firestorm.create(person2);
//            after = System.currentTimeMillis();
//            System.out.println(after - before);
//
//            System.out.println("-- LISTENER ATTACH --");
//
//            Firestorm.attachListener(person2, new FirestormEventListener<Person>(Person.class) {
//                @Override
//                public void onSuccess(Person object) {
//                    System.out.println("Received an update to an object: " + object.getFirstname());
//                }
//
//                @Override
//                public void onFailure(String failureMessage) {
//                    System.out.println("Error: " + failureMessage);
//                }
//            });
//
//            new Scanner(System.in).nextLine();
//
//            System.out.println("-- UPDATE --");
//
//            //Update:
//            person.setAge(person.getAge() + 1);
//            before = System.currentTimeMillis();
//            Firestorm.update(person);
//            after = System.currentTimeMillis();
//            System.out.println(after - before);
//
//            new Scanner(System.in).nextLine();
//
//            System.out.println("-- GET --");
//
//            //Get:
//            final String id = person.getId();
//            before = System.currentTimeMillis();
//            final Person retrievedPerson = Firestorm.get(Person.class, id);
//            after = System.currentTimeMillis();
//            System.out.println(new Gson().toJson(retrievedPerson));
//            System.out.println(after - before);
//
//            new Scanner(System.in).nextLine();
//
//            System.out.println("-- LIST--");
//
//            //List all:
//            before = System.currentTimeMillis();
//            final ArrayList<Person> persons = Firestorm.list(Person.class);
//            after = System.currentTimeMillis();
//            for (Person p : persons) {
//                System.out.println("--> " + p.getFirstname());
//            }
//            System.out.println(after - before);
//
//            new Scanner(System.in).nextLine();
//
//            System.out.println("-- FILTER --");
//
//            //List filter:
//            before = System.currentTimeMillis();
//            final ArrayList<Person> filteredPersons = Firestorm.filter(Person.class).whereGreaterThan("age", 25).fetch();
//            after = System.currentTimeMillis();
//            for (Person p : filteredPersons) {
//                System.out.println("--> " + p.getFirstname());
//            }
//            System.out.println(after - before);
//
//            new Scanner(System.in).nextLine();
//
//            System.out.println("-- DELETE --");
//
//            //Delete:
//            before = System.currentTimeMillis();
//            Firestorm.delete(person);
//            after = System.currentTimeMillis();
//            System.out.println(after - before);
//
//            new Scanner(System.in).nextLine();
//
//            System.out.println("-- TRANSACTION --");
//
//            Firestorm.runTransaction(new FirestormTransaction() {
//                @Override
//                public void managedExecute() {
//                    Person p = get(Person.class, person2.getId());
//                    System.out.println("Fetched with transaction: " + p.getFirstname());
//                    delete(p);
//                    System.out.println("Deleted with transaction (person 2)");
//
//                    Person person3 = new Person("John", "Smith", 50);
//                    create(person3);
//                    System.out.println("Created person3 with transaction");
//
//                    person3.setAge(40);
//                    update(person3);
//                    System.out.println("Updated person3 with transaction");
//
//                    delete(person3);
//                    System.out.println("Deleted person3 with transaction");
//                }
//            });
//
//
//            Firestorm.runBatch(new FirestormBatch() {
//                @Override
//                public void managedExecute() {
//                    create(new Person("Nicos", "K", 26));
//                    create(new Person("Nicos2", "K2", 27));
//                    create(new Person("Nicos3", "K3", 28));
//                }
//                @Override
//                public void onSuccess() {
//
//                }
//
//                @Override
//                public void onFailure(Exception e) {
//
//                }
//            });


        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO - Batch writes: https://firebase.google.com/docs/firestore/manage-data/transactions#batched-writes

        //TODO - Cursors & pagination: https://firebase.google.com/docs/firestore/query-data/query-cursors

        //TODO - Custom based data export tooling

        //TODO - Nested collections? Allow this?


    }

}
