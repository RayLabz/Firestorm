import com.google.cloud.firestore.WriteResult;
import com.raylabz.firestorm.FS;
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

//        List<WriteResult> now = FS.update(students).waitFor(1, TimeUnit.MINUTES);
//        System.out.println(now);
//
//        FS.update(students).then(result -> {
//            System.out.println(result.size());
//        }).onError(error ->  {
//            System.out.println(error.getMessage());
//        }).run();

        Student s = new Student("a", 20, "a", 4);

//        Student s = new Student("a", 65, "hi", 99);
//        FS.update(s).then(result -> {
//            System.out.println(result.getUpdateTime());
//        }).onError(error -> {
//            System.err.println(error.getMessage());
//        }).run();

        FS.delete(students).then(result -> {
            System.out.println(result.size());
        }).onError(error -> {
            System.err.println(error.getMessage());
        }).run();

        System.out.println("printout");

        while (true) {}

    }

}
