import com.raylabz.firestorm.FS;
import com.raylabz.firestorm.async.FailureCallback;
import com.raylabz.firestorm.util.FirebaseUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CreateTest {

    public static void main(String[] args) {

        //Init Firestore:
        try {
            FirebaseUtils.initialize(ServiceAccount.SERVICE_ACCOUNT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FS.init();
        FS.register(Person.class);
        FS.register(Student.class);



        System.out.println("printout");

        while (true) {}

    }

}
