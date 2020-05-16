import com.raylabz.firestorm.annotation.FirestormObject;

@FirestormObject
public class Tutor extends Person {

    public Tutor(String firstName, String lastName, int age, int ignoredField) {
        super(firstName, lastName, age, ignoredField);
    }

    public Tutor() {
        super();
    }

}
