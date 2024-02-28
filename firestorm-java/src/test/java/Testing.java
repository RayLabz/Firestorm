import com.raylabz.firestorm.Firestorm;
import com.raylabz.firestorm.firestore.FS;
import com.raylabz.firestorm.rdb.RDB;
import com.raylabz.firestorm.util.FirebaseUtils;

import java.io.IOException;

public class Testing {

    public static void main(String[] args) {
        //Init Firestore:
        try {
            FirebaseUtils.initialize(ServiceAccount.SERVICE_ACCOUNT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        RDB.init("https://raylabz.firebaseio.com/");
        FS.init();
        Firestorm.register(Person.class);



    }

}
