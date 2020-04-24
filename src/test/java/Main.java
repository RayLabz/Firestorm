import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.raylabz.firestorm.Firestorm;

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

        Tutor tutor = new Tutor("nicos", "kasenides", 27);
        final String id = Firestorm.create(tutor);
        System.out.println(id + " created!");

        new Scanner(System.in).nextLine();

        tutor.setAge(28);
        Firestorm.update(id, tutor);

        new Scanner(System.in).nextLine();

        Firestorm.delete(Tutor.class, id);

    }

}
