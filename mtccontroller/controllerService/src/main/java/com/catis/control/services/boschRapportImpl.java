package com.catis.control.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class boschRapportImpl implements IRapportMachine {

    @Autowired
    private Environment env;

    private File file;

    private Map<String, String> idVehicules= new HashMap();

    private Map<String, String> measure = new HashMap<>();

    private String line;

    private int indexRip, indexSusp =0;

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
            File file = convertPdfToText("/static/"+fileName, "bosch");
            System.out.println(file.getAbsolutePath());
            BufferedReader reader = new BufferedReader(new FileReader(file));
            reader.lines().forEach(line -> {
                this.line = line;
                getIdsVehicule();
                getTestRipage();
                System.out.println(line);
                getTestSuspension();
                getTestFreinage();
            });
            reader.close();
            //file.delete();
            System.err.println("bosch : "+measure.toString());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        System.err.println(measure.toString());
        return measure;
    }

    private void getIdsVehicule() {
        if (line.contains("immatriculation")){
            idVehicules.put("imat", line.split(" ")[2].trim());
            System.err.println(line);
        }

        if(line.contains("Démarrage")) {
            Pattern p = Pattern.compile("\\d{2}\\.\\d{2}\\.\\d{4}");
            Matcher matcher = p.matcher(line);
            String date = matcher.find() ? matcher.group() : "";
            System.err.println("date : "+ date.trim() );
            idVehicules.put("date", date.trim());
        }
    }

    private void getTestRipage() {
        Pattern p = Pattern.compile("[+-]?\\d{1,2}(\\.\\d)?");
        Matcher matcher = p.matcher(line);
        if(line.contains("mm/m") && !line.contains("...") && indexRip == 0 && matcher.find()) {
            measure.put("0401", matcher.group().trim());
            indexRip++;
        } else if(line.contains("mm/m") && ! line.contains("...") && matcher.find())
            measure.put("0402", matcher.group().trim());
    }

    private void getTestFreinage() {

        Pattern p = Pattern.compile("\\d{1,3} ");
        Matcher matcher = p.matcher(line);
        if(line.contains("Différence") && line.contains("service") && matcher.find())
            measure.put("0434", matcher.group().trim());
        else if(line.contains("Différence") && line.contains("stationnement") && matcher.find())
            measure.put("0442", matcher.group().trim());
        else if(line.contains("Efficacité") && line.contains("service") && matcher.find())
            measure.put("0446", matcher.group().trim());
        else if(line.contains("Efficacité") && line.contains("main") && matcher.find())
            measure.put("0465", matcher.group().trim());
    }

    private void getTestSuspension() {
        Pattern p = Pattern.compile("\\d{1,3}");
        Matcher matcher = p.matcher(line);
        if (line.contains("Test de suspension")) indexSusp++;
        if(line.contains("=%") && indexSusp == 1 && matcher.find()) {
            measure.put("0412", matcher.group().trim());
            indexSusp++;
        } else if(line.contains("=%") && indexSusp == 2 && matcher.find())
            measure.put("0415", matcher.group().trim());
    }
}
