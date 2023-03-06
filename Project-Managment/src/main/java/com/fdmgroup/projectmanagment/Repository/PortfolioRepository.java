package com.fdmgroup.projectmanagment.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.projectmanagment.Model.Portfolio;
import com.fdmgroup.projectmanagment.Model.User;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long>
{
	Optional<Portfolio> findById(Long id);
	
	List<Portfolio> findByCreator(User creator);
	
	
	
}
