package app;

import app.entities.Vehicle;

public class Client {
    public static void main(String args[]) throws Exception {

        Vehicle vehicle = ExecuteQuery.readById(100, Vehicle.class);
        System.out.println(vehicle.toString());

        vehicle.setYear(2002);
        vehicle.setComment("bad");

        ExecuteQuery.updateEntireItemById(100, vehicle, Vehicle.class);
    }
}
