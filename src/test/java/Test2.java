import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.raylabz.firestorm.Firestorm;

import java.io.FileInputStream;

public class Test2 {

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

        Tutor t = new Tutor("tutor", "myTutor", 15, 15);
        Firestorm.create(t);

//        System.out.println(t.getId());

    }

}