package com.fdmgroup.projectmanagment.Service;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdmgroup.projectmanagment.Model.Project;
import com.fdmgroup.projectmanagment.Model.ProjectType;
import com.fdmgroup.projectmanagment.Model.Region;
import com.fdmgroup.projectmanagment.Model.Role;
import com.fdmgroup.projectmanagment.Model.User;
import com.fdmgroup.projectmanagment.Repository.ProjectRepository;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTests {

	@Mock
	ProjectRepository projectRepository ;
	
	@InjectMocks
	ProjectService projectService ;
	
	private User user1;
	
	
	@BeforeEach
	void setUp() throws Exception {
		user1 = new User("Tiffany", "C", "tiff", "tiff@email.com", "password", Region.AU, Role.Trainee, true);
		
	}

	@Test
	void testGettingAllProjects() {
		List<Project> projects = new ArrayList<Project>() ;
		when(projectRepository.findAll()).thenReturn(projects) ;
		List<Project> projectsTest = projectService.getAllProjects();
		verify(projectRepository, times(1)).findAll() ;
		assertEquals(projects,projectsTest);
	}
	
	@Test
	void testFindingProjectById() {
		Long id = (long) 1 ;
		Project project = new Project(user1, "Elevator Project", ProjectType.Mock,"Multi-threaded Elevator Project");	
		when(projectRepository.findByProjectId(id)).thenReturn(project) ;
		Project testProject = projectService.findProjectById(id) ;
		verify(projectRepository, times(1)).findByProjectId(id) ;
		Project projectObject = project;
		assertEquals(projectObject,testProject) ;
	}
	
	@Test
	void testFindingProjectByTitle() {
		String title = "Elevator Project" ;
		Project project = new Project(user1, "Elevator Project", ProjectType.Mock,"Multi-threaded Elevator Project");	
		when(projectRepository.findByTitle(title)).thenReturn(project);
		Project testProject = projectService.findByTitle(title) ;
		verify(projectRepository, times(1)).findByTitle(title) ;
		Project projectObject = project;
		assertEquals(projectObject,testProject) ;
	}
	
	@Test
	public void test_findByProjectType()
	{
		List<Project> projects = new ArrayList<Project>();	
		when(projectRepository.findByType(ProjectType.Mock)).thenReturn(projects);
		
		List<Project> foundProjects = projectService.findByProjectType(ProjectType.Mock);
		
		assertNotNull(foundProjects);
		assertEquals(foundProjects, projects);		
	}
	
	@Test
	public void test_deleteByProjectId_calls_projectRepository()
	{
		projectService.deleteByProjectId(1L);
		
		verify(projectRepository, times(1)).deleteById(1L);
	}
	
	@Test
	public void test_saveProject_calls_projectRepository()
	{
		Project project = new Project(user1, "Elevator", ProjectType.Mock, "Elevator project");
		projectService.saveProject(project);
		
		verify(projectRepository, times(1)).save(project);
	}
	
	@Test
	public void test_findByCreator()
	{
		List<Project> projects = new ArrayList<Project>();		
		Project project = new Project(user1, "Elevator", ProjectType.Mock, "Elevator project");
		projects.add(project);
		
		when(projectRepository.findByCreator(user1)).thenReturn(projects);
		
		List<Project> foundProjects = projectService.findByCreator(user1);
		assertNotNull(foundProjects);
		assertEquals(projects, foundProjects);
		assertEquals(project, foundProjects.get(0));
	}
	
	@Test
	public void test_countSkills()
	{
		when(projectRepository.countProjects()).thenReturn(5);
		assertEquals(5, projectService.countProjects());
	}
	
	

}

