import static org.junit.Assert.*;

import com.google.cloud.firestore.WriteResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.raylabz.firestorm.Firestorm;
import com.raylabz.firestorm.async.FSFuture;
import com.raylabz.firestorm.exception.FirestormException;
import com.raylabz.firestorm.firestore.FS;
import com.raylabz.firestorm.rdb.RDB;
import com.raylabz.firestorm.util.FirebaseUtils;
import org.checkerframework.checker.units.qual.A;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RDBTest {

    public static void main(String[] args) {
        //Init Firestore:
        try {
            FirebaseUtils.initialize(ServiceAccount.SERVICE_ACCOUNT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        RDB.init("https://raylabz.firebaseio.com/");
        Firestorm.register(Person.class);
        Firestorm.register(Student.class);


//        String id = UUID.randomUUID().toString();
//        Student student = new Student(id, 10, "TestName", 90);
//        RDB.set(student).now();

        Student now = RDB.get(Student.class, "10717cc2-b7c1-4d1b-8039-18a61fd11fff").now();
        System.out.println(now);

        //TODO - Why does it not stop?!?!?! (The program hangs!!)


//        DatabaseReference reference = RDB.getRDB().getReference(Student.class.getSimpleName()).child("10717cc2-b7c1-4d1b-8039-18a61fd11fff");
//
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
////                if (dataSnapshot.exists()) {
//                Student value = dataSnapshot.getValue(Student.class);
//                System.out.println(value);
////                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                throw new FirestormException(databaseError.getMessage());
//            }
//        });

//        while (true) {}

//        ArrayList<Student> students = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            String id = UUID.randomUUID().toString();
//            Student student = new Student(id, 10 + i, "TestName" + i, 90 + i);
//            students.add(student);
//        }
//        RDB.set(students);

    }

}