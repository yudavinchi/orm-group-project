package app.utils;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

public class File {

    public static HashMap<String, String> readJson (String path) throws FileNotFoundException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

        Gson gson = new Gson();
        HashMap<String, String> json = gson.fromJson(bufferedReader, HashMap.class);
        return json;
    }
}
