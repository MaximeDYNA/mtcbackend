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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface IRapportMachine {

    Environment getEnv();

    /**
     * This method start the process to retrieve measure test
     * and verify this measure to measure receive by the gieglan
     * file
     *
     * @Return void
     */
    Map<String, String> start(String fileName) throws Exception;

    /**
     * Convert Pdf file in a text file
     *
     * @param path
     * @param filenameD
     * @return File
     * @throws Exception
     */
     default File convertPdfToText(String path, String filenameD) throws Exception {

        Class clazz = EuroSystMachineImpl.class;
        InputStream inputStream = clazz.getResourceAsStream(path);
        PDDocument pdDoc  = PDDocument.load(inputStream);
        PDFTextStripper pdfStripper = new PDFTextStripper();
        String parsedText = pdfStripper.getText(pdDoc);

        if (parsedText.trim().isEmpty())
            parsedText = extractTextFromScannedDocument(pdDoc);

        File tmpFile = File.createTempFile(filenameD, ".tmp");
        FileWriter writer = new FileWriter(tmpFile);

        writer.write(parsedText);
        writer.close();
        pdDoc.close();

        return tmpFile;
    }

    /**
     * Retrieve the text in a pdf file unreadable by convert pdf to image
     * file and extracting text in image with ocr
     *
     * @param document
     * @return String
     * @throws IOException
     * @throws TesseractException
     */
     default String extractTextFromScannedDocument(PDDocument document)
        throws IOException, TesseractException {

        PDFRenderer pdfRenderer = new PDFRenderer(document);
        StringBuilder out = new StringBuilder();

        ITesseract _tesseract = new Tesseract();
        String tesseractPath = getEnv().getProperty("tesseract.path-language");
        _tesseract.setDatapath(tesseractPath);

        for (int page = 0; page < document.getNumberOfPages(); page++) {
            BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);

            File temp = File.createTempFile("tempfile_" + page, ".png");
            ImageIO.write(bim, "png", temp);

            String result = _tesseract.doOCR(temp);
            out.append(result);

            temp.delete();
        }

        return out.toString();
    }
}
