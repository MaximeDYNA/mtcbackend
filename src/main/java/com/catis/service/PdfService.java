package com.catis.service;

import com.catis.Controller.configuration.SessionData;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class PdfService {

    private static final String PDF_RESOURCES = "/pdf-resources/";
    private VisiteService visiteService;
    private SpringTemplateEngine templateEngine;
    private HttpServletRequest request;

    @Autowired
    public PdfService(VisiteService visiteService, SpringTemplateEngine templateEngine, HttpServletRequest request) {
        this.visiteService = visiteService;
        this.templateEngine = templateEngine;
        this.request = request;
    }

    public File generatePdf() throws IOException, DocumentException {
        Context context = getContext();
        String html = loadAndFillTemplate(context);
        return renderPdf(html);
    }

    private File renderPdf(String html) throws IOException, DocumentException {
        File file = File.createTempFile("visites", ".pdf");
        OutputStream outputStream = new FileOutputStream(file);
        ITextRenderer renderer = new ITextRenderer(20f * 4f / 3f, 20);
        renderer.setDocumentFromString(html
        );
        renderer.layout();
        renderer.createPDF(outputStream);
        outputStream.close();
        file.deleteOnExit();
        return file;
    }

    private Context getContext() {
        Context context = new Context();
        context.setVariable("visites", visiteService.enCoursVisitListForContext(SessionData.getOrganisationId(request)));
        return context;
    }

    private String loadAndFillTemplate(Context context) {
        return templateEngine.process("visites", context);
    }


}

