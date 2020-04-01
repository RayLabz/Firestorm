import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.raylabz.firestorm.Firestorm;
import com.raylabz.firestorm.FirestormBatch;
import com.raylabz.firestorm.FirestormConfig;

import java.io.IOException;
import java.util.Scanner;

public class BatchExamples {

    //Create some objects for our test:
    private static final Person john = new Person("John", "Smith", 50, "excluded");
    private static final Person jane = new Person("Jane", "Smith", 45, "excluded");
    private static final Person george = new Person("George", "Smith", 18, "excluded");

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

        //--------------------------------------------------------------------------------------------------------------

        System.out.println("Press enter to run create batch...");
        new Scanner(System.in).nextLine();

        //Run a write batch:
        runCreateBatch();

        //--------------------------------------------------------------------------------------------------------------
        //   UPDATE BATCH

        System.out.println("Press enter to run update batch...");
        new Scanner(System.in).nextLine();

        //Update objects:
        john.setAge(1);
        jane.setAge(2);
        george.setAge(3);

        //Run an update batch:
        runUpdateBatch();

        //--------------------------------------------------------------------------------------------------------------
        //   DELETE BATCH

        System.out.println("Press enter to run delete batch...");
        new Scanner(System.in).nextLine();

        //Run a delete batch:
        runDeleteBatch();

        //--------------------------------------------------------------------------------------------------------------

    }

    private static void runCreateBatch() {
        Firestorm.runBatch(new FirestormBatch() {
            @Override
            public void execute() {
                create(john);
                create(jane);
                create(george);
            }

            @Override
            public void onSuccess() {
                System.out.println("Batch write successful - objects created.");
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("Batch write failed.");
            }
        });
    }

    private static void runUpdateBatch() {
        Firestorm.runBatch(new FirestormBatch() {
            @Override
            public void execute() {
                update(john);
                update(jane);
                update(george);
            }

            @Override
            public void onSuccess() {
                System.out.println("Batch update successful - objects updated.");
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("Batch update failed.");
            }
        });
    }

    private static void runDeleteBatch() {
        Firestorm.runBatch(new FirestormBatch() {
            @Override
            public void execute() {
                delete(john);
                delete(jane);
                delete(george);
            }

            @Override
            public void onSuccess() {
                System.out.println("Batch delete successful - objects deleted.");
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("Batch delete failed.");
            }
        });
    }

}
