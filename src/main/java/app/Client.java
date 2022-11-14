package app;

import app.entities.User;
import app.utils.File;
import app.entities.Vehicle;

import java.util.*;

public class Client {
    public static void main(String args[]) throws Exception {

        List<Vehicle> vehicles = ExecuteQuery.readByProperty("company", "abcde", Vehicle.class);

        for (Vehicle vehicle: vehicles){
            System.out.println(vehicle.toString());
        }
    }
}
