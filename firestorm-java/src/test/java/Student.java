import com.raylabz.firestorm.annotation.FirestormObject;

@FirestormObject
public class Student extends Person {

    private int grade;

    private Student() {
        super("", 0, "");
    }

    public Student(String id, int age, String name, int grade) {
        super(id, age, name);
        this.grade = grade;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Student{" +
                "grade=" + grade +
                "} " + super.toString();
    }
}
