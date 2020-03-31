import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.raylabz.firestorm.Firestorm;
import com.raylabz.firestorm.FirestormConfig;
import com.raylabz.firestorm.Paginator;
import com.raylabz.firestorm.QueryResult;

import java.io.IOException;
import java.util.Scanner;

public class PaginationExamples {

    public static void main(String[] args) {

        //Initialize Firebase:
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(FirestormConfig.fromString(ServiceAccount.FIREBASE_SERVICE_ACCOUNT_JSON).toInputStream()))
                    .setDatabaseUrl(ServiceAccount.DATABASE_URL)
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Initialize Firestorm:
        Firestorm.init();

        //Pagination:

        QueryResult<Person> result;
        String lastDocumentID = null;
        do {
            result = Paginator.init(Person.class, lastDocumentID, 5).orderBy("age").fetch();
            System.out.println("Fetched items: " + result.getItems().size());
            for (Person p : result.getItems()) {
                System.out.println("-> " + p.getFirstName());
            }
            lastDocumentID = result.getLastDocumentID();
            new Scanner(System.in).nextLine();
        } while (result.hasItems());


    }

}
