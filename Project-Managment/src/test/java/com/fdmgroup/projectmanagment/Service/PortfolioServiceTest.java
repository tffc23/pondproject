package com.fdmgroup.projectmanagment.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdmgroup.projectmanagment.Model.Portfolio;
import com.fdmgroup.projectmanagment.Model.User;
import com.fdmgroup.projectmanagment.Repository.PortfolioRepository;

@ExtendWith(MockitoExtension.class)
public class PortfolioServiceTest 
{
	@Mock
	PortfolioRepository mockPortfolioRepo;
	
	@InjectMocks
	private PortfolioService portfolioService;
	
	private Portfolio portfolio;
	@BeforeEach
	public void setUp()
	{
		portfolio = new Portfolio();
	}
	
	@Test
	public void test_findById()
	{
		portfolio.setId(1L);
		
		when(mockPortfolioRepo.findById(1L)).thenReturn(Optional.of(portfolio));
		
		Portfolio foundP = portfolioService.findById(1L);
		
		assertNotNull(foundP);
		assertEquals(portfolio, foundP);
	}
	
	@Test
	public void test_findById_when_there_is_no_portfolio()
	{
		when(mockPortfolioRepo.findById(1L)).thenReturn(Optional.empty());
		assertNull(portfolioService.findById(1L));
	}
	
	@Test
	public void test_findByCreator()
	{
		List<Portfolio> portfolios = List.of(portfolio);
		User u = new User();
		
		when(mockPortfolioRepo.findByCreator(u)).thenReturn(portfolios);
		
		List<Portfolio> foundPortfolios = portfolioService.findByCreator(u);
		assertNotNull(foundPortfolios);
		assertEquals(portfolios, foundPortfolios);		
	}
	
	@Test
	public void test_save()
	{
		portfolioService.save(portfolio);
		
		verify(mockPortfolioRepo, times(1)).save(portfolio);
	}
	
	@Test
	public void test_delete()
	{
		portfolioService.delete(portfolio);
		
		verify(mockPortfolioRepo, times(1)).delete(portfolio);
	}
	
	

}
