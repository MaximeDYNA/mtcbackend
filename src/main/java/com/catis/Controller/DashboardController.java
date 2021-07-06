package com.catis.Controller;

import com.catis.objectTemporaire.DaschBoardLogDTO;
import com.catis.objectTemporaire.DashboardData;
import com.catis.service.AuditService;
import com.catis.service.VenteService;
import com.catis.service.VisiteService;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

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
    public ResponseEntity<Object> getBusinessData() throws IllegalAccessException, InstantiationException, IOException, ClassNotFoundException {
        //as.getAllRevision();
        List<DaschBoardLogDTO> daschBoardLogDTOList = new ArrayList<>();
        List<DaschBoardLogDTO> daschBoardLogDTOListTrunqued = new ArrayList<>();
        for(Class<?> c : as.getModelClasses()){
            daschBoardLogDTOList.addAll(as.getRev(c));
        }
        Collections.sort(daschBoardLogDTOList, Comparator.comparing(DaschBoardLogDTO::getDate).reversed());
        daschBoardLogDTOListTrunqued = daschBoardLogDTOList.subList(0, 20);

        DashboardData dashboardData = new DashboardData();

        dashboardData.setDaschBoardLogDTOS(
                daschBoardLogDTOListTrunqued
        );
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
