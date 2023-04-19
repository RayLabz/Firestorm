//import static org.junit.Assert.*;
//
//import com.google.cloud.firestore.WriteResult;
//import com.raylabz.firestorm.Firestorm;
//import com.raylabz.firestorm.async.FSFuture;
//import com.raylabz.firestorm.firestore.FS;
//import com.raylabz.firestorm.util.FirebaseUtils;
//import org.checkerframework.checker.units.qual.A;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.UUID;
//import java.util.concurrent.TimeUnit;
//
//public class CreateTest {
//
////    assertEquals("This is the testcase in this class", str1);
//
//    @Before
//    public void init() {
//        //Init Firestore:
//        try {
//            FirebaseUtils.initialize(ServiceAccount.SERVICE_ACCOUNT);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        FS.init();
//        Firestorm.register(Person.class);
//        Firestorm.register(Student.class);
//    }
//
////    @After
////    public void tearDown() {
////        try {
////            FS.close();
////        } catch (Exception e) {
////            System.err.println(e.getMessage());
////        }
////    }
//
//    @Test
//    public void createOne_Sync() {
//        String id = UUID.randomUUID().toString();
//        Student student = new Student(id, 10, "TestName", 90);
//        FS.create(student).now();
//        assertEquals(FS.exists(Student.class, id).now(), true);
//    }
//
//    @Test
//    public void createOne_Async() {
//        String id = UUID.randomUUID().toString();
//        Student student = new Student(id, 10, "TestName", 90);
//        FS.create(student).then(result -> {
//            assertEquals(FS.exists(Student.class, id).now(), true);
//        }).run();
//    }
//
//    @Test
//    public void createOne_Wait() {
//        String id = UUID.randomUUID().toString();
//        Student student = new Student(id, 10, "TestName", 90);
//        FS.create(student).waitFor(30, TimeUnit.SECONDS, error -> {
//            System.err.println(error.getMessage());
//        });
//        assertEquals(FS.exists(Student.class, id).now(), true);
//    }
//
//    @Test
//    public void createOne_NullID() {
//        final ArrayList<Throwable> errors = new ArrayList<>();
//        Student student = new Student(null,10, "TestName", 90);
//        FS.create(student).waitFor(30, TimeUnit.SECONDS, errors::add);
//        assertFalse(errors.isEmpty());
//    }
//
//    @Test
//    public void createOne_EmptyID() {
//        final ArrayList<Throwable> errors = new ArrayList<>();
//        Student student = new Student("",10, "TestName", 90);
//        FS.create(student).waitFor(30, TimeUnit.SECONDS, errors::add);
//        assertFalse(errors.isEmpty());
//    }
//
//    @Test
//    public void createMany_Sync() {
//        ArrayList<Student> students = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            String id = UUID.randomUUID().toString();
//            Student student = new Student(id, i + 30, "TestName-" + i, i + 80);
//            students.add(student);
//        }
//        FS.create(students).now();
//
//        for (Student s : students) {
//            assertEquals(FS.exists(s).now(), true);
//        }
//    }
//
//    @Test
//    public void createMany_Async() {
//        ArrayList<Student> students = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            String id = UUID.randomUUID().toString();
//            Student student = new Student(id, i + 30, "TestName-" + i, i + 80);
//            students.add(student);
//        }
//        FS.create(students).then(result -> {
//            for (Student s : students) {
//                assertEquals(FS.exists(s).now(), true);
//            }
//        }).run();
//    }
//
//    @Test
//    public void createMany_Wait() {
//        ArrayList<Student> students = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            String id = UUID.randomUUID().toString();
//            Student student = new Student(id, i + 30, "TestName-" + i, i + 80);
//            students.add(student);
//        }
//        FS.create(students).waitFor(30, TimeUnit.SECONDS);
//
//        for (Student s : students) {
//            assertEquals(FS.exists(s).now(), true);
//        }
//    }
//
//    @Test
//    public void createMany_VarArgs() {
//        Student[] students = new Student[3];
//        for (int i = 0; i < 3; i++) {
//            String id = UUID.randomUUID().toString();
//            Student student = new Student(id, i + 30, "TestName-" + i, i + 80);
//            students[0] = student;
//        }
//        FS.create(students).waitFor(30, TimeUnit.SECONDS);
//
//        for (Student s : students) {
//            assertEquals(FS.exists(s).now(), true);
//        }
//    }
//
//}