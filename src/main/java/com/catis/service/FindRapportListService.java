package com.catis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class FindRapportListService {

    @Autowired
    private Environment env;

    private List<String> result = new ArrayList<>();

    public List<String> FilenameListInFolder() {

        File filePath = new File(env.getProperty("rapport.machine.directory"));
        File[] listingAllFiles = filePath.listFiles();
        iterateOverFiles(listingAllFiles);

        return result;
    }

    private void iterateOverFiles(File[] files) {
        File fileLocation = null;
        for (File file : files) {
            if (!file.isDirectory()) {
                fileLocation = findFileswithTxtExtension(file);
                if(fileLocation != null) {
                    result.add(fileLocation.getName());
                }
            }
        }
    }

    private File findFileswithTxtExtension(File file) {

        return file.getName().toLowerCase().endsWith("pdf")
            ? file
            : null;
    }
}
