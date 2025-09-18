package com.furryhub.petservices.repository;

import com.furryhub.petservices.model.entity.Provider;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProviderRepository extends JpaRepository <Provider, Long> {
	 Optional<Provider> findByUser_Email(String email);
	}


