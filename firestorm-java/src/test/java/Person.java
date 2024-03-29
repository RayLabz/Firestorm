import com.raylabz.firestorm.annotation.FirestormObject;
import com.raylabz.firestorm.annotation.ID;

@FirestormObject
public class Person {

    @ID
    private String id;
    private int age;
    private String name;

    private Person() {
    }

    public Person(String id, int age, String name) {
        this.id = id;
        this.age = age;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
