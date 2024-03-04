import com.google.cloud.firestore.ListenerRegistration;
import com.raylabz.firestorm.Firestorm;
import com.raylabz.firestorm.async.RealtimeUpdateCallback;
import com.raylabz.firestorm.firestore.*;
import com.raylabz.firestorm.rdb.RDB;
import com.raylabz.firestorm.util.FirebaseUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Testing {

    public static void main(String[] args) {
        //Init Firestore:
        try {
            FirebaseUtils.initialize(ServiceAccount.SERVICE_ACCOUNT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        RDB.init("https://raylabz.firebaseio.com/");
        FS.init();
        Firestorm.register(Person.class);
        Firestorm.register(Student.class);
        Firestorm.register(Book.class);


    }

}
