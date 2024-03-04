import com.google.cloud.firestore.ListenerRegistration;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.raylabz.firestorm.Firestorm;
import com.raylabz.firestorm.async.RealtimeUpdateCallback;
import com.raylabz.firestorm.firestore.*;
import com.raylabz.firestorm.rdb.OrderedRDBFilterable;
import com.raylabz.firestorm.rdb.RDB;
import com.raylabz.firestorm.rdb.RDBFilterable;
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
        OrderedRDBFilterable<Person> filterable = RDB.filter(Person.class)
                .whereEqualTo("age", 30);

        //Create listener:
        RDB.attachFilterableListener(filterable, new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //TODO - Implement
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //TODO - Implement
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //TODO - Implement
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                //TODO - Implement
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO - Implement
            }
        });

    }

}
