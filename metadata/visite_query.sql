SELECT  v.modified_date, JSON_OBJECT(
    'createdDate', v.created_date,
    'modified_date', v.modified_date,
    'statut', v.statut,
    'contreVisite', CASE v.contre_visite WHEN b'1' THEN 1 ELSE 0 END,
    'encours', CASE v.encours WHEN b'1' THEN 1 ELSE 0 END,
    'statutVisite', v.statut,
    'idVisite', v.id,
    'conformityTest', v.is_conform,
    'document', v.document,
    'freinage', v.freinage,
    'pollution', v.pollution,
    'reglophare', v.reglophare,
    'ripage', v.ripage,
    'suspension', v.suspension,
    'visuel', v.visuel,
    'control', JSON_OBJECT(
        'id', c.id,
        'status', c.status,
        'active_status', CASE c.active_status WHEN b'1' THEN 1 ELSE 0 END
    ), 
    'verbalStatus', IF(v.statut != 0 and v.statut !=1, CASE vb.status WHEN b'1' THEN 1 ELSE 0 END, NULL),
    'verbal_process_id', IF(v.statut != 0 and v.statut !=1, vb.id, NULL),
    'Inspection', IF(v.statut != 0 and v.statut !=1, JSON_OBJECT( 
            'id', i.id,
            'activeStatus', CASE i.active_status WHEN b'1' THEN 1 ELSE 0 END, 
            'bestPlate', i.best_plate, 
            'chassis', i.chassis,
            'date_debut', i.date_debut,
            'date_fin', i.date_fin, 
            'distance_percentage', i.distance_percentage, 
            'essieux', i.essieux, 
            'kilometrage', i.kilometrage 
        ), NULL),
    'carteGrise', JSON_OBJECT(
        'carteGriseId', cg.id,
        'numImmatriculation', cg.num_immatriculation,
        'preImmatriculation', cg.pre_immatriculation,
        'dateDebutValid', cg.date_debut_valid,
        'dateFinValid', cg.date_fin_valid,
        'ssdt_id', cg.ssdt_id,
        'commune', cg.commune,
        'montantPaye', cg.montant_paye,
        'vehiculeGage', cg.vehicule_gage,
        'genreVehicule', cg.genre_vehicule,
        'enregistrement', cg.enregistrement,
        'type', cg.type,
        'dateDelivrance', cg.date_delivrance,
        'lieuDedelivrance', cg.lieu_dedelivrance,
        'centre_ssdt', cg.centre_ssdt,
        'proprietaireVehicule', JSON_OBJECT(
            'proprietaireVehiculeId', pv.id,
            'partenaire', JSON_OBJECT(
                 'partenaireId', p.id,
                 'nom', p.nom,
                 'prenom', p.prenom
             ),
            'score', pv.score,
            'description', pv.description
        ),
        'vehicule', IF(v.statut != 0, JSON_OBJECT(
            'vehiculeId', vh.id,
            'typeVehicule', vh.type_vehicule,
            'carrosserie', vh.carrosserie,
            'placeAssise', vh.place_assise,
            'chassis', vh.chassis,
            'premiereMiseEnCirculation', vh.premiere_mise_en_circulation,
            'puissAdmin', vh.puiss_admin,
            'poidsTotalCha', vh.poids_total_cha,
            'poidsVide', vh.poids_vide,
            'chargeUtile', vh.charge_utile,
            'cylindre', vh.cylindre,
            'score', vh.score,
            'marqueVehicule', JSON_OBJECT(
                'marqueVehiculeId', mv.id,
                'libelle', mv.libelle,
                'description', mv.description
            ),
            'energie', JSON_OBJECT(
                'energieId', e.id,
                'libelle', e.libelle
            )
        ), NULL),
        'produit', JSON_OBJECT(
            'produitId', pr.id,
            'libelle', pr.libelle,
            'description', pr.description,
            'prix', pr.prix,
            'delaiValidite', pr.delai_validite,
            'img', pr.img,
            'categorieProduit', JSON_OBJECT(
                'categorieProduitId', cp.id,
                'libelle', cp.libelle,
                'description', cp.description
            ),
            'categorieVehicule', JSON_OBJECT(
                'id', cv.id,
                'type', cv.type
            )
        )
    )
) as json
FROM 
    t_visite v
LEFT JOIN t_inspection i on i.visite_id = v.id AND v.statut != 0 and v.statut !=1
LEFT JOIN verbal_process vb on vb.visite_id = v.id AND v.statut != 0 and v.statut !=1
JOIN 
    t_cartegrise cg ON v.carte_grise_id = cg.id
JOIN 
    control c on  c.carte_grise_id=cg.id
JOIN 
    t_proprietairevehicule pv ON cg.proprietaire_vehicule_id = pv.id
JOIN 
    t_partenaire p ON pv.partenaire_id = p.id
LEFT JOIN t_vehicule vh ON cg.vehicule_id = vh.id AND v.statut != 0
LEFT JOIN t_marquevehicule mv ON vh.marque_vehicule_id = mv.id AND v.statut != 0
LEFT JOIN t_energie e ON vh.energie_id = e.id AND v.statut != 0
JOIN 
    t_produit pr ON cg.produit_id = pr.id
JOIN 
    t_categorieproduit cp ON pr.categorie_produit_id = cp.id
JOIN 
    categorie_vehicule cv ON pr.categorie_vehicule_id = cv.id
ORDER BY v.modified_date DESC

