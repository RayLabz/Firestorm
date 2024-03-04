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

        //Create filterable:
        FSFilterable<Person> filterable = FS.filter(Person.class)
                .whereEqualTo("age", 30);

        //Create listener:
        ListenerRegistration listener = FS.attachFilterableListener(filterable, new RealtimeUpdateCallback<List<FSObjectChange<Person>>>() {
            @Override
            public void onUpdate(List<FSObjectChange<Person>> data) {
                System.out.println(data); //TODO - Handle onUpdate()
            }

            @Override
            public void onError(Throwable t) {
                System.err.println(t.getMessage()); //TODO - Handle onError()
            }
        });


    }

}
