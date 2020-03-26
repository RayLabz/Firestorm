import com.raylabz.firestorm.FirestormObject;

public class Person extends FirestormObject {

    private String firstname;
    private String lastname;
    private int age;

    public Person(String firstname, String lastname, int age) {
        super();
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
    }

    public Person() { }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
