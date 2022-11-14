package app;

import app.entities.User;
import app.utils.File;
import app.entities.Vehicle;

import java.util.*;

public class Client {
    public static void main(String args[]) throws Exception {

        ExecuteQuery.updatePropertyById(2, "model", "hyundai", Vehicle.class);
    }
}
