package app;

import app.entities.Dog;
import app.entities.TypeCheckClass;

import java.time.LocalDateTime;
import java.util.*;

public class Client {
    public static void main(String args[]) throws Exception {

//        List<Vehicle> vehicles = ExecuteQuery.readByProperty("company", "abcde", Vehicle.class);
//
//        for (Vehicle vehicle: vehicles){
//            System.out.println(vehicle.toString());
//        }

//        ExecuteQuery.createTable(Dog.class);

        Dog mydog  = new Dog(123, 5, "Bobby", "dog");
        Dog mydog2  = new Dog(124, 5, "Ronny", "dog");
        TypeCheckClass checkClass = new TypeCheckClass(true, (byte) 1, (short) 1,12,"stringblahblah",15000000000L,12.2F, 14.3, LocalDateTime.now(),'c', TypeCheckClass.Type.BLUE, mydog);
        TypeCheckClass checkClass1 = new TypeCheckClass(false, (byte) 1, (short) 1,12,"stringbl;kloop;ahblah",15000000000L,12.2F, 14.3, LocalDateTime.now(),'c', TypeCheckClass.Type.BLUE, mydog);

//        ExecuteQuery.addItem(mydog, Dog.class);

//        ExecuteQuery.createTable(Dog.class);
//        ExecuteQuery.createTable(TypeCheckClass.class);
//        ExecuteQuery.addItem(mydog2);
//        ExecuteQuery.addItem(mydog);
//        ExecuteQuery.addItem(checkClass);
//        ExecuteQuery.addItem(checkClass1);

        ExecuteQuery.deleteItemByProperty( Dog.class, "name", "Ronny");
//        ExecuteQuery.deleteItemByProperty( Dog.class, "age", "5");



//        ExecuteQuery.deleteTable(TypeCheckClass.class);
//        ExecuteQuery.deleteTable(Dog.class);
//        ExecuteQuery.createTable(TypeCheckClass.class);
//        System.out.println(checkClass);
        ExecuteQuery.addItem(checkClass);


    }
}
