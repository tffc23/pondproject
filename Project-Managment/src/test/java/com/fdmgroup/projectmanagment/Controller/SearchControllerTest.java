package com.fdmgroup.projectmanagment.Controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import com.fdmgroup.projectmanagment.Model.Project;
import com.fdmgroup.projectmanagment.Model.Skill;
import com.fdmgroup.projectmanagment.Model.User;
import com.fdmgroup.projectmanagment.Service.NotificationService;
import com.fdmgroup.projectmanagment.Service.ProjectRoleService;
import com.fdmgroup.projectmanagment.Service.ProjectService;
import com.fdmgroup.projectmanagment.Service.SkillService;
import com.fdmgroup.projectmanagment.Service.TraineeSkillService;
import com.fdmgroup.projectmanagment.Service.UserService;

import jakarta.servlet.http.HttpSession;


@ExtendWith(MockitoExtension.class)
class SearchControllerTest {

	
	@Mock
	ProjectService pService ;
	@Mock
	ProjectRoleService prService ;
	@Mock
	SkillService skillService ;
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
	SearchController sController ;
	
	
	@BeforeEach
	void setUp() {
	   MockitoAnnotations.openMocks(this);
	}

	@Test
	void testSearchTraineeBySkillExists() {
		
		assertNotNull(skillService) ; // not null but not called 
		
		when(session.getAttribute("user")).thenReturn(new User());

	    String skillName = "Java";
	    Skill skill = new Skill();
	    skill.setTitle(skillName);
	    when(skillService.getSkillByTitle(skillName)).thenReturn(skill);

	    List<User> trainees = new ArrayList<>();
	    when(tsService.getAllTraineesWithSkill(skill)).thenReturn(trainees);
	    
	    String result = sController.getSearchTraineePageSkill(session, model, skillName) ;
	    
	    assertEquals("Search/search", result );
	    verify(skillService, times(2)).getSkillByTitle(skillName); 
	    verify(tsService).getAllTraineesWithSkill(skill); 
	    verify(model).addAttribute("hasQuery", true);
	    verify(model).addAttribute("trainee", true);
	    verify(model).addAttribute("trainees", trainees);
	}
	    
	@Test
	void testSearchTraineeBySkillDoesNotExist() {
	    	
	    assertNotNull(skillService) ; // not null but not called 
			
		when(session.getAttribute("user")).thenReturn(new User());
			
	    String skillName = "JavaScript";
	    
	    Skill skill = new Skill();
	    skill.setTitle(skillName);
	    List<User> trainees = new ArrayList<>();
	    
	    when(skillService.getSkillByTitle(skillName)).thenReturn(null);
	    when(uService.findByLastName(skillName)).thenReturn(trainees);
	    
	    String result = sController.getSearchTraineePageSkill(session, model, skillName) ;
	    
	    assertEquals("Search/search", result );
	    verify(skillService, times(2)).getSkillByTitle(skillName); 
	    verify(uService).findByLastName(skillName);
	    verify(model).addAttribute("hasQuery", true);
	    verify(model).addAttribute("trainee", true);
	    verify(model).addAttribute("trainees", trainees); 
	    
	    }
	
	
	
	 @Test
	 void testGetSearchProjectPageSkillExists() {
		 

	        when(session.getAttribute("user")).thenReturn(new User());
	        
	        // skill exists
	        String skillName = "Java";
	        Skill skill = new Skill();
	        skill.setTitle(skillName);
	        
	        Project project = new Project() ;
	        
	        when(skillService.getSkillByTitle(skillName)).thenReturn(skill);

	        List<Long> projectIds = new ArrayList<>();
	        projectIds.add(1L) ;
	        
	        when(prService.getAllProjectIdsBySkill(skill)).thenReturn(projectIds);

	        Set<Project> projectsSet = new HashSet<Project>();
	        
	        for (Long projectId : projectIds) {
	            when(pService.findProjectById(projectId)).thenReturn(project);
	            projectsSet.add(project);
	        }
	        
	        
	        List<Project> projects = new ArrayList<Project>(projectsSet);
	       
	        String result = sController.getSearchProjectPage(session, model, skillName);

	        assertEquals("Search/search", result);
	        
	        verify(skillService, times(2)).getSkillByTitle(skillName);
	        verify(prService).getAllProjectIdsBySkill(skill);
	        verify(pService, times(projectIds.size())).findProjectById(projectIds.get(0));
	        verify(model).addAttribute("projects", projects);
	        verify(model).addAttribute("hasQuery", true);
	        verify(model).addAttribute("trainee", false);
	}
	 
	 
	 
	 @Test
	 void testGetSearchProjectPageSkillDoesntExist() {
		
		 	when(session.getAttribute("user")).thenReturn(new User());
		 
			Project project = new Project();
	        ArrayList<Project> projects = new ArrayList<>();
	        projects.add(project);
	        
	        when(skillService.getSkillByTitle("TestSkill")).thenReturn(null);
	        when(pService.findByTitle("TestSkill")).thenReturn(project);

	        String result = sController.getSearchProjectPage(session, model, "TestSkill");

	        assertEquals("Search/search", result);
	        
	        verify(session, times(2)).getAttribute("user");
	        verify(skillService, times(1)).getSkillByTitle("TestSkill");
	        verify(pService, times(1)).findByTitle("TestSkill");	 
		 
	 }
	 
	 }
	


