import com.raylabz.firestorm.FS;
import com.raylabz.firestorm.async.FailureCallback;
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


        List<Student> students = FS.getMany(Student.class, "id_0", "id_1").now();

        ArrayList<String> ids = new ArrayList<>();
        ids.add("id_0");
        ids.add("id_1");
        List<Student> students = FS.getMany(Student.class, ids).now();

        System.out.println(students);

        List<Student> students1 = FS.getMany(Student.class, "id_0", "id_1").waitFor(1, TimeUnit.MINUTES);

        FS.getMany(Student.class, "id_0", "id_1").then(result -> {
            System.out.println(result);
        }).onError(error -> {
            System.err.println(error);
        }).run();

        FS.getMany(Student.class, ids).then(result -> {
            System.out.println(result);
        }).onError(error -> {
            System.err.println(error);
        }).run();

        System.out.println("printout");

        while (true) {}

    }

}
