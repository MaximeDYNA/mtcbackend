package com.catis.Controller;

import com.catis.model.Visite;
import com.catis.objectTemporaire.DashboardData;
import com.catis.service.AuditService;
import com.catis.service.OrganisationService;
import com.catis.service.VenteService;
import com.catis.service.VisiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/dashboard")
public class DashboardController {
    @Autowired
    private VisiteService vs;
    @Autowired
    private VenteService venteService;
    @Autowired
    private AuditService as;

    @GetMapping("/business")
    public ResponseEntity<Object> getBusinessData() throws IllegalAccessException, NoSuchFieldException {
        //as.getAllRevision();
       vs.getRev();
        DashboardData dashboardData = new DashboardData();

        dashboardData.setVisitNumber(
                vs.getVisitsOfTheDay()
        );
        dashboardData.setCa(
                venteService.getTodayCA()
        );
        dashboardData.setTax(
                venteService.TaxeOfTheDay()
        );
        dashboardData.setCaGraph(
                venteService.caGraphWeek()
        );

        dashboardData.setOrganisationTopDTOS(
                vs.getTopOrganisation()
        );

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "succès", dashboardData);
    }
}
