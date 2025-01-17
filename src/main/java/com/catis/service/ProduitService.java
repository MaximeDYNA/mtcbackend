package com.catis.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.catis.model.entity.CarteGrise;
import com.catis.model.entity.Produit;
import com.catis.repository.CarteGriseRepository;
import com.catis.repository.ProduitRepository;


@Service
@CacheConfig(cacheNames={"productsCache"})
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
        File f= new File(env.getProperty("uploaded.image"));
        if(!f.exists())
            f.mkdirs();
        FileOutputStream output = new FileOutputStream(folder+libelle+".png");
        output.write(bytes);
        return folder + libelle + ".png";
    }
    // @Cacheable
    public List<Produit> findAllProduit() {
        List<Produit> produits = new ArrayList<>();
        produitRepository.findByActiveStatusTrue().forEach(produits::add);
        return produits;
    }
    // @Cacheable
    public List<Produit> findAllProduit(Pageable pageable) {
        List<Produit> produits = new ArrayList<>();
        produitRepository.findByActiveStatusTrue(pageable).forEach(produits::add);
        return produits;
    }
    @Cacheable
    public List<Produit> findByCategorieProduit(UUID id) {
        return produitRepository.findByCategorieProduit_CategorieProduitId(id);
    }
    
    // @CacheEvict(key = "#id")
    public void deleteById(UUID id){
        produitRepository.deleteById(id);
    }
    
    public Produit findById(UUID id) {
        return produitRepository.findById(id).get();
    }

    public Produit findByLibelle(String libelle) {
        return produitRepository.findByLibelleIgnoreCase(libelle);
    }
    // @Cacheable
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
    
    @CacheEvict(allEntries = true)
    public Produit addProduit(Produit produit) {
        return produitRepository.save(produit);

    }
    
    // @Cacheable(key = "'libellelist'")
    public List<String> getLibelleList(){
        List<String> produits = new ArrayList<>();
        findAllProduit().forEach(produit -> {
            produits.add(produit.getLibelle());
        });
        return produits;
    }
    // @Cacheable
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
    // @Cacheable
    public int productOccurenceInVisitList(Produit produit){
        int occurrence = visiteService.findActiveVI().stream()
                .filter(visite ->
                            visite.getCarteGrise().getProduit().getLibelle().equals(produit.getLibelle()
                        )
                ).collect(Collectors.toList())
                .size();
        return occurrence;
    }
    
    // @Cacheable(key = "#name")
    public Produit findByName(String name){
       Produit p = produitRepository.findByLibelleIgnoreCase(name);
       return p;
    }

}
