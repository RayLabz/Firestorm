import static org.junit.Assert.*;

import com.google.cloud.firestore.WriteResult;
import com.raylabz.firestorm.Firestorm;
import com.raylabz.firestorm.async.FSFuture;
import com.raylabz.firestorm.firestore.FS;
import com.raylabz.firestorm.rdb.RDB;
import com.raylabz.firestorm.util.FirebaseUtils;
import org.checkerframework.checker.units.qual.A;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RDBTest {

    public static void main(String[] args) {
        //Init Firestore:
        try {
            FirebaseUtils.initialize(ServiceAccount.SERVICE_ACCOUNT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        RDB.init("https://raylabz.firebaseio.com/");
        Firestorm.register(Person.class);
        Firestorm.register(Student.class);


        String id = UUID.randomUUID().toString();
        Student student = new Student(id, 10, "TestName", 90);
        RDB.set(student).now();

//        ArrayList<Student> students = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            String id = UUID.randomUUID().toString();
//            Student student = new Student(id, 10 + i, "TestName" + i, 90 + i);
//            students.add(student);
//        }
//        RDB.set(students);

    }

}