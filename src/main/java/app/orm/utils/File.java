package app.orm.utils;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

public class File {
    public static HashMap<String, String> readJson(String path) {

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println(fileNotFoundException);
        }

        Gson gson = new Gson();
        return gson.fromJson(bufferedReader, HashMap.class);
    }
}
