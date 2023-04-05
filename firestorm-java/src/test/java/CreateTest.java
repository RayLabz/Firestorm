import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import com.raylabz.firestorm.*;
import com.raylabz.firestorm.async.RealtimeUpdateCallback;
import com.raylabz.firestorm.util.FirebaseUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class CreateTest {

    static String id = null;

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

        ArrayList<Student> students = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            Student s = new Student("id_" + i, 20 + i, "Random guy " + i, i + 70);
            students.add(s);
        }

//        getPaginated();

//        FS.attachListener(students.get(0), new RealtimeUpdateCallback<>() {
//            @Override
//            public void onUpdate(Student object) {
//                System.out.println(object);
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                System.err.println(t.getMessage());
//            }
//        });

//        FS.attachListener(Student.class, "id_0", new RealtimeUpdateCallback<Student>() {
//            @Override
//            public void onUpdate(Student object) {
//                System.out.println(object);
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                System.err.println(t.getMessage());
//            }
//        });

//        FS.attachClassListener(Student.class, new RealtimeUpdateCallback<List<ObjectChange<Student>>>() {
//            @Override
//            public void onUpdate(List<ObjectChange<Student>> data) {
//                System.out.println(data);
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                System.err.println(t.getMessage());
//            }
//        });

        FirestormFilterable<Student> age = FS.filter(Student.class).whereGreaterThan("age", 23);
        FS.attachFilterableListener(age, new RealtimeUpdateCallback<>() {
            @Override
            public void onUpdate(List<ObjectChange<Student>> data) {
                System.out.println(data);
            }

            @Override
            public void onError(Throwable t) {
                System.err.println(t.getMessage());
            }
        });


        while (true) {
        }

    }

//    static void getPaginated() {
//        Paginator.next(Student.class, id).fetch().then(result -> {
//            id = result.getLastDocumentID();
//            System.out.println(result.getItems().size());
//            new Scanner(System.in).nextLine();
//            if (result.hasItems()) {
//                getPaginated();
//            }
//            else {
//                System.out.println("Out of results");
//            }
//        }).run();
//    }

}
