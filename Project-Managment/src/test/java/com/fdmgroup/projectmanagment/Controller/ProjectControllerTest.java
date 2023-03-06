package com.fdmgroup.projectmanagment.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import com.fdmgroup.projectmanagment.Model.Notification;
import com.fdmgroup.projectmanagment.Model.Project;
import com.fdmgroup.projectmanagment.Model.ProjectRole;
import com.fdmgroup.projectmanagment.Model.Role;
import com.fdmgroup.projectmanagment.Model.Skill;
import com.fdmgroup.projectmanagment.Model.Task;
import com.fdmgroup.projectmanagment.Model.User;
import com.fdmgroup.projectmanagment.Service.NotificationService;
import com.fdmgroup.projectmanagment.Service.ProjectFileService;
import com.fdmgroup.projectmanagment.Service.ProjectRoleService;
import com.fdmgroup.projectmanagment.Service.ProjectService;
import com.fdmgroup.projectmanagment.Service.SkillService;
import com.fdmgroup.projectmanagment.Service.TaskService;
import com.fdmgroup.projectmanagment.Service.TraineeSkillService;
import com.fdmgroup.projectmanagment.Service.UserService;

import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
public class ProjectControllerTest {
	@Mock
    Model model;
	
    @Mock
    HttpSession mockSession;
    
    @Mock
    ProjectService mockProjectService;
    @Mock
    ProjectRoleService mockProjectRoleService;
    @Mock
    SkillService mockSkillService;
    @Mock
    UserService mockUserService;
    @Mock
    NotificationService mockNotificationService;
    @Mock
    ProjectFileService mockFileService;
    @Mock
    TaskService mockTaskService;
    @Mock
    TraineeSkillService mockTraineeSkillService;
    
    @Mock
    User mockUser;
    @Mock
    Task mockTask;
    @Mock
    Skill mockSkill;
    @Mock
    List<Skill> mockSkills;
    @Mock
    List<User> mockTrainees;
    @Mock
    Project mockProject;
    @Mock
    ProjectRole mockProjectRole;
    @Mock
    Notification mockNotification;
    
    @InjectMocks
    ProjectController projectController;
    
    String pageReturned;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockProjectRole.setProject(mockProject);
    }
    
    /**
     * Test for tasks
     */
    @Test
    void test_getCreateTask_null_user() {
    	
    	pageReturned = projectController.getCreateTask(null, mockSession, model);
    	
    	assertEquals("redirect:/login", pageReturned);
    }
    
    @Test
    void test_getCreateTask() {
    	
    	Project project = new Project();
    	Long id = project.getProjectId();
    	when(mockSession.getAttribute("user")).thenReturn(mockUser);
    	pageReturned = projectController.getCreateTask(id, mockSession, model);
    	
    	
    	verify(mockSession, times(3)).getAttribute("user");
    	verify(model, times(2)).addAttribute("user", mockUser);
    	verify(model, times(1)).addAttribute("isEdit", false);
    	verify(model, times(1)).addAttribute("project", mockProjectService.findProjectById(id));
    	
    	assertEquals("Project/createTask", pageReturned);
    }
    
    @Test
    void test_getEditTask_null_user() {

    	pageReturned = projectController.getEditTask(null, null, mockSession, model);
    	
    	assertEquals("redirect:/login", pageReturned);
    }
    
    @Test
    void test_getEditTask() {
    	
    	Project project = new Project();
    	Task task = new Task();
    	Long pid = project.getProjectId();
    	Long tid = task.getId();
    	
    	when(mockSession.getAttribute("user")).thenReturn(mockUser);
    	pageReturned = projectController.getEditTask(pid, tid, mockSession, model);
    	
    	
    	verify(mockSession, times(3)).getAttribute("user");
    	verify(model, times(2)).addAttribute("user", mockUser);
    	verify(model, times(1)).addAttribute("isEdit", true);
    	verify(model, times(1)).addAttribute("project", mockProjectService.findProjectById(pid));
    	
    	assertEquals("Project/createTask", pageReturned);
    	
    }
    
    @Test
    void test_getEditTask_Admin() {
    	
    	Project project = new Project();
    	Task task = new Task();
    	Long pid = project.getProjectId();
    	Long tid = task.getId();
    	
    	when(mockSession.getAttribute("user")).thenReturn(mockUser);
    	when(mockUser.getRole()).thenReturn(Role.Admin);
    	
    	pageReturned = projectController.getEditTask(pid, tid, mockSession, model);
    	
    	verify(mockSession, times(3)).getAttribute("user");
    	verify(model, times(2)).addAttribute("user", mockUser);
    	verify(model, times(1)).addAttribute("isEdit", true);
    	verify(model, times(1)).addAttribute("project", mockProjectService.findProjectById(pid));
    	assertEquals("Dashboard/Project/projectCreateTask", pageReturned);
    }
    
    @Test
    void test_saveTask() {
    	
    	Long id = mockProject.getProjectId();
    	
    	when(mockSession.getAttribute("user")).thenReturn(mockUser);
    	when(mockTask.getProject()).thenReturn(mockProject);
    	
    	pageReturned = projectController.processCreateTask(mockSession, id, mockTask);
    	
    	assertEquals("redirect:/project/" + id, pageReturned);
    }
    

    @Test
    void test_saveTask_Admin() {
    	
    	Long id = mockProject.getProjectId();
    	
    	when(mockSession.getAttribute("user")).thenReturn(mockUser);
    	when(mockUser.getRole()).thenReturn(Role.Admin);
    	
    	pageReturned = projectController.processCreateTask(mockSession, id, mockTask);
    	
    	assertEquals("redirect:/projectSingleView/" + id, pageReturned);
    }
    
    @Test
    void test_deleteTask() {
    	
    	Long pid = mockProject.getProjectId();
    	Long tid = mockTask.getId();
    	
    	when(mockSession.getAttribute("user")).thenReturn(mockUser);
    	
    	pageReturned = projectController.getDeleteTask(pid, tid, mockSession, model);
    	
    	assertEquals("redirect:/project/" + pid, pageReturned);
    	
    }
    
    @Test
    void test_deleteTask_Admin() {
    	
    	Long pid = mockProject.getProjectId();
    	Long tid = mockTask.getId();
    	
    	when(mockSession.getAttribute("user")).thenReturn(mockUser);
    	when(mockUser.getRole()).thenReturn(Role.Admin);
    	
    	pageReturned = projectController.getDeleteTask(pid, tid, mockSession, model);
    	
    	assertEquals("redirect:/projectSingleView/" + pid, pageReturned);
    	
    }
    
    /**
     * Test for project
     */
    @Test
    public void test_isMember_user_is_memeber()
    {
    	User u = new User();
    	User u2 = new User();
    	
    	Project p = new Project();
    	p.setCreator(u2);
    	p.setProjectId(1L);
    	
    	ProjectRole pr1 = new ProjectRole();
    	ProjectRole pr2 = new ProjectRole();
    	pr2.setParticipant(u);
    	pr1.setProject(p);
    	pr2.setProject(p);
    	    	
    	when(mockProjectRoleService.findProjectRolesByParticipant(u)).thenReturn(new ArrayList<ProjectRole>(List.of(pr1)));
    	when(mockProjectService.findByCreator(u)).thenReturn(new ArrayList<Project>(List.of(p)));
    	
    	assertTrue(projectController.isMember(u, 1L));
    }
    
    @Test
    public void test_isMember_user_is_creator()
    {
    	User u = new User();
    	User u2 = new User();
    	
    	Project p = new Project();
    	p.setCreator(u);
    	p.setProjectId(1L);

    	Project p2 = new Project();
    	p2.setCreator(u2);
    	p2.setProjectId(2L);
    	
    	ProjectRole pr1 = new ProjectRole();
    	ProjectRole pr2 = new ProjectRole();
    	pr1.setProject(p);
    	pr2.setProject(p);
    	
    	u2.setProjects(new ArrayList<Project>(List.of(p, p2)));
    	    	
    	when(mockProjectRoleService.findProjectRolesByParticipant(u2)).thenReturn(new ArrayList<ProjectRole>());
    	when(mockProjectService.findByCreator(u)).thenReturn(new ArrayList<Project>(List.of(p, p2)));
    	
    	assertTrue(projectController.isMember(u, 1L));
    }
    
    @Test
    public void test_isMemeber_user_is_not_creator_or_member()
    {
    	User u = new User();
    	User u2 = new User();

    	Project p = new Project();
    	p.setProjectId(1L);
    	p.setCreator(u);
    	Project p2 = new Project();
    	p2.setProjectId(2L);
    	p2.setCreator(u2);
    	
    	ProjectRole pr1 = new ProjectRole();
    	pr1.setProject(p);
    	ProjectRole pr2 = new ProjectRole();
    	pr2.setProject(p2);
    	
    	when(mockProjectRoleService.findProjectRolesByParticipant(u2)).thenReturn(new ArrayList<ProjectRole>(List.of(pr2)));
    	when(mockProjectService.findByCreator(u2)).thenReturn(new ArrayList<Project>(List.of(p2)));
    	
    	assertFalse(projectController.isMember(u2, 1L));
    }
    
    
    @Test
    void test_getProjectPage_null_user() {
    	
    	pageReturned = projectController.getProjectPage(null, mockSession, model);
    	
    	assertEquals("redirect:/login", pageReturned);
    }
    
    @Test
    void test_getProjectPage_isCreator() {
    	
    	User user = new User();
    	user.setId(1L);
    	Project project1 = new Project();
    	project1.setProjectId(1L);
    	project1.setCreator(user);
    	user.setProjects(List.of(project1));
    	    	
    	when(mockSession.getAttribute("user")).thenReturn(user);
    	when(mockProjectService.findProjectById(1L)).thenReturn(project1);
    	
    	pageReturned = projectController.getProjectPage(1L, mockSession, model);

    	verify(model, times(1)).addAttribute("project", mockProjectService.findProjectById(1L));
    	verify(model, times(1)).addAttribute("isCreator", true);
    	
    	assertEquals("Project/project", pageReturned);
    }
    
    @Test
    void test_getProjectPage_isNotCreator()
    {
    	
    	User user = new User();
    	user.setId(1L);
    	User user2 = new User();
    	user2.setId(2L);
    	
    	Project project1 = new Project();
    	project1.setProjectId(1L);
    	project1.setCreator(user2);
    	user.setProjects(List.of(project1));
    	    	
    	when(mockSession.getAttribute("user")).thenReturn(user);
    	when(mockProjectService.findProjectById(1L)).thenReturn(project1);
    	
    	pageReturned = projectController.getProjectPage(1L, mockSession, model);

    	verify(model, times(1)).addAttribute("project", mockProjectService.findProjectById(1L));
    	verify(model, never()).addAttribute("isCreator", true);
    	
    	assertEquals("Project/project", pageReturned);
    }
    
    
    @Test
    void test_getProjectPage_isAdmin() {
    	
    	Long id = mockProject.getProjectId();
    	
    	when(mockSession.getAttribute("user")).thenReturn(mockUser);
    	when(mockProjectService.findProjectById(id)).thenReturn(mockProject);
    	when(mockProject.getCreator()).thenReturn(mockUser);
    	when(mockUser.getRole()).thenReturn(Role.Admin);
    	
    	pageReturned = projectController.getProjectPage(id, mockSession, model);
    	

    	verify(model, times(1)).addAttribute("project", mockProjectService.findProjectById(id));


    	assertEquals("redirect:/projectSingleView/" + id, pageReturned);
    }
    
    @Test
    void test_editProject_null_user() {
    	
    	pageReturned = projectController.editProject(0, mockSession, model);
    	
    	assertEquals("redirect:/login", pageReturned);
    }
    
    @Test
    void test_editProject() {
    	
    	long id = mockProject.getProjectId();
    	
    	when(mockSession.getAttribute("user")).thenReturn(mockUser);
    	pageReturned = projectController.editProject(id, mockSession, model);

    	assertEquals("Project/editProject", pageReturned);
    }
    
    @Test
    void test_createProject_null_user() {
    	
    	when(mockSession.getAttribute("user")).thenReturn(null);
    	
    	pageReturned = projectController.getCreateProject(mockSession, model);
    	
    	assertEquals("redirect:/login", pageReturned);
    }
    
    
    @Test
    void test_createProject() {
    	
    	when(mockSession.getAttribute("user")).thenReturn(mockUser);
    	
    	pageReturned = projectController.getCreateProject(mockSession, model);
    	
    	verify(model, times(1)).addAttribute("user", mockUser);
    	assertEquals("Project/createProject", pageReturned);
    	
    }
    
    @Test
    void test_createProject_Sales() {
    	
    	when(mockSession.getAttribute("user")).thenReturn(mockUser);
    	when(mockUser.getRole()).thenReturn(Role.Sales);
    	
    	pageReturned = projectController.getCreateProject(mockSession, model);
    	
    	verify(model, times(1)).addAttribute("user", mockUser);
    	assertEquals("Project/createProject", pageReturned);
    	
    }
    
    @Test
    void test_processProject() {
    	
    	when(mockSession.getAttribute("user")).thenReturn(mockUser);
    	
    	pageReturned = projectController.processProject(mockSession, mockProject);
    	
    	assertEquals("redirect:/profile/" + mockUser.getId(), pageReturned);
    }
    
    @Test
    void test_processEditProject() {
    	
    	pageReturned = projectController.processEditProject(mockProject);
    	
    	assertEquals("redirect:/profile", pageReturned);
    }
    
    /**
	 * Test for roles
	 */
    @Test
    void test_getAddRole_null_user() {
    	
    	pageReturned = projectController.getAddRole(null, mockSession, model);
    	
    	assertEquals("redirect:/login", pageReturned);
    }
    
    @Test
    void test_getAddRole() {
    	
    	Long pid = mockProject.getProjectId();
    	
    	when(mockSession.getAttribute("user")).thenReturn(mockUser);
    	
    	pageReturned = projectController.getAddRole(null, mockSession, model);
    	
    	verify(model, times(2)).addAttribute("user", mockUser);
    	verify(model, times(1)).addAttribute("isEdit", false);
    	verify(model, times(1)).addAttribute("project", mockProjectService.findProjectById(pid));
    	verify(model, times(1)).addAttribute("approvedSkills", mockSkillService.getAllApprovedSkills());
    	assertEquals("Project/addRole", pageReturned);
    }
    
    @Test
    void test_getEditRole_null_user() {
    	
    	pageReturned = projectController.getEditRole(null, null, mockSession, model);
    	
    	assertEquals("redirect:/login", pageReturned);
    }
    
    @Test
    void test_getEditRole() {
    	
    	Long pid = 1L;
    	Long rid = 1L;
    	
    	Skill s1 = new Skill();
    	s1.setId(1L);
    	
    	ProjectRole pr1 = new ProjectRole();
    	pr1.setId(1L);
    	
    	ProjectRole pr2 = new ProjectRole();
    	pr2.setId(2L);
    	
    	pr1.setSkills(new ArrayList<Skill>(List.of(s1)));
    	
    	s1.setProjectRoles(new ArrayList<ProjectRole>(List.of(pr1, pr2)));
    	
    	when(mockSession.getAttribute("user")).thenReturn(mockUser);
    	when(mockProjectService.findProjectById(pid)).thenReturn(mockProject);
    	when(mockProjectRoleService.findProjectRoleById(rid)).thenReturn(pr1);
    	
    	pageReturned = projectController.getEditRole(pid, rid, mockSession, model);
    	
    	verify(model, times(2)).addAttribute("user", mockUser);
    	verify(model, times(1)).addAttribute("isEdit", true);
    	verify(model, times(1)).addAttribute("project", mockProjectService.findProjectById(pid));
    	verify(model, times(1)).addAttribute("approvedSkills", mockSkillService.getAllApprovedSkills());
    	
    	assertEquals("Project/addRole", pageReturned);
    }
    
    
    @Test
    void test_getEditRole_isAdmin() {
    	
    	Long pid = mockProject.getProjectId();
    	Long rid = mockProjectRole.getId();
    	
    	when(mockSession.getAttribute("user")).thenReturn(mockUser);
    	when(mockProjectService.findProjectById(pid)).thenReturn(mockProject);
    	when(mockProjectRoleService.findProjectRoleById(rid)).thenReturn(mockProjectRole);
    	when(mockUser.getRole()).thenReturn(Role.Admin);
    	
    	pageReturned = projectController.getEditRole(pid, rid, mockSession, model);
    	
    	assertEquals("Dashboard/Project/editRolesAdmin", pageReturned);
    }
    
    @Test
    void test_processAddRole() {
    	
    	Long pid = mockProject.getProjectId();
    	
    	Skill s1 = new Skill();
    	s1.setId(1L);
    	
    	ProjectRole pr1 = new ProjectRole();
    	pr1.setId(1L);
    	
    	ProjectRole pr2 = new ProjectRole();
    	pr2.setId(2L);
    	
    	pr1.setSkills(new ArrayList<Skill>(List.of(s1)));
    	
    	s1.setProjectRoles(new ArrayList<ProjectRole>(List.of(pr1, pr2)));
    	
    	User user1 = new User();
    	User user2 = new User();
    	
    	when(mockSession.getAttribute("user")).thenReturn(mockUser);
    	when(mockProjectService.findProjectById(pid)).thenReturn(mockProject);
    	when(mockTraineeSkillService.getAllTraineesWithSkill(s1)).thenReturn(new ArrayList<User>(List.of(user1, user2)));
    	
    	pageReturned = projectController.processAddRole(mockSession, pid, pr1);
    	
    	assertEquals("redirect:/project/" + pid, pageReturned);
    }
    
    @Test
    void test_processAddRole_isAdmin() {
    	
    	Long pid = mockProject.getProjectId();
    	
    	when(mockSession.getAttribute("user")).thenReturn(mockUser);
    	when(mockProjectService.findProjectById(pid)).thenReturn(mockProject);
    	when(mockUser.getRole()).thenReturn(Role.Admin);
    	
    	pageReturned = projectController.processAddRole(mockSession, pid, mockProjectRole);
    	
    	assertEquals("redirect:/projectSingleView/" + pid, pageReturned);
    }
    
    @Test
    void test_getDeleteRole() {
    	
    	Long pid = mockProject.getProjectId();
    	Long rid = mockProjectRole.getId();
    	
    	when(mockSession.getAttribute("user")).thenReturn(mockUser);
    	when(mockProjectRoleService.findProjectRoleById(rid)).thenReturn(mockProjectRole);
    	when(mockProjectRole.getProject()).thenReturn(mockProject);
    	
    	pageReturned = projectController.getDeleteRole(pid, rid, mockSession, model);
    	
    	assertEquals("redirect:/project/" + pid, pageReturned);
    }
    
    @Test
    void test_getDeleteRole_isAdmin() {
    	
    	Long pid = mockProject.getProjectId();
    	Long rid = mockProjectRole.getId();
    	
    	when(mockSession.getAttribute("user")).thenReturn(mockUser);
    	when(mockProjectRoleService.findProjectRoleById(rid)).thenReturn(mockProjectRole);
    	when(mockProjectRole.getProject()).thenReturn(mockProject);
    	when(mockUser.getRole()).thenReturn(Role.Admin);
    	
    	pageReturned = projectController.getDeleteRole(pid, rid, mockSession, model);
    	
    	assertEquals("redirect:/projectSingleView/" + pid, pageReturned);
    }
    
    @Test
    void test_getAssignPage_null_user() {
    	
    	pageReturned = projectController.getAssignPage(null, null, mockSession, model);
    	
    	assertEquals("redirect:/login", pageReturned);
    }
    
    @Test
    void test_getAssignPage() {
    	
    	Long pid = mockProject.getProjectId();
    	Long rid = mockProjectRole.getId();
    	
    	when(mockSession.getAttribute("user")).thenReturn(mockUser);
    	when(mockProjectService.findProjectById(pid)).thenReturn(mockProject);
    	when(mockProjectRoleService.findProjectRoleById(rid)).thenReturn(mockProjectRole);
    	
    	pageReturned = projectController.getAssignPage(pid, rid, mockSession, model);
    	
    	verify(model,times(1)).addAttribute("project", mockProject);
    	verify(model,times(1)).addAttribute("projectRole", mockProjectRole);
    	
    	assertEquals("Project/assign", pageReturned);
    }
    
    @Test
    void test_getInvitePage_null_user() {
    	
    	pageReturned = projectController.getInvitePage(null, null, mockSession, model);
    	
    	assertEquals("redirect:/login", pageReturned);
    }
    
    @Test
    void test_getInvitePage() {

    	Long pid = mockProject.getProjectId();
    	Long rid = mockProjectRole.getId();
    	
    	when(mockSession.getAttribute("user")).thenReturn(mockUser);
    	when(mockProjectService.findProjectById(pid)).thenReturn(mockProject);
    	when(mockProjectRoleService.findProjectRoleById(rid)).thenReturn(mockProjectRole);
    	
    	pageReturned = projectController.getInvitePage(pid, rid, mockSession, model);
    	
    	verify(model, times(1)).addAttribute("projectRole", mockProjectRole);
    	verify(model, times(1)).addAttribute("trainees", mockUserService.getAllTraineesExcept(mockUser.getId()));
    	
    	assertEquals("Project/invite", pageReturned);
    }
    
    @Test
    void test_processInvitation() {
    	
    	Long pid = mockProject.getProjectId();
    	
    	pageReturned = projectController.processInvitation(pid, mockNotification);
    	
    	assertEquals("redirect:/project/" + pid, pageReturned);
    }
    
    @Test
    void test_assignRole() {
    	
    	when(mockProjectRole.getParticipant()).thenReturn(mockUser);
    	when(mockProjectRole.getProject()).thenReturn(mockProject);
    	
    	pageReturned = projectController.assignRole(mockProjectRole);
    	
    	assertEquals("redirect:/project/" + mockProjectRole.getProject().getProjectId(), pageReturned);
    }
    
    @Test
    void test_removeParticipantFromRole_null_user() {
    	
    	pageReturned = projectController.removeParticipantFromRole(null, null, mockSession, model);
    	
    	assertEquals("redirect:/login", pageReturned);
    }
    
    @Test
    void test_removeParticipantFromRole() {
    	
    	Long pid = mockProject.getProjectId();
    	Long rid = mockProjectRole.getId();
    	
    	when(mockSession.getAttribute("user")).thenReturn(mockUser);
    	when(mockProjectRoleService.findProjectRoleById(rid)).thenReturn(mockProjectRole);
    	when(mockProjectRole.getParticipant()).thenReturn(mockUser);
    	when(mockProjectRole.getProject()).thenReturn(mockProject);
    	
    	pageReturned = projectController.removeParticipantFromRole(pid, rid, mockSession, model);
    	
    	assertEquals("redirect:/project/" + pid, pageReturned);
    }
    
    @Test
    void test_applyForRole_null_user() {
    	
    	pageReturned = projectController.applyForRole(null, null, mockSession, model);
    	
    	assertEquals("redirect:/login", pageReturned);
    }
    
    @Test
    void test_applyForRole() {
    	Long pid = mockProject.getProjectId();
    	Long rid = mockProjectRole.getId();
    	
    	Project project = new Project();
    	ProjectRole pr1 = new ProjectRole();
    	ProjectRole pr2 = new ProjectRole();
    	pr1.setApplicants(new HashSet<User>());
    	pr2.setApplicants(new HashSet<User>());
    	pr1.setProject(project);
    	pr2.setProject(project);
    	project.setProjectRoles(new ArrayList<ProjectRole>(List.of(pr1, pr2)));
    	
    	
    	when(mockSession.getAttribute("user")).thenReturn(mockUser);
    	when(mockProjectRoleService.findProjectRoleById(rid)).thenReturn(pr2);
    	when(mockProjectService.findProjectById(pid)).thenReturn(project);
    	
    	pageReturned = projectController.applyForRole(pid, rid, mockSession, model);
    	
    	assertEquals("redirect:/project/" + pid, pageReturned);
    }
    
    @Test
    void test_applyForRole_when_role_has_been_removed()
    {
    	Long pid = mockProject.getProjectId();
    	Long rid = mockProjectRole.getId();
    	
    	Project project = new Project();
    	ProjectRole pr1 = new ProjectRole();
    	ProjectRole pr2 = new ProjectRole();
    	pr1.setApplicants(new HashSet<User>());
    	pr2.setApplicants(new HashSet<User>());
    	pr1.setProject(project);
    	pr2.setProject(project);
    	User u = new User();
    	pr2.setParticipant(u);
    	
    	project.setProjectRoles(new ArrayList<ProjectRole>(List.of(pr1, pr2)));
    	
    	when(mockSession.getAttribute("user")).thenReturn(mockUser);
    	when(mockProjectRoleService.findProjectRoleById(rid)).thenReturn(pr2);
    	when(mockProjectService.findProjectById(pid)).thenReturn(project);
    	
    	pageReturned = projectController.applyForRole(pid, rid, mockSession, model);
    	
    	assertEquals("redirect:/project/" + pid, pageReturned);
    }
    
    /**
     * Test for Admin
     */
    @Test
    void test_getProjects() {
    	
    	List<Project> projects = new ArrayList<Project>();
    	
    	when(mockProjectService.getAllProjects()).thenReturn(projects);
    	when(mockSession.getAttribute("user")).thenReturn(mockUser);
    	
    	pageReturned = projectController.getProjects(model, mockSession);
    	
    	assertEquals("Dashboard/Project/allProjects", pageReturned);
    }
    
    @Test
    void test_deleteUser_null_user() {
    	
    	pageReturned = projectController.deleteUser(model, mockSession, null);
    	
    	assertEquals("redirect:/login", pageReturned);
    }
    
    @Test
    void test_deleteUser() {
    	
    	Long pid = mockProject.getProjectId();
    	
    	when(mockSession.getAttribute("user")).thenReturn(mockUser);
    	when(mockProjectService.findProjectById(pid)).thenReturn(mockProject);
    	
    	pageReturned = projectController.deleteUser(model, mockSession, pid);
    	
    	assertEquals("redirect:/profile/" + mockUser.getId(), pageReturned);
    }
    
    @Test
    void test_deleteUser_isAdmin() {
    	
    	Long pid = mockProject.getProjectId();
    	
    	when(mockSession.getAttribute("user")).thenReturn(mockUser);
    	when(mockProjectService.findProjectById(pid)).thenReturn(mockProject);
    	when(mockUser.getRole()).thenReturn(Role.Admin);
    	
    	pageReturned = projectController.deleteUser(model, mockSession, pid);
    	
    	assertEquals("redirect:/projects", pageReturned);
    }
    
    @Test
    void test_editAllUser() {
    	
    	Long pid = mockProject.getProjectId();
    	
    	when(mockProjectService.findProjectById(pid)).thenReturn(mockProject);
    	
    	pageReturned = projectController.editAllUser(pid, model, mockSession);
    	
    	assertEquals("Dashboard/Project/editAllProjects", pageReturned);
    }
    
    @Test
    void test_saveAllProject() {
    	
    	pageReturned = projectController.saveAllProject(mockProject);
    	
    	assertEquals("redirect:/projects", pageReturned);
    }
    
    @Test
    void test_adminCreateProject() {
    	
    	pageReturned = projectController.adminCreateProject(model);
    	
    	assertEquals("Dashboard/Project/adminCreateProject", pageReturned);
    }
    
    @Test
    void test_saveAdminUser() {
    	
    	pageReturned = projectController.saveAdminUser(mockProject);
    	
    	assertEquals("redirect:/projects", pageReturned);
    }
    
    @Test
    void test_getProjectAdminPage_null_user() {
    	
    	pageReturned = projectController.getProjectAdminPage(null, mockSession, model);
    	
    	assertEquals("redirect:/login", pageReturned);
    }
    
    @Test
    void test_getProjectAdminPage() {
    	
    	Long pid = mockProject.getProjectId();
    	List<ProjectRole> roles = new ArrayList<ProjectRole>();
    	
    	when(mockSession.getAttribute("user")).thenReturn(mockUser);
    	when(mockProjectService.findProjectById(pid)).thenReturn(mockProject);
    	when(mockProjectRoleService.findProjectRolesByProject(mockProject)).thenReturn(roles);
    	
    	pageReturned = projectController.getProjectAdminPage(pid, mockSession, model);
    	
    	assertEquals("Dashboard/Project/projectSingleV", pageReturned);
    }
    
    @Test
    void test_getAddRoleAdmin_null_user() {
    	
    	pageReturned = projectController.getAddRoleAdmin(null, mockSession, model);
    	
    	assertEquals("redirect:/login", pageReturned);
    }
    
    @Test
    void test_getAddRoleAdmin() {
    	
    	Long pid = mockProject.getProjectId();
    	
    	when(mockSession.getAttribute("user")).thenReturn(mockUser);
    	when(mockProjectService.findProjectById(pid)).thenReturn(mockProject);
    	
    	pageReturned = projectController.getAddRoleAdmin(pid, mockSession, model);
    	
    	verify(model, times(1)).addAttribute("isEdit", false);
    	verify(model, times(1)).addAttribute("project", mockProjectService.findProjectById(pid));
    	verify(model, times(1)).addAttribute("approvedSkills", mockSkillService.getAllApprovedSkills());
    	
    	assertEquals("Dashboard/Project/addRolesAdmin", pageReturned);
    }
    
    @Test
    void test_viewUploadedFilesAdmin() {
    	
    	Long pid = mockProject.getProjectId();
    	
    	when(mockSession.getAttribute("user")).thenReturn(mockUser);
    	when(mockProjectService.findProjectById(pid)).thenReturn(mockProject);
    	
    	pageReturned = projectController.viewUploadedFilesAdmin(pid, mockSession, model);
    	
    	assertEquals("Dashboard/Project/projectFilesUploaded", pageReturned);
    }
    
    @Test
    void test_viewTasksAdmin() {
    	
    	Long pid = mockProject.getProjectId();
    	
    	when(mockProjectService.findProjectById(pid)).thenReturn(mockProject);
    	
    	pageReturned = projectController.viewTasksAdmin(pid, mockSession, model);
    	
    	assertEquals("Dashboard/Project/viewProjectTasks", pageReturned);
    }
    
    @Test
    void test_getCreateTaskAdmin() {
    	
    	Long pid = mockProject.getProjectId();
    	
    	when(mockSession.getAttribute("user")).thenReturn(mockUser);
    	when(mockProjectService.findProjectById(pid)).thenReturn(mockProject);
    	
    	pageReturned = projectController.getCreateTaskAdmin(pid, mockSession, model);
    	
    	verify(model, times(1)).addAttribute("isEdit", false);
    	
    	assertEquals("Dashboard/Project/projectCreateTask", pageReturned);
    }
}
