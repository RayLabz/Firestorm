import com.google.cloud.firestore.WriteResult;
import com.raylabz.firestorm.FS;
import com.raylabz.firestorm.FirestormBatch;
import com.raylabz.firestorm.FirestormTransaction;
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

        ArrayList<Student> students = new ArrayList<>();

        for (int i = 0 ; i < 15; i++) {
            Student s = new Student("id_" + i, 20 + i, "Random guy " + i, i + 70);
            students.add(s);
        }

//        Student s = new Student("a", 20, "a", 4);

//        FS.runBatch(new FirestormBatch() {
//            @Override
//            public void execute() {
//                create(students.get(0));
//                create(students.get(1));
//                create(students.get(2));
//                delete(students.get(1));
//            }
//        }).then(result -> {
//            System.out.println(result);
//        }).onError(error -> {
//            System.out.println(error.getMessage());
//        }).run();

//        FS.deleteType(Student.class).then(result -> {
//            //TODO - Success
//        }).onError(error -> {
//            //TODO - Error
//        }).run();

        FS.runTransaction(new FirestormTransaction() {
            @Override
            public void execute() {
                ArrayList<Student> list = list(Student.class);
                System.out.println(list);
                Student student = get(Student.class, students.get(0).getId());
                System.out.println(student);
//                create(students.get(1));
            }
        }).then(result -> {
            System.out.println(result);
        }).onError(error -> {
            System.err.println(error.getMessage());
        }).run();

        System.out.println("hi");


        while (true) {}

    }

}
