package com.catis.controller.pdfhandler;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Component
public class PdfGenaratorUtil {

        @Autowired
        private TemplateEngine templateEngine;
        public void createPdf(String templateName, Context context) throws Exception {
            Assert.notNull(templateName, "The templateName can not be null");
            Context ctx = new Context();
            /*if (map != null) {
                Iterator itMap = map.entrySet().iterator();
                while (itMap.hasNext()) {
                    Map.Entry pair = (Map.Entry) itMap.next();
                    ctx.setVariable(pair.getKey().toString(), pair.getValue());
                }
            }*/

            String processedHtml = templateEngine.process(templateName, context);
            FileOutputStream os = null;
            String fileName = UUID.randomUUID().toString();
            try {
                final String outputFile = "C:/PV/452.pdf";
                os = new FileOutputStream(outputFile);

                ITextRenderer renderer = new ITextRenderer();
                renderer.setDocumentFromString(processedHtml);
                renderer.layout();
                renderer.createPDF(os, false);
                renderer.finishPDF();
                System.out.println("PDF created successfully");
            }
            finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) { /*ignore*/ }
                }
            }
        }
}

