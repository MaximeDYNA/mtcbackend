package com.catis.service;

import java.io.FileOutputStream;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.catis.model.entity.CarteGrise;
import com.catis.model.entity.Produit;
import com.catis.repository.CarteGriseRepository;
import com.catis.repository.ProduitRepository;


@Service
public class ProduitService {

    @Autowired
    private ProduitRepository produitRepository;
    @Autowired
    private CarteGriseRepository cgr;
    @Autowired
    private VisiteService visiteService;
    @Autowired
    org.springframework.core.env.Environment env;

    public String saveImage(String base64, String libelle) throws Exception {
        String folder = env.getProperty("uploaded.image");
        String base64New = base64.substring(base64.indexOf(",")+1);
        base64New.trim();
        //base64 = base64.replace("data:image/png;base64,","");
        byte[] bytes = Base64.getMimeDecoder().decode(base64New);
        FileOutputStream output = new FileOutputStream(folder+libelle+".png");
        output.write(bytes);
        return folder + libelle + ".png";
    }

    public List<Produit> findAllProduit() {
        List<Produit> produits = new ArrayList<>();
        produitRepository.findByActiveStatusTrue().forEach(produits::add);
        return produits;
    }

    public List<Produit> findByCategorieProduit(Long id) {
        return produitRepository.findByCategorieProduit_CategorieProduitId(id);
    }

    public void deleteById(Long id){
        produitRepository.deleteById(id);
    }
    public Produit findById(Long id) {
        return produitRepository.findById(id).get();
    }

    public Produit findByLibelle(String libelle) {
        return produitRepository.findByLibelleIgnoreCase(libelle);
    }

    public List<Produit> findProduitWithoutContreVisite() {
        return findAllProduit().stream().filter(produit -> !produit.getLibelle().equalsIgnoreCase("cv"))
                .filter(produit -> !produit.getLibelle().equalsIgnoreCase("dec"))
                .collect(Collectors.toList());
    }

    public Produit findByImmatriculation(String imOrCha) {
        if (cgr.findByNumImmatriculationIgnoreCaseOrVehicule_ChassisIgnoreCase(imOrCha, imOrCha).isEmpty()) {

            return null;
        } else {
            List<Produit> produits = new ArrayList<>();

            for (CarteGrise cg : cgr.findByNumImmatriculationIgnoreCaseOrVehicule_ChassisIgnoreCase(imOrCha, imOrCha)) {

                produits.add(cg.getProduit());
            }
            return produits.get(0);
        }

    }

    public Produit addProduit(Produit produit) {
        return produitRepository.save(produit);

    }

    public List<String> getLibelleList(){
        List<String> produits = new ArrayList<>();
        findAllProduit().forEach(produit -> {
            produits.add(produit.getLibelle());
        });
        return produits;
    }
    public Map<String, Integer> getLibelleAndOccurence(){
        Map<String, Integer> maps = new HashMap<>();
        produitRepository.findByActiveStatusTrue().forEach(
                produit -> {
                    if(!visiteService.findbyProduit(produit).isEmpty());
                                maps.put(produit.getLibelle(),
                                        productOccurenceInVisitList(produit));
                    if(produit.getLibelle().equalsIgnoreCase("cv"))
                        maps.put(produit.getLibelle(), visiteService.findActiveCV().size());
                }
        );
        return maps;
    }

    public int productOccurenceInVisitList(Produit produit){
        int occurrence = visiteService.findActiveVI().stream()
                .filter(visite ->
                            visite.getCarteGrise().getProduit().getLibelle().equals(produit.getLibelle()
                        )
                ).collect(Collectors.toList())
                .size();
        return occurrence;
    }
    public Produit findByName(String name){
       Produit p = produitRepository.findByLibelleIgnoreCase(name);
       return p;
    }

}
