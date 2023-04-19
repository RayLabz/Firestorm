//import com.raylabz.firestorm.Firestorm;
//import com.raylabz.firestorm.async.RealtimeUpdateCallback;
//import com.raylabz.firestorm.firestore.FS;
//import com.raylabz.firestorm.firestore.FSFilterable;
//import com.raylabz.firestorm.firestore.FSObjectChange;
//import com.raylabz.firestorm.util.FirebaseUtils;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class Sample_DeleteMe {
//
//    static String id = null;
//
//    public static void main(String[] args) {
//
//        //Init Firestore:
//        try {
//            FirebaseUtils.initialize(ServiceAccount.SERVICE_ACCOUNT);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        FS.init();
//        Firestorm.register(Person.class);
//        Firestorm.register(Student.class);
//
//        ArrayList<Student> students = new ArrayList<>();
//
//        for (int i = 0; i < 15; i++) {
//            Student s = new Student("id_" + i, 20 + i, "Random guy " + i, i + 70);
//            students.add(s);
//        }
//
////        getPaginated();
//
////        FS.attachListener(students.get(0), new RealtimeUpdateCallback<>() {
////            @Override
////            public void onUpdate(Student object) {
////                System.out.println(object);
////            }
////
////            @Override
////            public void onError(Throwable t) {
////                System.err.println(t.getMessage());
////            }
////        });
//
////        FS.attachListener(Student.class, "id_0", new RealtimeUpdateCallback<Student>() {
////            @Override
////            public void onUpdate(Student object) {
////                System.out.println(object);
////            }
////
////            @Override
////            public void onError(Throwable t) {
////                System.err.println(t.getMessage());
////            }
////        });
//
////        FS.attachClassListener(Student.class, new RealtimeUpdateCallback<List<ObjectChange<Student>>>() {
////            @Override
////            public void onUpdate(List<ObjectChange<Student>> data) {
////                System.out.println(data);
////            }
////
////            @Override
////            public void onError(Throwable t) {
////                System.err.println(t.getMessage());
////            }
////        });
//
//        FSFilterable<Student> age = FS.filter(Student.class).whereGreaterThan("age", 23);
//        FS.attachFilterableListener(age, new RealtimeUpdateCallback<>() {
//            @Override
//            public void onUpdate(List<FSObjectChange<Student>> data) {
//                System.out.println(data);
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                System.err.println(t.getMessage());
//            }
//        });
//
//
//        while (true) {
//        }
//
//    }
//
////    static void getPaginated() {
////        Paginator.next(Student.class, id).fetch().then(result -> {
////            id = result.getLastDocumentID();
////            System.out.println(result.getItems().size());
////            new Scanner(System.in).nextLine();
////            if (result.hasItems()) {
////                getPaginated();
////            }
////            else {
////                System.out.println("Out of results");
////            }
////        }).run();
////    }
//
//}
