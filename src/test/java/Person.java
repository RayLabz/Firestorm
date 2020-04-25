import com.google.cloud.firestore.annotation.Exclude;
import com.raylabz.firestorm.annotation.FirestormObject;
import com.raylabz.firestorm.annotation.ID;

@FirestormObject
public class Person {

    @ID private String id;
    private String firstName;
    private String lastName;
    private int age;
    private int ignoredField;

    public Person(String firstName, String lastName, int age, int ignoredField) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.ignoredField = ignoredField;
    }

    private Person() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Exclude
    public int getIgnoredField() {
        return ignoredField;
    }

    public void setIgnoredField(int ignoredField) {
        this.ignoredField = ignoredField;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", firstname='" + firstName + '\'' +
                ", lastname='" + lastName + '\'' +
                ", age=" + age +
                '}';
    }
}