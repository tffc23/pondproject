package com.fdmgroup.projectmanagment.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fdmgroup.projectmanagment.Model.Portfolio;
import com.fdmgroup.projectmanagment.Model.User;
import com.fdmgroup.projectmanagment.Repository.PortfolioRepository;

@Service
public class PortfolioService 
{	
	private PortfolioRepository portfolioRepository;

	public PortfolioService(PortfolioRepository portfolioRepository) {
		super();
		this.portfolioRepository = portfolioRepository;
	}
	
	public Portfolio findById(Long id)
	{
		return this.portfolioRepository.findById(id).orElse(null);
	}
	
	public List<Portfolio> findByCreator(User creator)
	{
		return this.portfolioRepository.findByCreator(creator);
	}
	
	public void save(Portfolio portfolio)
	{
		this.portfolioRepository.save(portfolio);
	}
	
	public void delete(Portfolio portfolio)
	{
		this.portfolioRepository.delete(portfolio);
	}
}
