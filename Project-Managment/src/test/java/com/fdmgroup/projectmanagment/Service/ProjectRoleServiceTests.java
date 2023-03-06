package com.fdmgroup.projectmanagment.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdmgroup.projectmanagment.Model.Project;
import com.fdmgroup.projectmanagment.Model.ProjectRole;
import com.fdmgroup.projectmanagment.Model.ProjectType;
import com.fdmgroup.projectmanagment.Model.Region;
import com.fdmgroup.projectmanagment.Model.Role;
import com.fdmgroup.projectmanagment.Model.Skill;
import com.fdmgroup.projectmanagment.Model.User;
import com.fdmgroup.projectmanagment.Repository.ProjectRoleRepository;

@ExtendWith(MockitoExtension.class)
public class ProjectRoleServiceTests {
	
	@Mock
	ProjectRoleRepository projectRoleRepository ;
	
	
	@InjectMocks
	ProjectRoleService projectRoleService ;
	
	private User user1;
	private Project project1;
	private ProjectRole developer1;
	private ProjectRole tester1;
	private Skill italian;
	
	@BeforeEach
	void setUp()
	{
		user1 = new User("Tiffany", "C", "tiff", "tiff@email.com", "password", Region.AU, Role.Trainee, true);
		project1 = new Project(user1, "Elevator Project", ProjectType.Mock,"Multi-threaded Elevator Project");
		developer1 = new ProjectRole(project1, "Developer", "Coding");
		tester1 = new ProjectRole(project1, "Tester", "Testing");
		italian = new Skill("Italian", "Language");
		
	}

	@Test
	void testSaveProjectRole() {
		assertNotNull(projectRoleRepository) ;
		projectRoleService.save(developer1) ;
		verify(projectRoleRepository,times(1)).save(developer1) ;
	}
	
	@Test
	void test_delete()
	{
		projectRoleService.delete(developer1) ;
		verify(projectRoleRepository,times(1)).delete(developer1) ;
	}
	
	@Test
	void testFindingProjectRoleBySkill() {
		
		assertNotNull(projectRoleRepository) ;

		italian.setStatus(true);
		italian.setId(1L); 
		
		List<ProjectRole> projectRoles = new ArrayList<ProjectRole>() ;
		projectRoles.add(developer1) ;
		projectRoles.add(tester1) ;
		
		List<Long> testIds = new ArrayList<Long>() ;
		testIds.add(1L);
		testIds.add(2L);
				
		when(projectRoleRepository.findByMyColumn(italian.getId().toString())).thenReturn(testIds) ;
		when(projectRoleRepository.findById(1L)).thenReturn(Optional.of(developer1));
		when(projectRoleRepository.findById(2L)).thenReturn(Optional.of(tester1));
		
		List<ProjectRole> testProjectRoles = projectRoleService.findProjectRoleIdBySkill(italian) ;
		
		verify(projectRoleRepository,times(1)).findByMyColumn(italian.getId().toString()) ;
		
		assertNotNull(testProjectRoles);
		assertEquals(projectRoles.get(0), testProjectRoles.get(0)) ;
		assertEquals(projectRoles.get(1), testProjectRoles.get(1)) ;
		
	}
	
	@Test
	void testGetAllProjectsBySkill() {

		italian.setStatus(true);
		italian.setId(1L); 
		
		List<Long> projectIds = new ArrayList<Long>() ;
		projectIds.add(1L);
		projectIds.add(2L) ;
		
		List<Long> testIds = new ArrayList<Long>() ;
		testIds.add(1L);
		testIds.add(2L);
		
		when(projectRoleRepository.findByMyColumn(italian.getId().toString())).thenReturn(testIds) ;
		when(projectRoleRepository.findProjectById("1")).thenReturn(1L);
		when(projectRoleRepository.findProjectById("2")).thenReturn(2L);
		
		List<Long> testProjectIds = projectRoleService.getAllProjectIdsBySkill(italian) ;
		verify(projectRoleRepository,times(1)).findByMyColumn(italian.getId().toString()) ; 
		verify(projectRoleRepository,times(1)).findProjectById("1") ;
		verify(projectRoleRepository,times(1)).findProjectById("2") ;
		
		assertEquals(testProjectIds, projectIds) ;
	}
	
	@Test
	public void test_getParticipantId_when_the_participant_is_not_null()
	{
		developer1.setParticipant(user1);
		developer1.setId(1L);
		when(projectRoleRepository.findParticipantById("1")).thenReturn(Optional.of(1L));
		
		Long foundId = projectRoleService.getParticipantId(developer1);
		
		assertNotNull(foundId);
		assertEquals(1L, foundId);		
	}

	@Test
	public void test_getParticipantId_when_the_participant_is_null()
	{
		developer1.setId(1L);
		when(projectRoleRepository.findParticipantById("1")).thenReturn(Optional.empty());
		
		Long foundId = projectRoleService.getParticipantId(developer1);
		
		assertNull(foundId);
	}
	
	@Test
	public void test_findProjectRolesByParticipant()
	{
		List<ProjectRole> roles = new ArrayList<ProjectRole>();
		roles.add(developer1);
		when(projectRoleRepository.findByParticipant(user1)).thenReturn(roles);
		
		assertEquals(projectRoleService.findProjectRolesByParticipant(user1), roles);
	}
	
	@Test
	public void test_findProjectRoleById()
	{
		when(projectRoleRepository.findById(1L)).thenReturn(Optional.of(developer1));
		
		assertEquals(projectRoleService.findProjectRoleById(1L), developer1);
	}
	
	@Test
	public void test_findProjectRoleById_when_it_does_not_exist()
	{
		when(projectRoleRepository.findById(1L)).thenReturn(Optional.empty());
		assertNull(projectRoleService.findProjectRoleById(1L));
	}
	
	@Test
	public void test_deleteProjectRoleById()
	{
		projectRoleService.deleteProjectRoleById(1L);
		verify(projectRoleRepository, times(1)).deleteById(1L);
	}
	
	@Test
	public void test_findProjectRolesByProject()
	{
		List<ProjectRole> roles = List.of(developer1, tester1);
		when(projectRoleRepository.findByProject(project1)).thenReturn(roles);
		
		List<ProjectRole> foundRoles = projectRoleService.findProjectRolesByProject(project1);
		assertNotNull(foundRoles);
		assertEquals(roles, foundRoles);
	}
	
	@Test
	public void test_findParticipantsByProject()
	{
		List<User> users = List.of(user1);

		when(projectRoleRepository.findParticipantsByProject(project1)).thenReturn(users);
		
		List<User> foundParticipants = projectRoleService.findParticipantsByProject(project1);
		
		assertNotNull(foundParticipants);
		assertEquals(users, foundParticipants);
	}
  
}

