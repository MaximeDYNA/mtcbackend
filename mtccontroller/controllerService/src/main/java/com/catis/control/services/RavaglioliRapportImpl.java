package com.catis.control.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;

@Service
public class RavaglioliRapportImpl implements IRapportMachine {

    @Autowired
    private Environment env;

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
    public Map<String, String> start(String fileName) throws Exception {
        File file = convertPdfToText("/static/"+fileName, "ravaglioli");
        return null;
    }
}
