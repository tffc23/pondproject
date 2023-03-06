package com.fdmgroup.projectmanagment.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.fdmgroup.projectmanagment.Model.Portfolio;
import com.fdmgroup.projectmanagment.Model.Project;
import com.fdmgroup.projectmanagment.Model.ProjectRole;
import com.fdmgroup.projectmanagment.Model.Region;
import com.fdmgroup.projectmanagment.Model.Role;
import com.fdmgroup.projectmanagment.Model.Skill;
import com.fdmgroup.projectmanagment.Model.SkillLevel;
import com.fdmgroup.projectmanagment.Model.Stream;
import com.fdmgroup.projectmanagment.Model.TraineeSkill;
import com.fdmgroup.projectmanagment.Model.User;
import com.fdmgroup.projectmanagment.Service.NotificationService;
import com.fdmgroup.projectmanagment.Service.PortfolioService;
import com.fdmgroup.projectmanagment.Service.ProjectRoleService;
import com.fdmgroup.projectmanagment.Service.ProjectService;
import com.fdmgroup.projectmanagment.Service.SkillService;
import com.fdmgroup.projectmanagment.Service.TraineeSkillService;
import com.fdmgroup.projectmanagment.Service.UserService;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
public class ProfileControllerTest 
{
	@Mock
	UserService mockUserService;
	
	@Mock
	TraineeSkillService mockTraineeSkillService;
	
	@Mock
	ProjectService mockProjectService;
	
	@Mock
	ProjectRoleService mockProjectRoleService;
	
	@Mock
	SkillService mockSkillService;
	
	@Mock
	NotificationService mockNotificationService;
	
	@Mock
	PortfolioService mockPortfolioService;
	
	@Mock
	HttpSession mockSession;
	
	@Mock
	Model mockModel;
	
	@InjectMocks
	private ProfileController controller;
	
	User user;
	
	@BeforeEach
	public void set_up()
	{
		user = new User();
		user.setId(1L);
	}
	
	@Test
	public void test_renderUserProfile()
	{
		List<Project> projects = new ArrayList<Project>();
		List<ProjectRole> roles = new ArrayList<ProjectRole>();
		
		when(mockProjectService.findByCreator(user)).thenReturn(projects);
		controller.renderUserProfile(user, mockModel);
		
		verify(mockProjectService, times(1)).findByCreator(user);
		verify(mockModel, times(1)).addAttribute("listOfProjects", projects);
		verify(mockProjectRoleService, times(1)).findProjectRolesByParticipant(user);
		verify(mockModel, times(1)).addAttribute("listOfRoles", roles);
	}
	
	@Test
	public void test_getPortfolioPage_when_not_logged_in()
	{
		when(mockSession.getAttribute("user")).thenReturn(null);
		assertEquals("redirect:/login", controller.getPortfolioPage(1L, mockSession, mockModel));
	}
	
	@Test
	public void test_getPortfolioPage_when_logged_in()
	{
		Portfolio portfolio = new Portfolio();
		portfolio.setId(1L);
		
		Project p1 = new Project();
		p1.setProjectId(1L);
		Project p2 = new Project();
		p1.setProjectId(2L);
		
		ProjectRole r1 = new ProjectRole();
		ProjectRole r2 = new ProjectRole();

		p1.setProjectRoles(List.of(r1));
		r1.setProject(p1);
		portfolio.setProjects(List.of(p1));
		p2.setProjectRoles(List.of(r2));
		r2.setProject(p2);
		
		when(mockSession.getAttribute("user")).thenReturn(user);
		when(mockPortfolioService.findById(1L)).thenReturn(portfolio);
		when(mockProjectRoleService.findProjectRolesByParticipant(user)).thenReturn(List.of(r1, r2));
		
		String page = controller.getPortfolioPage(1L, mockSession, mockModel);
		verify(mockModel, times(1)).addAttribute("portfolio", portfolio);
		assertEquals("Portfolio/portfolioDetail", page);
	}
	
	@Test
	public void test_getCreatePortfolio_when_not_logged_in()
	{		
		when(mockSession.getAttribute("user")).thenReturn(null);
		assertEquals("redirect:/login", controller.getCreatePortfolio(mockSession, mockModel));
	}
	
	@Test
	public void test_getCreatePortfolio_when_logged_in()
	{
		when(mockSession.getAttribute("user")).thenReturn(user);

		Project p1 = new Project();
		ProjectRole pr1 = new ProjectRole();
		pr1.setProject(p1);
		p1.setProjectRoles(List.of(pr1));
		
		Skill java = new Skill("java", "java");
		java.setStatus(true);

		List<Project> projects = List.of(p1);
		List<ProjectRole> roles = List.of(pr1);
		List<Skill> approvedSkills = List.of(java);
		
		when(mockProjectService.findByCreator(user)).thenReturn(projects);
		when(mockProjectRoleService.findProjectRolesByParticipant(user)).thenReturn(roles);
		when(mockSkillService.getAllApprovedSkills()).thenReturn(approvedSkills);
		
		
		String page = controller.getCreatePortfolio(mockSession, mockModel);
		verify(mockModel, times(1)).addAttribute("approvedSkills", approvedSkills);
		verify(mockModel, times(1)).addAttribute("isEdit", false);
		assertEquals("Portfolio/createPortfolio", page);
	}
	
	@Test
	public void test_savePortfolio_when_it_is_a_portfolio_being_edited()
	{
		when(mockSession.getAttribute("user")).thenReturn(user);
		Portfolio p = new Portfolio();
		p.setId(2L);
		
		String page = controller.savePortfolio(mockSession, p, true);
		
		assertEquals("redirect:/portfolio/2", page);
	}
	
	@Test
	public void test_savePortfolio_when_it_is_a_portfolio_being_created()
	{
		when(mockSession.getAttribute("user")).thenReturn(user);
		Portfolio p = new Portfolio();
		
		String page = controller.savePortfolio(mockSession, p, false);
		
		assertEquals("redirect:/profile/1", page);
	}
	
	@Test
	public void test_deletePortfolio()
	{
		Portfolio p = new Portfolio();
		p.setId(1L);
		
		when(mockSession.getAttribute("user")).thenReturn(user);
		when(mockPortfolioService.findById(1L)).thenReturn(p);
		
		String page = controller.deletePortfolio(1L, mockSession);
		
		assertEquals("redirect:/profile/1", page);
		verify(mockPortfolioService, times(1)).delete(p);
	}
	
	@Test
	public void test_editPortfolio_when_not_logged_in()
	{
		when(mockSession.getAttribute("user")).thenReturn(null);
		assertEquals("redirect:/login", controller.editPortfolio(1L, mockSession, mockModel));
	}
	
	@Test
	public void test_editPortfolio_when_logged_in()
	{
		Portfolio portfolio = new Portfolio();
		portfolio.setId(1L);

		Project p1 = new Project();
		ProjectRole pr1 = new ProjectRole();
		pr1.setProject(p1);
		p1.setProjectRoles(List.of(pr1));
		
		Skill java = new Skill("java", "java");
		java.setStatus(true);

		List<Project> projects = List.of(p1);
		List<ProjectRole> roles = List.of(pr1);
		List<Skill> approvedSkills = List.of(java);
		
		when(mockProjectService.findByCreator(user)).thenReturn(projects);
		when(mockProjectRoleService.findProjectRolesByParticipant(user)).thenReturn(roles);
		when(mockSkillService.getAllApprovedSkills()).thenReturn(approvedSkills);
		when(mockSession.getAttribute("user")).thenReturn(user);
		when(mockPortfolioService.findById(1L)).thenReturn(portfolio);
		
		String page = controller.editPortfolio(1L, mockSession, mockModel);
		
		assertEquals("Portfolio/createPortfolio", page);
		verify(mockModel, times(1)).addAttribute("portfolio", portfolio);
		verify(mockModel, times(1)).addAttribute("approvedSkills", approvedSkills);
		verify(mockModel, times(1)).addAttribute("isEdit", true);
	}
	
	@Test
	public void test_deleteProjectFromPortfolio()
	{
		Portfolio portfolio = new Portfolio();
		portfolio.setId(1L);
		Project project1 = new Project();
		project1.setProjectId(1L);
		Project project2 = new Project();
		project2.setProjectId(2L);
		portfolio.setProjects(new ArrayList<Project>(List.of(project1, project2)));
		
		when(mockPortfolioService.findById(1L)).thenReturn(portfolio);
		when(mockProjectService.findProjectById(1L)).thenReturn(project1);
		
		String page = controller.deleteProjectFromPortfolio(1L, 1L);
		
		portfolio.setProjects(List.of(project2));
		
		assertEquals("redirect:/portfolio/1", page);
		verify(mockPortfolioService, times(1)).save(portfolio);
	}
	
	@Test
	public void test_deleteProjectFromPortfolio_no_projects_in_portfolio()
	{

		Portfolio portfolio = new Portfolio();
		portfolio.setId(1L);
		portfolio.setProjects(new ArrayList<Project>());
		
		when(mockPortfolioService.findById(1L)).thenReturn(portfolio);
		when(mockProjectService.findProjectById(1L)).thenReturn(null);
		
		String page = controller.deleteProjectFromPortfolio(1L, 1L);

		assertEquals("redirect:/portfolio/1", page);
		verify(mockPortfolioService, times(1)).save(portfolio);		
	}
	
	@Test
	public void test_getProfilePage_when_not_logged_in()
	{
		when(mockSession.getAttribute("user")).thenReturn(null);
		assertEquals("redirect:/login", controller.getProfilePage(1L, mockSession, mockModel));
	}
	
	@Test
	public void test_getProfilePage_when_logged_in_profile_is_of_current_user_and_user_is_trainee()
	{
		user.setRole(Role.Trainee);
		when(mockSession.getAttribute("user")).thenReturn(user);
		
		List<TraineeSkill> skills = new ArrayList<TraineeSkill>();
		List<Portfolio> portfolios = new ArrayList<Portfolio>();
		
		when(mockTraineeSkillService.getAllTraineeSkillsWithTrainee(user)).thenReturn(skills);
		when(mockPortfolioService.findByCreator(user)).thenReturn(portfolios);
		
		String page = controller.getProfilePage(1L, mockSession, mockModel);
		
		verify(mockModel, times(1)).addAttribute("isUser", true);
		verify(mockModel, times(1)).addAttribute("listOfTraineeSkills", skills);
		verify(mockModel, times(1)).addAttribute("portfolios", portfolios);
		
		assertEquals("UserProfile/userProfile", page);
	}
	
	@Test
	public void test_getProfilePage_when_logged_in_profile_is_of_current_user_and_user_is_not_trainee()
	{
		user.setRole(Role.Sales);
		when(mockSession.getAttribute("user")).thenReturn(user);
		
		String page = controller.getProfilePage(1L, mockSession, mockModel);
		
		verify(mockModel, times(1)).addAttribute("isUser", true);
		assertEquals("UserProfile/userProfile", page);
	}
	
	@Test
	public void test_getProfilePage_when_logged_in_profile_is_not_of_current_user_and_user_is_not_trainee()
	{
		user.setRole(Role.Sales);
		when(mockSession.getAttribute("user")).thenReturn(user);
		User user2 = new User();
		when(mockUserService.findById(2L)).thenReturn(user2);
	
		String page = controller.getProfilePage(2L, mockSession, mockModel);
		
		verify(mockModel, times(1)).addAttribute("isUser", false);
		verify(mockModel, times(1)).addAttribute(user2);
		assertEquals("UserProfile/userProfile", page);
	}
	
	@Test
	public void test_getProfilePage_when_logged_in_profile_is_not_of_current_user_and_profile_user_is_trainee()
	{
		when(mockSession.getAttribute("user")).thenReturn(user);
		User user2 = new User();
		user2.setRole(Role.Trainee);
		when(mockUserService.findById(2L)).thenReturn(user2);
		
		List<TraineeSkill> skills = new ArrayList<TraineeSkill>();
		List<Portfolio> portfolios = new ArrayList<Portfolio>();
		
		when(mockTraineeSkillService.getAllTraineeSkillsWithTrainee(user2)).thenReturn(skills);
		when(mockPortfolioService.findByCreator(user2)).thenReturn(portfolios);
		
		String page = controller.getProfilePage(2L, mockSession, mockModel);
		
		verify(mockModel, times(1)).addAttribute("listOfTraineeSkills", skills);
		verify(mockModel, times(1)).addAttribute("portfolios", portfolios);
		
		verify(mockModel, times(1)).addAttribute("isUser", false);
		verify(mockModel, times(1)).addAttribute(user2);
		assertEquals("UserProfile/userProfile", page);
	}
	
	@Test
	public void test_getAddSkillPage_when_not_logged_in()
	{
		when(mockSession.getAttribute("user")).thenReturn(null);
		assertEquals("redirect:/login", controller.getAddSkillPage(mockSession, mockModel));
	}
	
	@Test
	public void test_getAddSkillPage_when_logged_in_but_user_is_not_trainee()
	{
		user.setRole(Role.Sales);
		when(mockSession.getAttribute("user")).thenReturn(user);
		assertEquals("redirect:/", controller.getAddSkillPage(mockSession, mockModel));
	}
	
	@Test
	public void test_getAddSkillPage_when_logged_in_and_user_is__trainee()
	{
		user.setRole(Role.Trainee);
		when(mockSession.getAttribute("user")).thenReturn(user);
		
		Skill java = new Skill();
		java.setId(1L);
		Skill python = new Skill();
		python.setId(2L);
		
		List<Skill> skillsNotHave = new ArrayList<Skill>(List.of(java, python));
		
		List<Long> skillsHave = new ArrayList<Long>(List.of(1L));
		
		when(mockSkillService.getAllApprovedSkills()).thenReturn(skillsNotHave);
		when(mockSkillService.getSkillById(1L)).thenReturn(java);
		when(mockTraineeSkillService.getAllSkillIdsByTrainee(user)).thenReturn(skillsHave);
		
		String page = controller.getAddSkillPage(mockSession, mockModel);
		
		assertEquals("Skill/traineeAddSkill", page);
		verify(mockModel, times(1)).addAttribute("listOfApprovedSkills", new ArrayList<Skill>(List.of(python)));
		verify(mockModel, times(1)).addAttribute("skillLevels", SkillLevel.values());
	}
	
	@Test
	public void test_processNewTraineeSkill()
	{
		Skill java = new Skill();
		java.setId(1L);
		when(mockSession.getAttribute("user")).thenReturn(user);		
		when(mockSkillService.getSkillById(1L)).thenReturn(java);

		TraineeSkill ts = new TraineeSkill();
		ts.setTrainee(user);
		ts.setSkill(java);
		ts.setSkillLevel(SkillLevel.Advanced);
		
		String page = controller.processNewTraineeSkill(1L, SkillLevel.Advanced, mockSession);

		assertEquals("redirect:/traineeSkill", page);
		verify(mockTraineeSkillService, times(1)).save(ts);
	}
	
	@Test
	public void test_deleteSkill_when_not_logged_in()
	{
		when(mockSession.getAttribute("user")).thenReturn(null);
		assertEquals("redirect:/login", controller.deleteSkill(1L, mockSession, mockModel));
	}
	
	@Test
	public void test_deleteSkill_when_not_trainee()
	{
		user.setRole(Role.Sales);
		when(mockSession.getAttribute("user")).thenReturn(user);
		assertEquals("redirect:/", controller.deleteSkill(1L, mockSession, mockModel));
	}
	
	@Test
	public void test_deleteSkill_when_is_trainee()
	{
		user.setRole(Role.Trainee);
		when(mockSession.getAttribute("user")).thenReturn(user);
		TraineeSkill ts = new TraineeSkill();
		when(mockTraineeSkillService.getTraineeSkillById(1L)).thenReturn(ts);
		
		String page = controller.deleteSkill(1L, mockSession, mockModel);
		
		assertEquals("redirect:/traineeSkill", page);
		verify(mockTraineeSkillService, times(1)).delete(ts);
	}
	
	@Test
	public void test_getSuggestSkillPage_when_not_logged_in()
	{
		when(mockSession.getAttribute("user")).thenReturn(null);
		assertEquals("redirect:/login", controller.getSuggestSkillPage(mockSession, mockModel));
	}
	
	@Test
	public void test_getSuggestSkillPage_when_not_trainee()
	{
		user.setRole(Role.Sales);
		when(mockSession.getAttribute("user")).thenReturn(user);
		assertEquals("redirect:/", controller.getSuggestSkillPage(mockSession, mockModel));
	}
	
	@Test
	public void test_getSuggestSkillPage_when_logged_in_and_is_trainee()
	{
		user.setRole(Role.Trainee);
		when(mockSession.getAttribute("user")).thenReturn(user);
		assertEquals("Skill/traineeSuggestSkill", controller.getSuggestSkillPage(mockSession, mockModel));
		verify(mockModel, times(1)).addAttribute("newSkill", new Skill());
	}
	
	@Test
	public void test_processSuggestedSkill()
	{
		Skill s = new Skill();
		
		assertEquals("redirect:/traineeSkill", controller.processSuggestedSkill(s));
		
		s.setStatus(false);
		verify(mockSkillService, times(1)).save(s);
	}
	
	@Test
	public void test_getSkillRequestPage_when_not_logged_in()
	{
		when(mockSession.getAttribute("user")).thenReturn(null);
		assertEquals("redirect:/login", controller.getSkillRequestPage(mockSession, mockModel));
	}
	
	@Test
	public void test_getSkillRequestPage_when_logged_in()
	{
		List<Skill> unapprovedSkills = new ArrayList<Skill>();

		when(mockSession.getAttribute("user")).thenReturn(user);
		when(mockSkillService.getAllUnapprovedSkills()).thenReturn(unapprovedSkills);
		
		assertEquals("Skill/trainerSkillRequests", controller.getSkillRequestPage(mockSession, mockModel));
		verify(mockModel, times(1)).addAttribute("skills", unapprovedSkills);
	}
	
	@Test
	public void test_approveSkill()
	{
		Skill skill = new Skill();
		when(mockSkillService.getSkillById(1L)).thenReturn(skill);
		
		skill.setStatus(true);
		
		assertEquals("redirect:/skillRequests", controller.approveSkill(1L, mockModel));
		verify(mockSkillService, times(1)).save(skill);		
	}
	
	@Test
	public void test_rejectSkill()
	{
		assertEquals("redirect:/skillRequests", controller.rejectSkill(1L, mockModel));
		verify(mockSkillService, times(1)).delete(1L);		
	}
	
	@Test
	public void test_getTraineeSkillPage_not_logged_in()
	{
		when(mockSession.getAttribute("user")).thenReturn(null);
		assertEquals("redirect:/login", controller.getTraineeSkillPage(mockSession, mockModel));
	}
	
	@Test
	public void test_getTraineeSkillPage_logged_in_but_not_trainee()
	{
		user.setRole(Role.Sales);
		when(mockSession.getAttribute("user")).thenReturn(user);
		assertEquals("redirect:/", controller.getTraineeSkillPage(mockSession, mockModel));
	}
	
	@Test
	public void test_getTraineeSkillPage_logged_in_and_is_trainee()
	{
		user.setRole(Role.Trainee);
		when(mockSession.getAttribute("user")).thenReturn(user);
		
		List<TraineeSkill> skills = new ArrayList<TraineeSkill>();
		when(mockTraineeSkillService.getAllTraineeSkillsWithTrainee(user)).thenReturn(skills);
		
		assertEquals("Skill/traineeViewSkill", controller.getTraineeSkillPage(mockSession, mockModel));
		verify(mockModel, times(1)).addAttribute("listOfSkills", skills);
	}
	
	@Test
	public void test_getChangeSkillLevelPage_not_logged_in()
	{
		when(mockSession.getAttribute("user")).thenReturn(null);
		assertEquals("redirect:/login", controller.getChangeSkillLevelPage(1L, mockSession, mockModel));
	}
	
	@Test
	public void test_getChangeSkillLevelPage_logged_in_but_not_trainee()
	{
		user.setRole(Role.Sales);
		when(mockSession.getAttribute("user")).thenReturn(user);
		assertEquals("redirect:/", controller.getChangeSkillLevelPage(1L, mockSession, mockModel));
	}
	
	@Test
	public void test_getChangeSkillLevelPage_logged_in_and_is_trainee()
	{
		user.setRole(Role.Trainee);
		when(mockSession.getAttribute("user")).thenReturn(user);
		
		TraineeSkill ts = new TraineeSkill();
		when(mockTraineeSkillService.getTraineeSkillById(1L)).thenReturn(ts);
		
		assertEquals("Skill/changeSkillLevel", controller.getChangeSkillLevelPage(1L, mockSession, mockModel));
		
		verify(mockModel, times(1)).addAttribute("traineeSkill", ts);
		verify(mockModel, times(1)).addAttribute("skillLevels", SkillLevel.values());	
	}
	
	@Test
	public void test_updateSkillLevel()
	{
		TraineeSkill ts = new TraineeSkill();
		ts.setId(1L);
		
		TraineeSkill ts2 = new TraineeSkill();
		ts2.setId(1L);
		ts2.setTrainee(user);
		
		when(mockTraineeSkillService.getTraineeSkillById(1L)).thenReturn(ts);
		
		assertEquals("redirect:/traineeSkill", controller.updateSkillLevel(ts2));
		verify(mockTraineeSkillService, times(1)).save(ts2);
	}
	
	@Test
	public void test_getChangeSkillLevelPage()
	{
		assertEquals("Skill/changeSkillLevel", controller.getChangeSkillLevelPage());
	}
	
	@Test
	public void test_getEditProfilePage_when_not_logged_in()
	{
		when(mockSession.getAttribute("user")).thenReturn(null);
		assertEquals("redirect:/login", controller.getEditProfilePage(1L, mockSession, mockModel));
	}
	
	@Test
	public void test_getEditProfilePage_logged_in()
	{
		when(mockSession.getAttribute("user")).thenReturn(user);
		
		assertEquals("UserProfile/editProfile", controller.getEditProfilePage(1L, mockSession, mockModel));
		verify(mockModel, times(1)).addAttribute("regions", Region.values());
		verify(mockModel, times(1)).addAttribute("streams", Stream.values());
	}
	
	@Test
	public void test_getResetPasswordPage_not_logged_in()
	{
		when(mockSession.getAttribute("user")).thenReturn(null);
		assertEquals("redirect:/login", controller.getResetPasswordPage(mockSession, mockModel));
	}
	
	@Test
	public void test_getResetPasswordPage_logged_in()
	{
		when(mockSession.getAttribute("user")).thenReturn(user);
		assertEquals("UserProfile/resetPassword", controller.getResetPasswordPage(mockSession, mockModel));
	}
	
	
}
