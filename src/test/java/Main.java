import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.raylabz.firestorm.Firestorm;
import com.raylabz.firestorm.OnObjectUpdateListener;
import com.raylabz.firestorm.OnFailureListener;

import java.io.FileInputStream;
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

        System.out.println("Press to create");
        new Scanner(System.in).nextLine();

        Tutor tutor = new Tutor("nicos", "kasenides", 27);
        final String id = Firestorm.create(tutor, new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
        System.out.println(tutor.getId() + " created!");

        System.out.println("Press to update");
        new Scanner(System.in).nextLine();

        tutor.setAge(28);
        Firestorm.update(tutor);

        System.out.println("Press to attach listener");
        new Scanner(System.in).nextLine();

        Firestorm.attachListener(new OnObjectUpdateListener(tutor) {
            @Override
            public void onSuccess() {
                System.out.println(tutor);
            }

            @Override
            public void onFailure(String failureMessage) {
                System.err.println(failureMessage);
            }
        });
        System.out.println("Listener attached");

        System.out.println("Press to get");
        new Scanner(System.in).nextLine();

        final Tutor tutor1 = Firestorm.get(Tutor.class, id);
        System.out.println(tutor1);

        System.out.println("Press to delete");
        new Scanner(System.in).nextLine();

        Firestorm.delete(Tutor.class, id);

    }

}
