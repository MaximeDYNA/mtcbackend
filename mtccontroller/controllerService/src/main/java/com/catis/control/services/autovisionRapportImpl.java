package com.catis.control.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class autovisionRapportImpl implements IRapportMachine {

    @Autowired
    private Environment env;

    private Map<String, String> idVehicules= new HashMap();

    private Map<String, String> measure = new HashMap<>();

    private String line;

    private int indexFrein, indexSusp =0;

    @Override
    public Environment getEnv() {
        return env;
    }

    /**
     * This method start the process to retrieve measure test
     * and verify this measure to measure receive by the gieglan
     * file
     *
     * @Return void
     */
    @Override
    public Map<String, String> start(String fileName) {
        try {
            File file = convertPdfToText("/static/"+fileName, "autovision");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            reader.lines().forEach(line -> {
                this.line = line;
                getIdsVehicule();
                getTestRipage();
                getTestSuspension();
                getTestFreinage();
            });
            reader.close();
            file.delete();
            System.err.println("autovision : "+measure.toString());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return measure;
    }

    private void getIdsVehicule() {
        if (line.contains("immatriculation"))
            idVehicules.put("imat", line.split(" ")[3].trim());

        if(line.contains("DATE")) {
            Pattern p = Pattern.compile("\\d{2}/\\d{2}/\\d{4}");
            Matcher matcher = p.matcher(line);
            String date = matcher.find() ? matcher.group() : "";
            idVehicules.put("date", date.trim());
        }
    }

    private void getTestRipage() {
        Pattern p = Pattern.compile("[+-]?\\d{1,2}(\\.\\d)?");
        Matcher matcher = p.matcher(line);
        int index = 0;
        while (matcher.find() && line.contains("m/km") && line.contains("RIPAGE")) {
            measure.put(index == 0 ? "0401": "0402", matcher.group().trim());
            index++;
        }
    }

    private void getTestFreinage() {
        Pattern p = Pattern.compile("\\d{1,3} ");
        Matcher matcher = p.matcher(line);
        if (line.contains("SIMULTANE") || indexFrein != 0) indexFrein++;
        if(indexFrein==6 && matcher.find())
            measure.put("0434", matcher.group().trim());
        if(line.contains("déséquilibre essieu") && matcher.find())
            measure.put("0442", matcher.group().trim());
        else if(line.contains("efficacité véhicule") && matcher.find()) {
            measure.put(indexFrein <= 10 ? "0465": "0446", matcher.group().trim());
            indexFrein = 0;
        }
    }

    private void getTestSuspension() {
        Pattern p = Pattern.compile("\\d{1,3}");
        Matcher matcher = p.matcher(line);
        if (line.contains("SUSPENSION") || indexSusp != 0) indexSusp++;
        if(indexSusp == 2 && matcher.find()) {
            measure.put("0412", matcher.group().trim());
        } else if(line.contains("dissymétrie") && matcher.find())
            measure.put("0415", matcher.group().trim());
    }
}
