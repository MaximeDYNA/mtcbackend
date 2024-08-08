package com.catis.control.services;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CapelecRapportImpl implements IRapportMachine {

    @Autowired public Environment env;

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
    //@Override
    public Map<String, String> start(String fileName) {
        try {
            File file = convertPdfToText("/static/"+fileName, "capelec");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            reader.lines().forEach(line -> {
                this.line = line;
                getIdsVehicule();
                getTestRipage();
                getTestSuspension();
                getTestFreinage();
            });
            reader.close();
            //file.delete();
            System.err.println("capelec : "+measure.toString());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return measure;
    }

    private void getIdsVehicule() {
        if (line.contains("Immatriculation"))
            idVehicules.put("imat", line.split(" ")[1].trim());

        if(line.contains("Fin")) {
            Pattern p = Pattern.compile("\\d{2}/\\d{2}/\\d{4}");
            Matcher matcher = p.matcher(line);
            String date = matcher.find() ? matcher.group() : "";
            idVehicules.put("date", date.trim());
        }
    }

    private void setMeasureInline(int currentInd, String code, Matcher matcher) {
        int index = 0;
        while (matcher.find()) {
            if(index==currentInd) measure.put(code, matcher.group().trim());
            index++;
        }
    }

    private void getTestRipage() {
        Pattern p = Pattern.compile("[+-]?\\d{1,2}(\\.\\d)?");
        Matcher matcher = p.matcher(line);
        if(line.contains("Ripage") && line.contains("E1"))
            setMeasureInline(1, "0401", matcher);

        if(line.contains("Ripage") && line.contains("E2"))
            setMeasureInline(1, "0402", matcher);

    }

    private void getTestFreinage() {
        Pattern p = Pattern.compile("\\d{1,3}");
        Matcher matcher = p.matcher(line);
        if(line.contains("Déséquilibre") && line.contains("E1"))
            setMeasureInline(1, "0434", matcher);

        if(line.contains("Déséquilibre") && line.contains("E2"))
            setMeasureInline(1, "0442", matcher);

        if(line.contains("Efficacité") && line.contains("service") && matcher.find())
            measure.put("0446", matcher.group().trim());

        if(line.contains("Stat") && line.contains("Efficacité") && matcher.find())
            measure.put("0465", matcher.group().trim());
    }

    private void getTestSuspension() {
        Pattern p = Pattern.compile("\\d{1,3}");
        Matcher matcher = p.matcher(line);
        if (line.contains("Dissymétrie") && line.contains("E1"))
            setMeasureInline(1, "0412", matcher);

        if (line.contains("Dissymétrie") && line.contains("E2"))
            setMeasureInline(1, "0415", matcher);
    }
}