package com.catis.controller;

import com.catis.model.entity.ConstructorModel;
import com.catis.model.entity.Machine;
import com.catis.model.entity.Organisation;
import com.catis.objectTemporaire.MachinePOJO;
import com.catis.repository.ConstructorModelRepo;
import com.catis.service.MachineService;
import com.catis.service.OrganisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/api/v1/admin/machines")
public class MachineController {
    @Autowired
    private OrganisationService os;
    @Autowired
    private MachineService ms;
    @Autowired
    private ConstructorModelRepo constructorModelRepo;


    @GetMapping
    public ResponseEntity<Object> getAll(){

        List<Machine> machines = ms.findAllActive();

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", machines);

    }

    @Transactional
    @PostMapping
    public ResponseEntity<Object> addMachine(@RequestBody MachinePOJO pojo){
        ConstructorModel cm = pojo.getConstructorModel() == null ? null : constructorModelRepo.findById(pojo.getConstructorModel().getId()).get();
        Organisation o = pojo.getOrganisationId() == null ? null : os.findByOrganisationId(pojo.getOrganisationId().getId());
        Machine machine = new Machine();

        machine.setIdMachine(pojo.getIdMachine());
        machine.setFabriquant(pojo.getFabriquant());
        machine.setModel(pojo.getModel());
        machine.setNumSerie(pojo.getNumSerie());
        machine.setConstructorModel(cm);
        machine.setOrganisation(o);

        machine = ms.save(machine);

        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", machine);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable UUID id){

        try{
            ms.deleteById(id);
            return ApiResponseHandler.generateResponse(HttpStatus.OK, true, "success", null);

        }
        catch (Exception e){
            e.printStackTrace();
            return ApiResponseHandler.generateResponse(HttpStatus.OK, false, "failed", null);
        }
    }

}
