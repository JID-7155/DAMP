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
import java.util.ArrayList;
import java.util.Iterator;

public class JSONService {
    private final JSONParser parser = new JSONParser();

    public JSONService() {
        //Leaving this here just in case.
    }

    private boolean isPlansPresent(Context context, String patientName) {
        String path = getPatientPlans(context, patientName);
        File file = new File(path);
        return file.exists();
    }

    private boolean isPatientInfoPresent(Context context, String patientName) {
        String path = getPatientInfo(context, patientName);
        File file = new File(path);
        return file.exists();
    }

    private String getPatientsDir(Context context) {
        return context.getFilesDir() + File.separator + "Patients";
    }

    private String getPatientDir(Context context, String patient) {
        return context.getFilesDir() + File.separator + "Patients" + File.separator + patient + File.separator;
    }

    private String getPatientPlans(Context context, String patientName) {
        return getPatientDir(context, patientName) + (patientName + "Plans.json");
    }

    private String getPatientInfo(Context context, String patientName) {
        return getPatientDir(context, patientName) + (patientName + "Info.json");
    }

    public String[] getPatientNames(Context context) {
        ArrayList<String> toReturn = new ArrayList<>();
        toReturn.add("Add New Patient");
        for(File file : (new File(getPatientsDir(context)).listFiles()))
        {
            toReturn.add(file.getName());
        }
        return toReturn.toArray(new String[toReturn.size()]);
    }

    @SuppressWarnings("unchecked")
    public void writeToPlans(Context context, String patientName, String newName, String newSteps, String newMedications, String newOthers) {
        try {
            JSONObject section = new JSONObject();

            section.put("Other Notes", newOthers);
            section.put("Medications", newMedications);
            section.put("Steps", newSteps);
            section.put("Name", newName);

            String fullFilePath = getPatientPlans(context, patientName);
            if (isPlansPresent(context, patientName)) {
                FileReader readMe = new FileReader(fullFilePath);
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
    public void writeToPatients(Context context, String patientName, String newName, String newAge, String newOtherInfo) {
        try {
            JSONObject section = new JSONObject();
            section.put("Other Info", newOtherInfo);
            section.put("Age", newAge);
            section.put("Name", newName);

            String fullFilePath = getPatientInfo(context, patientName);
            if (isPatientInfoPresent(context, patientName)) {
                FileReader readMe = new FileReader(fullFilePath);
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

    public void deletePlan(Context context, String patientName, int position) {
        try {
            String fullFilePath = getPatientPlans(context, patientName);
            if (isPlansPresent(context, patientName)) {
                FileReader readMe = new FileReader(fullFilePath);
                JSONArray currentJson = (JSONArray) parser.parse(readMe);
                currentJson.remove(position);

                FileWriter file = new FileWriter(fullFilePath, false);
                file.write(currentJson.toJSONString());

                file.close();
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

    public String[] getInternalPlanProperties(Context context, String patientName, String propertyName) {
        try {
            JSONArray myJson;
            FileReader file = new FileReader(getPatientPlans(context, patientName));
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