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

        FS.get(Student.class, "").now(error -> {
            //TODO
        });

//        for (int i = 0 ; i < 15; i++) {
//            Student s = new Student("id_" + i, 20 + i, "Random guy " + i, i + 70);
//            students.add(s);
//        }
//
//        List<WriteResult> now = FS.create(students).waitFor(1, TimeUnit.MINUTES);
//        System.out.println(now);

//        FS.create(new Student("a", 20, "a", 4), new Student("b", 21, "b", 53)).now();

        System.out.println("printout");

        while (true) {}

    }

}
