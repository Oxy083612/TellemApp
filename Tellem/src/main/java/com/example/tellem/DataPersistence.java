package com.example.tellem;

import com.example.tellem.application.models.Project;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataPersistence {

    private static final String FILE_NAME = "data.json";

    public static void saveTabsToFile(List<Project> tabs) {
        try (Writer writer = new FileWriter(FILE_NAME)) {
            Gson gson = new Gson();
            gson.toJson(tabs, writer);
            System.out.println("Dane zapisane do pliku.");
        } catch (IOException e) {
            System.err.println("Błąd przy zapisie: " + e.getMessage());
        }
    }

    public static List<Project> loadTabsFromFile() {
        try (Reader reader = new FileReader(FILE_NAME)) {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Project>>() {}.getType();
            ArrayList<Project> tabs = gson.fromJson(reader, listType);
            System.out.println("Dane wczytane z pliku.");
            return tabs != null ? tabs : new ArrayList<>();
        } catch (FileNotFoundException e) {
            System.out.println("Brak pliku z danymi – zaczynamy od zera.");
            return new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Błąd przy odczycie: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
