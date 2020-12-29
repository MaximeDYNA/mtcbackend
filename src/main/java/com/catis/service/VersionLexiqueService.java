package com.catis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catis.model.VersionLexique;
import com.catis.repository.VersionLexiqueRepository;

@Service
public class VersionLexiqueService {

	@Autowired
	private VersionLexiqueRepository versionLexiqueRepo;
	
	public VersionLexique add(VersionLexique v) {
		return versionLexiqueRepo.save(v);
	}
	
	public VersionLexique findById(Long id) {
		return versionLexiqueRepo.findById(id).get();
	}
}
