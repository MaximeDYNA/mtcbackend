package com.catis.service;

import com.catis.model.Machine;
import com.catis.repository.MachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MachineService {
    @Autowired
    private MachineRepository mr;

    public List<Machine> findAllActive(){
        List<Machine> machines = new ArrayList<>();
        mr.findByActiveStatusTrue().forEach(machines::add);
        return machines;
    }

    public Machine save(Machine m){
        m = mr.save(m);
        return m;
    }

    public void deleteById(Long id){

        mr.deleteById(id);

    }
}
