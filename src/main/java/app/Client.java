package app;

import app.entities.Cat;
import app.entities.Dog;
import app.entities.Student;

public class Client {
    public static void main(String args[]) throws Exception {


//-----------------------------------------------------------------

        ExecuteQuery.deleteTable(Student.class);
        ExecuteQuery.createTableWithAnnotations(Student.class);

        Student student1 = new Student("Ramesh Fadatare");
        Student student2 = new Student("Oleg");
        Student student3 = new Student("Ben");

        System.out.println("==================================================");

        System.out.println(student1);
        System.out.println(student2);
        System.out.println(student3);
        ExecuteQuery.addItemWithAnnotations(student1);
        ExecuteQuery.addItemWithAnnotations(student2);
        ExecuteQuery.addItemWithAnnotations(student3);


        System.out.println("==================================================");
        System.out.println(student1);
        System.out.println(student2);
        System.out.println(student3);


        ExecuteQuery.deleteTable(Dog.class);
        ExecuteQuery.createTableWithAnnotations(Dog.class);

        Dog dog1 = new Dog(1,5,"a","A");
        Dog dog2 = new Dog(2,5,"b","B");
        Dog dog3 = new Dog(1,5,"c","C");
//
        ExecuteQuery.addItemWithAnnotations(dog1);
        ExecuteQuery.addItemWithAnnotations(dog2);
//        ExecuteQuery.addItemWithAnnotations(dog3);
        dog3.setId(3);
        ExecuteQuery.addItemWithAnnotations(dog3);


        ExecuteQuery.deleteTable(Cat.class);
        ExecuteQuery.createTableWithAnnotations(Cat.class);
        Cat cat1 = new Cat(1,1);
        Cat cat2 = new Cat(2,1);
        Cat cat3 = new Cat(3,2);

        ExecuteQuery.addItemWithAnnotations(cat1);
        ExecuteQuery.addItemWithAnnotations(cat2);
        ExecuteQuery.addItemWithAnnotations(cat3);


    }
}

