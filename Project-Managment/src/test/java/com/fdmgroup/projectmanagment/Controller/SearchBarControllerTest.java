package com.fdmgroup.projectmanagment.Controller;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import com.fdmgroup.projectmanagment.Model.User;
import com.fdmgroup.projectmanagment.Service.NotificationService;
import com.fdmgroup.projectmanagment.Service.ProjectRoleService;
import com.fdmgroup.projectmanagment.Service.ProjectService;
import com.fdmgroup.projectmanagment.Service.SkillService;
import com.fdmgroup.projectmanagment.Service.TraineeSkillService;
import com.fdmgroup.projectmanagment.Service.UserService;

import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class SearchBarControllerTest {

	@Mock
	ProjectService pService ;
	@Mock
	ProjectRoleService prService ;
	@Mock
	SkillService sService ;
	@Mock
	TraineeSkillService tsService ;
	@Mock
	UserService uService ;
	@Mock
	NotificationService nService;
	
	@Mock
	Model model;
	
	@Mock
	HttpSession session;
	
	@InjectMocks
	SearchBarController sbController ;
	
	
	@BeforeEach
	void setUp() {
	   MockitoAnnotations.openMocks(this);
	}

	@Test
	void renderUserWhenNull() {
		when(session.getAttribute("user")).thenReturn(null) ;
		boolean result = sbController.renderUser(session, model) ;
		assertEquals(false, result) ;
		
	}
	
	@Test
	void renderUserWhenNotNull() {
		when(session.getAttribute("user")).thenReturn(new User()) ;
		boolean result = sbController.renderUser(session, model) ;
		assertEquals(true, result) ;
		
	}
	
	@Test
	void returnsSearchPage() {
		when(session.getAttribute("user")).thenReturn(new User()) ;
		String result = sbController.getSearchPage(session, model) ;
		assertEquals("Search/search" , result) ;
	}
	
	@Test
	void doesNotReturnSearchPage() {
		when(session.getAttribute("user")).thenReturn(null) ;
		String result = sbController.getSearchPage(session, model) ;
		assertEquals("redirect:/login" , result) ;
	}
	
	@Test
	void searchProjects() {
		
		 
        String searchType = "option 1";
        String skillName = "Java";
        String expectedUrl = "redirect:/searchTrainee/Java";
        assertEquals(expectedUrl, sbController.searchTraineesProjectsBySkill(searchType, skillName, model));

        
        searchType = "option 2";
        skillName = "Python";
        expectedUrl = "redirect:/searchProject/Python";
        assertEquals(expectedUrl, sbController.searchTraineesProjectsBySkill(searchType, skillName, model));

        
        searchType = "other value";
        skillName = "JavaScript";
        expectedUrl = "redirect:/search";
        assertEquals(expectedUrl, sbController.searchTraineesProjectsBySkill(searchType, skillName, model));
    }
		
	}


