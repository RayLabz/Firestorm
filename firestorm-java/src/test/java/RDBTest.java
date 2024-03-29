import com.google.cloud.firestore.DocumentReference;
import com.google.firebase.database.*;
import com.raylabz.firestorm.Firestorm;
import com.raylabz.firestorm.firestore.FS;
import com.raylabz.firestorm.firestore.FSTransaction;
import com.raylabz.firestorm.rdb.RDB;
import com.raylabz.firestorm.util.FirebaseUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

        DatabaseReference reference = RDB.getInstance().getReference("Student").child("0476c48c-288c-4034-9cd3-71931178188a");
        reference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Student value = mutableData.getValue(Student.class);
                System.out.println(value);
                if (value == null) {
                    mutableData.setValue(1);
                }
                else {
                    value.setGrade(88);
                    mutableData.setValue(value);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
//                System.out.println(databaseError.getMessage());
            }
        });


//        ArrayList<Student> students = new ArrayList<>();
//
//        for (int i = 0; i < 5; i++) {
//            String id = UUID.randomUUID().toString();
//            System.out.println(id);
//            Student student = new Student(id, 10 + i, "TestName" + i, 90 + i);
//            students.add(student);
//        }
//
//        RDB.set(students).now();
//
//        Thread.sleep(1000);

//        RDB.deleteAllOfType(Student.class).now();

//        RDB.list(Student.class).now();

//        long t = System.currentTimeMillis();
//        Student api = RDB.get(Student.class, "043b34d2-fc7c-4438-85fc-e3f32040fd02").now();
//        System.out.println(System.currentTimeMillis() - t + "A");
//
//        long[] t2 = new long[1];
//        t2[0] = System.currentTimeMillis();
//        DatabaseReference databaseReference = RDB.getRDB().getReference("Student");
//        Query query = databaseReference.orderByChild("grade");
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
//                for (DataSnapshot child : children) {
//                    System.out.println(child.getValue(Student.class).getName());
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

//        DatabaseReference reference = RDB.getRDB().getReference("Student");
//        reference.startAt(50, "age").startAt(50, "grade").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
//                for (DataSnapshot child : children) {
//                    System.out.println(child.getValue(Student.class));
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

//        RDB.filter(Student.class)
//                .whereLessThanOrEqualTo("name", "TestName2")
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        System.out.println("changed");
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });

//        for (Student student : students) {
//            System.out.println(student);
//        }


//        while(true);


//        List<Student> list1 = RDB.list(Student.class, 2).now();
//        System.out.println(list1.size());
//
//        List<Student> list2 = RDB.list(Student.class).now();
//        System.out.println(list2.size());
//
//        List<Student> list3 = RDB.list(Student.class, 10).now();
//        System.out.println(list3.size());

//        RDB.delete(students).now();

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

        while (true);

    }

}