package com.catis.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.catis.model.Utilisateur;
import com.catis.repository.UtilisateurRepository;

@Service
public class UtilisateurService {

	@Autowired
	private UtilisateurRepository utilisateurRepository;
	
	public void addUtilisateur(Utilisateur utilisateur) {
		utilisateurRepository.save(utilisateur);
	}
	public void updateUtilisateur(Utilisateur utilisateur) {
		utilisateurRepository.save(utilisateur);
	}
	public List<Utilisateur> findAllUtilisateur() {
		List<Utilisateur> utilisateurs = new ArrayList<>();
		utilisateurRepository.findAll().forEach(utilisateurs::add);
		return utilisateurs;
	}
	public Utilisateur findUtilisateurById(String idUtilisateur) {
		return utilisateurRepository.findById(idUtilisateur).get();
	}
	public Utilisateur findUtilisateurByLogin(String login) {
		return utilisateurRepository.findByLogin(login);
	}
	public void deleteUtilisateurById(String idCaissierCaisse) {
		utilisateurRepository.deleteById(idCaissierCaisse);
	}
	public Utilisateur authentification(Utilisateur user) {
		return utilisateurRepository.findByLogin(user.getLogin());
	}
}
