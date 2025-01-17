package com.catis.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.catis.controller.message.Message;
import com.catis.model.entity.VersionLexique;
import com.catis.objectTemporaire.VersionLexiqueDTO;
import com.catis.service.VersionLexiqueService;

@RestController
@CrossOrigin
public class VersionController {

    @Autowired
    private VersionLexiqueService vls;

    @Transactional
    @GetMapping("/api/v1/admin/versionlists")
    public ResponseEntity<Object> versionList() {

        List<VersionLexiqueDTO> versionLexiqueDTOs = new ArrayList<>();
        VersionLexiqueDTO vlDTO;
        for (VersionLexique vl : vls.findAll()) {
            vlDTO = new VersionLexiqueDTO();
            vlDTO.setCreatedDate(vl.getCreatedDate());
            vlDTO.setModifiedDate(vl.getModifiedDate());
            vlDTO.setLibelle(vl.getLibelle());
            vlDTO.setId(vl.getId());
            vlDTO.setVersion(vl.getVersion());
            versionLexiqueDTOs.add(vlDTO);
        }


        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, Message.OK_LIST_VIEW + "version lexique", versionLexiqueDTOs);

    }

    @Transactional
    @GetMapping("/api/v1/admin/versionlists/{id}")
    public ResponseEntity<Object> getVersion(@PathVariable UUID id) {

        VersionLexiqueDTO versionLexiqueDTO = new VersionLexiqueDTO();
        VersionLexique dto = vls.findById(id);
        versionLexiqueDTO.setId(dto.getId());
        versionLexiqueDTO.setLibelle(dto.getLibelle());
        versionLexiqueDTO.setCreatedDate(dto.getCreatedDate());
        versionLexiqueDTO.setModifiedDate(dto.getModifiedDate());
        return ApiResponseHandler.generateResponse(HttpStatus.OK, true, Message.OK_LIST_VIEW + "version lexique", versionLexiqueDTO);

    }


}
