package app;

import app.entities.User;
import app.utils.File;
import app.entities.Vehicle;

import java.util.*;

public class Client {
    public static void main(String args[]) throws Exception {

        HashMap<String, String> configurations = File.readJson("src/main/java/configurations/sql config.json");

        User user = new User(123, "David", "Yudin");
        Vehicle vehicle = new Vehicle("ABCD", "ZXCV", 2, 2021, "good car");

        ExecuteQuery.addOne(vehicle, Vehicle.class);

        List<Vehicle> vehicles = ExecuteQuery.readAll(Vehicle.class);
        vehicles.forEach(vehiclez -> System.out.println(vehiclez.toString()));

        Vehicle vehicless = ExecuteQuery.readById(1, Vehicle.class);
        System.out.println(vehicless.toString());
    }
}
