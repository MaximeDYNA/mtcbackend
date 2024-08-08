package com.catis.control.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;

@Service
public class SatelliteRapportImpl implements IRapportMachine {

    @Autowired public Environment env;

    @Override
    public Environment getEnv() {
        return env;
    }

    /**
     * This method start the process to retrieve measure result
     * in a machine rapport
     *
     * @Return void
     */
    @Override
    public Map<String, String> start(String fileName) throws Exception {
        File file = convertPdfToText("/static/"+fileName, "satelliteNgono");
        return null;
    }
}
