package app.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Student {


    @AutoIncrementedId
    private long id2;
    private String name;

    public Student(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Student() {
    }

    public void setId(long id) {
        this.id2 = id;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id2 +
                ", name='" + name + '\'' +
                '}';
    }
}
