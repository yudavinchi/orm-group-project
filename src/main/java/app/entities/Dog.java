package app.entities;

public class Dog {
    int id;
    int age;
    String name;
    String type;

    public Dog(int id, int age, String name, String type) {
        this.id = id;
        this.age = age;
        this.name = name;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "id=" + id +
                ", age=" + age +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
