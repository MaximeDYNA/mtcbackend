package com.catis.control.services;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EuroSystMachineImpl implements IRapportMachine {

    @Autowired public Environment env;

    private File file;

    private Map<String, String> idVehicules= new HashMap();

    private Map<String, String> measure = new HashMap<>();

    private String line;

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
            file = convertPdfToText("/static/"+fileName, "euroSyst");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            reader.lines().forEach(line -> {
                this.line = line;
                getIdsVehicule();
                getTestRipage();
                getTestSuspension();
                getTestFreinage();
            });

            reader.close();
            System.err.println("euro : "+ measure.toString());
            // file.delete();
        } catch (Exception e) {}

        return measure;
    }

    private void getIdsVehicule() {

        if (line.contains("Immatriculation")) {
            System.out.println(line.split(" ")[1]);
            idVehicules.put("imat", line.split(" ")[1]);
        }
        if(line.contains("Heure Date")) {
            Pattern p = Pattern.compile("\\d{2}\\.\\d{2}\\.\\d{4}");
            Matcher matcher = p.matcher(line);
            String date = matcher.find() ? matcher.group() : "";
            idVehicules.put("date", date);
        }
    }

    private void getTestRipage() {
        Pattern p = Pattern.compile("[+-]?\\d{1,2}(,\\d)?");
        Matcher matcher = p.matcher(line);
        if(line.contains("m/km") && line.contains("avant") && matcher.find())
            measure.put("0401", matcher.group());
        if(line.contains("m/km") && line.contains("arrière") && matcher.find())
            measure.put("0402", matcher.group());
    }

    private void getTestSuspension() {

        Pattern p = Pattern.compile("\\d{1,3} ");
        Matcher matcher = p.matcher(line);
        List<String> codeAv = Arrays.asList("0410", "0411", "0412");
        List<String> codeAr = Arrays.asList("0413", "0414", "0415");
        boolean isEssieuAv = line.contains("avant");
        int index = 0;
        while (line.contains("Mm") && line.contains("Essieu") && matcher.find() && index != codeAv.size()) {
            System.err.println("code :" + (isEssieuAv ? codeAv.get(index): codeAr.get(index)) +" : "+matcher.group());
            measure.put(isEssieuAv ? codeAv.get(index): codeAr.get(index), matcher.group());
            index++;
        }
    }

    private void getTestFreinage() {
        Pattern p = Pattern.compile("\\d{1,3} ");
        Matcher matcher = p.matcher(line);
        boolean isEssieu = (line.contains("avant") || line.contains("arrière")) && line.contains("Manuel");
        int index =0;
        while (index != 3 && isEssieu && matcher.find()) {
            if(index == 2) {
                System.err.println((line.contains("avant") ? "0434" : "0442")+"  : "+matcher.group());
                measure.put(line.contains("avant") ? "0434" : "0442", matcher.group());
            }

            index++;
        }

        index = 0;
        boolean isEff = (line.contains("station") || line.contains("service")) && line.contains("totales");
        while (index != 2 && isEff && matcher.find()) {
            if(index == 1) {
                System.err.println((line.contains("station") ? "0465" : "0446")+"  : "+matcher.group());
                measure.put(line.contains("station") ? "0465" : "0446", matcher.group());
            }

            index++;
        }
    }
}
