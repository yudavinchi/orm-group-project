package app.entities;

public class Person {
    int id;
    String firstName;
    String lastName;
    String email;
    String gender;

    public Person(int id) {}

    public Person(int id, String firstName, String lastName, String email, String gender) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
    }
}
