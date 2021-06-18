package com.catis.Controller;

import com.catis.model.Machine;
import com.catis.objectTemporaire.MachinePOJO;
import com.catis.service.MachineService;
import com.catis.service.OrganisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/machines")
public class MachineController {
    @Autowired
    private OrganisationService os;
    @Autowired
    private MachineService ms;


    @GetMapping
    public ResponseEntity<Object> getAll(){

        List<Machine> machines = ms.findAllActive();

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", machines);

    }

    @PostMapping
    public ResponseEntity<Object> addMachine(MachinePOJO pojo){

        List<Machine> machines = ms.findAllActive();

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", machines);

    }

}
