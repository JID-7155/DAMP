package com.ad_revenue.damp.Services;

import android.content.Context;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

public class JSONService {
    private final JSONParser parser = new JSONParser();

    public JSONService() {
        //Leaving this here just in case.
    }

    private boolean isFilePresent(Context context, String fileName) {
        String path = context.getFilesDir() + File.separator + fileName;
        File file = new File(path);
        return file.exists();
    }

    @SuppressWarnings("unchecked")
    public void writeToPlans(Context context, String newName, String newSteps, String newMedications, String newOthers) {
        try {
            JSONObject section = new JSONObject();

            section.put("Other Notes", newOthers);
            section.put("Medications", newMedications);
            section.put("Steps", newSteps);
            section.put("Name", newName);

            String fullFilePath = context.getFilesDir() + File.separator + "plans.json";
            if (isFilePresent(context, "plans.json")) {
                FileReader readMe = new FileReader(context.getFilesDir() + File.separator + "plans.json");
                JSONArray currentJson = (JSONArray) parser.parse(readMe);
                if (!currentJson.contains(section)) {
                    FileWriter file = new FileWriter(fullFilePath, false);
                    currentJson.add(section);
                    file.write(currentJson.toJSONString());
                    file.close();
                }
                readMe.close();
            } else {
                FileWriter file = new FileWriter(fullFilePath, false);
                JSONArray myArray = new JSONArray();
                myArray.add(section);
                file.write(myArray.toJSONString());
                file.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading/writing to file.");
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Error reading file.");
        }
    }

    @SuppressWarnings("unchecked")
    public void writeToPatients(Context context, String newName, String newAge, String newOtherInfo) {
        try {
            JSONObject section = new JSONObject();
            section.put("Other Info", newOtherInfo);
            section.put("Age", newAge);
            section.put("Name", newName);

            String fullFilePath = context.getFilesDir() + File.separator + "patients.json";
            if (isFilePresent(context, "patients.json")) {
                FileReader readMe = new FileReader(context.getFilesDir() + File.separator + "patients.json");
                JSONArray currentJson = (JSONArray) parser.parse(readMe);
                if (!currentJson.contains(section)) {
                    FileWriter file = new FileWriter(fullFilePath, false);
                    currentJson.add(section);
                    file.write(currentJson.toJSONString());
                    file.close();
                }
                readMe.close();
            } else {
                FileWriter file = new FileWriter(fullFilePath, false);
                JSONArray myArray = new JSONArray();
                myArray.add(section);
                file.write(myArray.toJSONString());
                file.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading/writing to file.");
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Error reading file.");
        }
    }

    public String[] getAllProperties(Context context, int index) {
        String[] toReturn = new String[4];

        try {
            InputStream stream = context.getAssets().open("conditions.json");
            InputStreamReader reader = new InputStreamReader(stream);
            JSONArray myJson = (JSONArray) parser.parse(reader);

            toReturn[0] = getProperty("Name", myJson.get(index));
            toReturn[1] = getProperty("Steps", myJson.get(index));
            toReturn[2] = getProperty("Medications", myJson.get(index));
            toReturn[3] = getProperty("Other Notes", myJson.get(index));

            return toReturn;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Error parsing file.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading/writing to file.");
        }
        return new String[1];
    }

    private String getProperty(String propertyName, Object sectionName) {
        JSONObject json = (JSONObject) sectionName;
        return (String) json.get(propertyName);
    }

    public void deleteSection(Context context, int position) {
        try {


            String fullFilePath = context.getFilesDir() + File.separator + "plans.json";
            if(isFilePresent(context, "plans.json")) {
                FileReader readMe = new FileReader(context.getFilesDir() + File.separator + "plans.json");
                JSONArray currentJson = (JSONArray) parser.parse(readMe);

                currentJson.remove(position);
                readMe.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading/writing to file.");
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Error reading file.");
        }
    }

    //Reads in conditions from assets folder. Do not delete.
    public String[] getProperties(Context context, String fileName, String propertyName) {
        try {
            InputStream stream = context.getAssets().open(fileName);
            InputStreamReader reader = new InputStreamReader(stream);
            JSONArray myJson = (JSONArray) parser.parse(reader);

            String[] toReturn = new String[myJson.size()];
            Iterator jsonIterator = myJson.iterator();
            int count = 0;

            while (jsonIterator.hasNext()) {
                toReturn[count] = getProperty(propertyName, jsonIterator.next());
                count++;
            }
            return toReturn;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Error parsing file.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading/writing to file.");
        }
        return new String[1];
    }

    public String[] getInternalProperties(Context context, String fileName, String propertyName) {
        try {
            JSONArray myJson;
            FileReader file = new FileReader(context.getFilesDir() + File.separator + fileName);
            myJson = (JSONArray) parser.parse(file);
            String[] toReturn = new String[myJson.size()];
            Iterator jsonIterator = myJson.iterator();
            int count = 0;
            file.close();

            while (jsonIterator.hasNext()) {
                toReturn[count] = getProperty(propertyName, jsonIterator.next());
                count++;
            }
            return toReturn;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Error parsing file.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading/writing to file.");
        }
        return new String[1];
    }
}