package app.orm.entities;

public class Person {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;

    public Person(int id) {}

    public Person(int id, String firstName, String lastName, String email, String gender) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
    }
}
