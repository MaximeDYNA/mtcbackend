package com.catis.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.catis.model.CarteGrise;
import com.catis.model.Produit;
import com.catis.repository.CarteGriseRepository;
import com.catis.repository.ProduitRepository;


@Service
public class ProduitService {

	@Autowired
	private ProduitRepository produitRepository;
	@Autowired
	private CarteGriseRepository cgr;
	
	public String saveImage(MultipartFile imageFile) throws Exception{
		String folder = "uploaded/";
		byte[] bytes = imageFile.getBytes();
		Path path = Paths.get(folder + imageFile.getOriginalFilename());
		Files.write(path, bytes);
		return folder + imageFile.getOriginalFilename();
	}

	public List<Produit> findAllProduit(){
		List<Produit> produits = new ArrayList<>();
		produitRepository.findAll().forEach(produits::add);
		return produits;
	}
	public List<Produit> findByCategorieProduit(Long id){
		return produitRepository.findByCategorieProduit_CategorieProduitId(id);
	}
	public Produit findById(Long id) {
		return produitRepository.findById(id).get();
	}
	public Produit findByLibelle(String libelle) {
		return produitRepository.findByLibelleIgnoreCase(libelle);
	}
	public List<Produit> findProduitWithoutContreVisite(){
		return findAllProduit().stream().filter(produit -> !produit.getLibelle().equalsIgnoreCase("cv"))
										.filter(produit -> !produit.getLibelle().equalsIgnoreCase("dec"))
										.collect(Collectors.toList());
	}
	public Produit findByImmatriculation(String imOrCha) {
		if(cgr.findByNumImmatriculationIgnoreCaseOrVehicule_ChassisIgnoreCase(imOrCha, imOrCha).isEmpty()) {
			
			return null;
		}
		else {
				List <Produit> produits = new ArrayList<>();
				
				for(CarteGrise cg : cgr.findByNumImmatriculationIgnoreCaseOrVehicule_ChassisIgnoreCase(imOrCha, imOrCha) ) {
				
				produits.add(cg.getProduit());
				}
			return produits.get(0);
		}
		
	}
	public Produit addProduit(Produit produit) {
		return produitRepository.save(produit);
		
	}
	
}
