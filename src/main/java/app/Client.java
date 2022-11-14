package app;

import app.entities.User;
import app.utils.File;
import app.entities.Vehicle;

import java.util.*;

public class Client {
    public static void main(String args[]) throws Exception {

//        Vehicle vehicle = new Vehicle("aBc", "zXC", 321, 2019, "normal car");
//        Vehicle vehicle2 = new Vehicle("abcc", "zXC", 32111, 201999, "normal car 2");

//        ExecuteQuery.addOne(vehicle, Vehicle.class);
//        ExecuteQuery.addOne(vehicle2, Vehicle.class);

        List<Vehicle> vehicles = ExecuteQuery.readByProperty("year", "2019", Vehicle.class);

        for (Vehicle vehicle: vehicles){
            System.out.println(vehicle.toString());
        }
    }
}
