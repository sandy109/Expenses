package me.nijraj.expenses.utils;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import me.nijraj.expenses.models.Expense;
import me.nijraj.expenses.models.Person;

/**
 * Created by buddha on 12/31/17.
 */

public class Database {
    public static File export(Context context, File dir){
        JSONObject db = Database.toJSON();
        if(db != null){
            try {
                File outputFile = File.createTempFile("backup", ".json", dir);
                outputFile.setWritable(true);
                FileWriter writer = new FileWriter(outputFile);
                writer.write(Database.toJSON().toString());
                writer.flush();
                return outputFile;
            }catch (IOException e){
                return null;
            }
        }
        return null;
    }

    public static JSONObject toJSON(){
        JSONObject root = new JSONObject();
        JSONArray people = new JSONArray();
        JSONArray expenses = new JSONArray();
        for(Person e: Person.listAll(Person.class))
            people.put(e.toJSON());
        for(Expense e: Expense.listAll(Expense.class))
            expenses.put(e.toJSON());
        try {
            root.put("people", people);
            root.put("expenses", expenses);
        }catch (JSONException e){
            return null;
        }
        return root;
    }

    public static boolean fromJSON(JSONObject json) {
        try {
            JSONArray people = json.getJSONArray("people");
            JSONArray expenses = json.getJSONArray("expenses");
            ArrayList<Person> personArrayList = new ArrayList<>();
            ArrayList<Expense> expenseArrayList = new ArrayList<>();
            Database.clear();
            for(int i = 0; i < people.length(); i++) {
                personArrayList.add(Person.fromJSON(people.getJSONObject(i)));
            }
            for(int i = 0; i < expenses.length(); i++) {
                Expense e = Expense.fromJSON(expenses.getJSONObject(i));
                expenseArrayList.add(e);
            }
            for (Person p: personArrayList)
                p.saveRaw();
            for (Expense e: expenseArrayList)
                e.saveRaw();
        }catch (JSONException e){
            return false;
        }
        return true;
    }

    public static void clear() {
        Person.executeQuery("DELETE FROM SQLITE_SEQUENCE WHERE name = 'PERSON'");
        Person.deleteAll(Person.class);
        Expense.executeQuery("DELETE FROM SQLITE_SEQUENCE WHERE name = 'EXPENSE'");
        Expense.deleteAll(Expense.class);
    }
}
