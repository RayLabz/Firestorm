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
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RDBTest {

    public static void main(String[] args) throws InterruptedException {
        //Init Firestore:
        try {
            FirebaseUtils.initialize(ServiceAccount.SERVICE_ACCOUNT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        RDB.init("https://raylabz.firebaseio.com/");
        Firestorm.register(Person.class);
        Firestorm.register(Student.class);

//        ArrayList<Student> students = new ArrayList<>();

//        for (int i = 0; i < 5; i++) {
//            String id = UUID.randomUUID().toString();
//            System.out.println(id);
//            Student student = new Student(id, 10 + i, "TestName" + i, 90 + i);
//            students.add(student);
//        }
//
//        RDB.set(students).now();

//        List<Student> students = RDB.get(Student.class,
//                "6298dac8-3e25-4169-a1a7-84b2b176f8a1",
//                "f3305f22-514f-4b5b-b741-9dbfe11c28d5",
//                "5c9dae6b-4cb1-4b49-8952-1d977ccadaaa",
//                "f59b7b88-03ee-4b23-8122-f20f2d857a4c",
//                "a30ca755-7033-43e4-a13d-0c44f4006041"
//        ).now();
//
//        System.out.println(students);

//        Boolean exists = RDB.exists(Student.class, "60098dac8-3e25-4169-a1a7-84b2b176f8a1").now();
//        System.out.println(exists);

//        Student now = RDB.get(Student.class, "f3305f22-514f-4b5b-b741-9dbfe11c28d5").now();
//        System.out.println(now);

//        RDB.delete(Student.class, "f59b7b88-03ee-4b23-8122-f20f2d857a4c").now();


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

        //        while (true);

    }

}