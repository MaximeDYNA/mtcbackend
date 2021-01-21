package com.catis.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.catis.Controller.message.Message;
import com.catis.model.Client;
import com.catis.model.Lexique;
import com.catis.model.VersionLexique;
import com.catis.objectTemporaire.LexiqueChildDTO;
import com.catis.objectTemporaire.LexiqueDTO;
import com.catis.objectTemporaire.LexiquePOJO;
import com.catis.objectTemporaire.LexiqueReceived;
import com.catis.service.CategorieVehiculeService;
import com.catis.service.ClientService;
import com.catis.service.LexiqueService;
import com.catis.service.VersionLexiqueService;

@RestController
@CrossOrigin
public class LexiqueController {

	private VersionLexiqueService versionLexiqueService;
	private LexiqueService lexiqueService;
	private ClientService clientService;
	private CategorieVehiculeService categorieVehiculeService;
	
	@Autowired
	public LexiqueController(LexiqueService lexiqueService, VersionLexiqueService versionLexiqueService, ClientService clientService, CategorieVehiculeService categorieVehiculeService) {
		this.lexiqueService = lexiqueService;
		this.versionLexiqueService = versionLexiqueService;
		this.clientService = clientService;
		this.categorieVehiculeService = categorieVehiculeService;
	}
	
	
	@PostMapping(value="/api/v1/lexique")
	@Transactional
	public ResponseEntity<Object> ajouterLexique(@RequestBody LexiqueReceived lexique){
		/**Version lexique***/
			
			VersionLexique vl = new VersionLexique();
			vl.setId(lexique.getId()==null? null : lexique.getId());
			
			vl.setLibelle(lexique.getNom());			
			vl.setVersion(lexique.getVersion());			
			vl = versionLexiqueService.add(vl);
			
		/********------******/
		List<Lexique> lexiques = new ArrayList<>();
		Lexique lexiq;
		
		for(LexiquePOJO l : lexique.getRows()) {
			lexiq = new Lexique();
			lexiq.setId(l.getId()==null? null : l.getId());
			lexiq.setCode(l.getCode().replace("\"", ""));
			lexiq.setLibelle(l.getLibelle());
			
			lexiq.setParent(lexiqueService.findByCode(l.getParent()==null?l.getParent():l.getParent().replace("\"", "")));
			if( l.getHaschild().equals("TRUE") || l.getHaschild().equals("true")) {
				lexiq.setHaschild(true);
			}
			else
				lexiq.setHaschild(false);
			lexiq.setVersionLexique(vl);
			lexiq.setVisuel(Boolean.valueOf(l.getVisual()));
			lexiq.setClients(lexiqueService.findByVersionLexique(versionLexiqueId).);
			//System.out.println("Nom :"+ lexique.getId());
			lexiq.setCategorieVehicule(categorieVehiculeService.findById(Long.valueOf(l.getCategoryId()) ));
			lexiqueService.add(lexiq);
		}
		vl = versionLexiqueService.findById(vl.getId());
		
		
		
		return ApiResponseHandler.generateResponse(HttpStatus.OK, true, Message.OK_ADD + "Lexique", vl );
	}
	
	@GetMapping(value="/api/v1/lexiques/{id}")
	public ResponseEntity<Object> getLexiquesForUpdate(@PathVariable Long id){
		
		LexiqueReceived lr = new LexiqueReceived();
		List<Long> ids;
		List<LexiquePOJO> list = new ArrayList<>();
		LexiquePOJO pojo;
		for(Lexique l: lexiqueService.findByVersionLexique(id)) {
			pojo = new LexiquePOJO();
			pojo.setId(l.getId());
			pojo.setCode(l.getCode());
			pojo.setLibelle(l.getLibelle());
			pojo.setParent(l.getParent()==null? null : l.getParent().getCode());
			pojo.setVisual(l.getVisuel().toString());
			pojo.setHaschild(l.getHaschild().toString());
			pojo.setCategoryId(l.getCategorieVehicule().getId().intValue());
			ids = new ArrayList<>();
			for(Client i :l.getClients()) {
				ids.add(i.getClientId());
			}
			pojo.setClientId(ids);
			pojo.setVersion(l.getVersionLexique().getId());
			list.add(pojo);
			
		}
		
		return ApiResponseHandler.generateResponse(HttpStatus.OK, true, Message.OK_ADD + "Lexique", list);
	}
	@GetMapping(value="/api/v1/lexiques/read/{id}")
	public ResponseEntity<Object> readLexiques(@PathVariable Long id){
		
		LexiqueDTO lexiqueDTO;
		LexiqueChildDTO lexiqueChildDTO;
		List<LexiqueDTO> parents = new ArrayList<>();
		List<LexiqueChildDTO> children;
		
		for(Lexique l : lexiqueService.findByVersionLexique(id)) {
			//le code recupère uniquement les parents et leurs enfants
			if(l.getParent()==null) {
				lexiqueDTO = new LexiqueDTO();
				lexiqueDTO.setId(l.getId());
				lexiqueDTO.setName(l.getCode() +" :"+ l.getLibelle());
				children = new ArrayList<>();
				for(Lexique child : l.getChilds()) {
					lexiqueChildDTO = new LexiqueChildDTO();
					lexiqueChildDTO.setId(child.getId());
					lexiqueChildDTO.setName(child.getCode() +" :"+ child.getLibelle());
					children.add(lexiqueChildDTO);
				}
				lexiqueDTO.setChildren(children);
				parents.add(lexiqueDTO);
			}			
		}
		return ApiResponseHandler.generateResponse(HttpStatus.OK, true, Message.OK_ADD + "Lexique", parents );
	}
}
