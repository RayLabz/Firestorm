import com.google.cloud.firestore.annotation.Exclude;
import com.raylabz.firestorm.FirestormObject;

public class Person extends FirestormObject {

    private String firstName;
    private String lastName;
    private int age;
    private String excludedField;

    public Person(String firstName, String lastName, int age, String excludedField) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.excludedField = excludedField;
    }

    public Person() { }

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

    @Exclude
    public String getExcludedField() {
        return excludedField;
    }

    public void setExcludedField(String excludedField) {
        this.excludedField = excludedField;
    }

}
