import com.raylabz.firestorm.FS;
import com.raylabz.firestorm.util.FirebaseUtils;

import java.io.IOException;

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

        Person p = new Person("yo", 10, "Nicos");

        //------------------------------------------------------
        // ------------------ ASYNC RANDOM ID ------------------
        //------------------------------------------------------

//        FS.Create.Async.randomID(p, new ApiFutureCallback<WriteResult>() {
//            @Override
//            public void onFailure(Throwable throwable) {
//                System.err.println(throwable.getMessage());
//            }
//
//            @Override
//            public void onSuccess(WriteResult writeResult) {
//                System.out.println(writeResult.getUpdateTime());
//            }
//        });
//
//        System.out.println("First");

        Student s = new Student("a", 18, "Nicos", 10);

//        FS.create(s).then(result -> {
//            System.out.println(result.getUpdateTime());
//        }).onError(error -> {
//            System.err.println(error.getMessage());
//        }).run();

        FS.get(Student.class, "a").then(result -> {
            System.out.println(result);
        }).onError(error -> {
            System.err.println(error.getMessage());
        }).run();

//        Student a = FS.get(Student.class, "a").now();
//        System.out.println(a);

        System.out.println("hey");

        while (true) {}

    }

}
