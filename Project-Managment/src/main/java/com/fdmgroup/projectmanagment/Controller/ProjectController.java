package com.fdmgroup.projectmanagment.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fdmgroup.projectmanagment.Model.Notification;
import com.fdmgroup.projectmanagment.Model.Project;
import com.fdmgroup.projectmanagment.Model.ProjectRole;
import com.fdmgroup.projectmanagment.Model.ProjectType;
import com.fdmgroup.projectmanagment.Model.Role;
import com.fdmgroup.projectmanagment.Model.Skill;
import com.fdmgroup.projectmanagment.Model.Task;
import com.fdmgroup.projectmanagment.Model.User;
import com.fdmgroup.projectmanagment.Service.NotificationService;

import com.fdmgroup.projectmanagment.Service.ProjectRoleService;
import com.fdmgroup.projectmanagment.Service.ProjectService;
import com.fdmgroup.projectmanagment.Service.SkillService;
import com.fdmgroup.projectmanagment.Service.TaskService;
import com.fdmgroup.projectmanagment.Service.TraineeSkillService;
import com.fdmgroup.projectmanagment.Service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class ProjectController {

	private ProjectService projectService;
	private ProjectRoleService projectRoleService;
	private SkillService skillService;
	private UserService userService;
	private NotificationService notificationService;
	private TaskService taskService;
	private TraineeSkillService traineeSkillService;

	public ProjectController(ProjectService projectService, ProjectRoleService projectRoleService,
			SkillService skillService, UserService userService, NotificationService notificationService,
			TaskService taskService, TraineeSkillService traineeSkillService) {
		super();
		this.projectService = projectService;
		this.projectRoleService = projectRoleService;
		this.skillService = skillService;
		this.userService = userService;
		this.notificationService = notificationService;
		this.taskService = taskService;
		this.traineeSkillService = traineeSkillService;
	}

	private boolean renderUser(HttpSession session, Model model) {

		if (session.getAttribute("user") == null) {
			return false;
		}

		User user = (User) session.getAttribute("user");
		model.addAttribute("user", user);
		model.addAttribute("hasNotif", notificationService.countNotifactionsByUser(user));

		return true;
	}

	public boolean isMember(User user, Long pid) {

		List<ProjectRole> roles = projectRoleService.findProjectRolesByParticipant(user);
		List<Project> projects = projectService.findByCreator(user);

		for (ProjectRole role : roles) {
			if (role.getProject().getProjectId() == pid) {
				return true;
			}
		}

		for (Project project : projects) {
			if (project.getProjectId() == pid) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Project Homepage
	 */
	@GetMapping("/project/{id}")
	public String getProjectPage(@PathVariable Long id, HttpSession session, Model model) {

		if (!renderUser(session, model)) {
			return "redirect:/login";
		}

		User user = (User) session.getAttribute("user");
		model.addAttribute("isMember", isMember(user, id));

		Project project = projectService.findProjectById(id);

		if (user.getId() == project.getCreator().getId()) {
			model.addAttribute("isCreator", true);
		}

		model.addAttribute("project", project);

		if(user.getRole() == Role.Admin){
			return "redirect:/projectSingleView/" + id;
		}
		return "Project/project";
	}

	/**
	 * Edit project
	 */
	@GetMapping("/project/{id}/edit")
	public String editProject(@PathVariable long id, HttpSession session, Model model) {

		if (!renderUser(session, model)) {
			return "redirect:/login";
		}

		Project project = projectService.findProjectById(id);
		model.addAttribute("project", project);

		return "Project/editProject";
	}

	/**
	 * Roles
	 */
	// Add roles
	@GetMapping("/project/{id}/addRole")
	public String getAddRole(@PathVariable Long id, HttpSession session, Model model) {

		if (!renderUser(session, model)) {
			return "redirect:/login";
		}

		User user = (User) session.getAttribute("user");

		model.addAttribute("user", user);

		model.addAttribute("isEdit", false);

		model.addAttribute("project", projectService.findProjectById(id));
		model.addAttribute("projectRole", new ProjectRole());

		model.addAttribute("approvedSkills", skillService.getAllApprovedSkills());

		return "Project/addRole";
	}

	// Edit roles
	@GetMapping("/project/{pid}/editRole/{rid}")
	public String getEditRole(@PathVariable Long pid, @PathVariable Long rid, HttpSession session, Model model) {

		if (!renderUser(session, model)) {
			return "redirect:/login";
		}

		User user = (User) session.getAttribute("user");
		model.addAttribute("user", user);
		model.addAttribute("isEdit", true);
		model.addAttribute("project", projectService.findProjectById(pid));

		ProjectRole r = projectRoleService.findProjectRoleById(rid);

		List<Skill> foundSkills = r.getSkills();
		for (Skill s : foundSkills) {
			List<ProjectRole> roles = s.getProjectRoles();
			roles.remove(r);
			s.setProjectRoles(roles);
			skillService.save(s);
		}

		r.setSkills(new ArrayList<Skill>());

		model.addAttribute("projectRole", r);
		model.addAttribute("approvedSkills", skillService.getAllApprovedSkills());

		if (user.getRole() == Role.Admin) {
			return "Dashboard/Project/editRolesAdmin";
		}

		return "Project/addRole";
	}

	@PostMapping("/saveProjectRole")
	public String processAddRole(HttpSession session, @RequestParam Long projectId,
			@ModelAttribute ProjectRole projectRole) {

		User user = (User) session.getAttribute("user");

		List<Skill> foundSkills = projectRole.getSkills();
		foundSkills.remove(null);
		projectRole.setSkills(foundSkills);

		Project project = projectService.findProjectById(projectId);

		// Add all mapping of project role and the current skills
		for (Skill s : foundSkills) {
			List<ProjectRole> roles = s.getProjectRoles();
			roles.add(projectRole);
			s.setProjectRoles(roles);
		}

		projectRole.setProject(project);

		this.projectRoleService.save(projectRole);

		for (Skill s : foundSkills) {
			for (User trainee : traineeSkillService.getAllTraineesWithSkill(s)) {
				createNotificationSkill(s, projectRole, trainee);
			}
		}

		if (user.getRole() == Role.Admin) {
			return "redirect:/projectSingleView/" + projectId;

		}
		return "redirect:/project/" + projectId;
	}

	private void createNotificationSkill(Skill skill, ProjectRole role, User user) {

		String title = "New Project Available";

		String body = "A role " + role.getTitle() + " requiring " + skill.getTitle() + " skill has been created in "
				+ role.getProject().getTitle() + " on " + getTime() + ".";

		Notification notif = new Notification(title, body);
		notif.setOwner(user);

		this.notificationService.save(notif);

	}

	private String getTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd/MM/yyyy ");
		Date date = new Date();
		return formatter.format(date);
	}

	// Delete Roles
	@GetMapping("/project/{pid}/deleteRole/{rid}")
	public String getDeleteRole(@PathVariable Long pid, @PathVariable Long rid, HttpSession session, Model model) {

		User user = (User) session.getAttribute("user");

		createNotificationRemove(projectRoleService.findProjectRoleById(rid), user);

		projectRoleService.deleteProjectRoleById(rid);

		if (user.getRole() == Role.Admin) {
			return "redirect:/projectSingleView/" + pid;
		}

		return "redirect:/project/" + pid;
	}

	// Assign + Invite
	@GetMapping("/project/{pid}/assign/{rid}")
	public String getAssignPage(@PathVariable Long pid, @PathVariable Long rid, HttpSession session, Model model) {

		if (!renderUser(session, model)) {
			return "redirect:/login";
		}

		Project project = projectService.findProjectById(pid);
		ProjectRole role = projectRoleService.findProjectRoleById(rid);

		role.addApplicants(project.getCreator());

		model.addAttribute("project", project);
		model.addAttribute("projectRole", role);
		return "Project/assign";
	}

	@GetMapping("/project/{pid}/invite/{rid}")
	public String getInvitePage(@PathVariable Long pid, @PathVariable Long rid, HttpSession session, Model model) {
		if (!renderUser(session, model)) {
			return "redirect:/login";
		}

		User user = (User) session.getAttribute("user");
		Project project = projectService.findProjectById(pid);
		ProjectRole role = projectRoleService.findProjectRoleById(rid);

		String title = "You're invited!";
		String text = user.getFirstName() + " " + user.getLastName() + " invited you to join " + project.getTitle()
				+ " as " + role.getTitle() + " on " + getTime() + ".";

		model.addAttribute("projectRole", role);
		model.addAttribute("notification", new Notification(title, text));

		model.addAttribute("trainees", userService.getAllTraineesExcept(user.getId()));
		return "Project/invite";
	}

	@PostMapping("/inviteRole")
	public String processInvitation(
			@RequestParam Long pid,
			@ModelAttribute Notification notif) {

		notificationService.save(notif);

		return "redirect:/project/" + pid;
	}

	// Assign participant
	@PostMapping("/assignRole")
	public String assignRole(@ModelAttribute ProjectRole role) {
		projectRoleService.save(role);

		User user = role.getParticipant();
		List<ProjectRole> roles = user.getProjectRoles();
		roles.add(role);
		user.setProjectRoles(roles);

		userService.save(user);

		createNotificationAssign(role, user);

		return "redirect:/project/" + role.getProject().getProjectId();
	}

	private void createNotificationAssign(ProjectRole role, User user) {
		String title = "Application successful";
		String body = "You've been assigned with a new role - " + role.getTitle() + " in "
				+ role.getProject().getTitle() + " on " + getTime() + ".";

		Notification notif = new Notification(title, body);
		notif.setOwner(user);

		notificationService.save(notif);
	}

	// Remove
	@GetMapping("/project/{pid}/remove/{rid}")
	public String removeParticipantFromRole(@PathVariable Long pid, @PathVariable Long rid, HttpSession session,
			Model model) {

		if (!renderUser(session, model)) {
			return "redirect:/login";
		}

		ProjectRole role = projectRoleService.findProjectRoleById(rid);
		User user = role.getParticipant();

		List<ProjectRole> roles = user.getProjectRoles();
		roles.remove(role);

		user.setProjectRoles(roles);
		role.setParticipant(null);

		projectRoleService.save(role);
		userService.save(user);

		createNotificationRemove(role, user);

		return "redirect:/project/" + pid;
	}

	private void createNotificationRemove(ProjectRole role, User owner) {
		String title = "Removed";
		String body = "You've been removed from " + role.getProject().getTitle() + ".";

		Notification notif = new Notification(title, body);
		notif.setOwner(owner);

		notificationService.save(notif);
	}

	// Apply
	@GetMapping("/project/{pid}/apply/{rid}")
	public String applyForRole(@PathVariable Long pid, @PathVariable Long rid, HttpSession session, Model model) {
		if (!renderUser(session, model)) {
			return "redirect:/login";
		}

		ProjectRole role = projectRoleService.findProjectRoleById(rid);
		User user = (User) session.getAttribute("user");
		Project project = projectService.findProjectById(pid);

		Set<User> applicants = role.getApplicants();
		applicants.add(user);
		role.setApplicants(applicants);
		projectRoleService.save(role);

		List<ProjectRole> projectRoles = project.getProjectRoles();
		for (ProjectRole r : projectRoles) {
			if (r.getId() == role.getId()) {
				projectRoles.remove(r);
				projectRoles.add(role);
				break;
			}
		}
		project.setProjectRoles(projectRoles);
		projectService.saveProject(project);

		createNotificationApply(role, user, project.getCreator());
		for (ProjectRole r : project.getProjectRoles()) {
			if (r.getParticipant() != null) {
				createNotificationApply(role, user, r.getParticipant());
			}
		}

		return "redirect:/project/" + pid;
	}

	private void createNotificationApply(ProjectRole role, User user, User owner) {
		String title = "New Applicant";
		String body = user.getFirstName() + " " + user.getLastName() + " applies Role - " + role.getTitle() + " in "
				+ role.getProject().getTitle() + " on " + getTime() + ".";

		Notification notif = new Notification(title, body);
		notif.setOwner(owner);

		notificationService.save(notif);
	}

	/**
	 * Create Project
	 */
	@GetMapping("/create/project")
	public String getCreateProject(HttpSession session, Model model) {

		if (session.getAttribute("user") == null) {
			return "redirect:/login";
		}

		User user = (User) session.getAttribute("user");
		model.addAttribute("user", user);

		Project project = new Project();

		if (user.getRole() == Role.Sales) {
			project.setType(ProjectType.Client);
		} else {
			project.setType(ProjectType.Mock);
		}

		model.addAttribute("project", project);
		return "Project/createProject";
	}

	@PostMapping("/saveProject")
	public String processProject(HttpSession session, @ModelAttribute Project project) {

		User user = (User) session.getAttribute("user");
		project.setCreator(user);
		this.projectService.saveProject(project);

		return "redirect:/profile/" + user.getId();
	}

	@PostMapping("/edit")
	public String processEditProject(@ModelAttribute Project project) {

		this.projectService.saveProject(project);
		return "redirect:/profile";
	}

	/**
	 * Tasks
	 */
	// Create task
	@GetMapping("/project/{id}/createTask")
	public String getCreateTask(@PathVariable Long id, HttpSession session, Model model) {

		if (!renderUser(session, model)) {
			return "redirect:/login";
		}

		User user = (User) session.getAttribute("user");

		model.addAttribute("user", user);

		model.addAttribute("isEdit", false);

		model.addAttribute("project", projectService.findProjectById(id));
		model.addAttribute("task", new Task());

		return "Project/createTask";
	}

	// Edit tasks
	@GetMapping("/project/{pid}/editTask/{tid}")
	public String getEditTask(@PathVariable Long pid, @PathVariable Long tid, HttpSession session, Model model) {

		if (!renderUser(session, model)) {
			return "redirect:/login";
		}

		User user = (User) session.getAttribute("user");
		model.addAttribute("user", user);
		model.addAttribute("isEdit", true);
		model.addAttribute("project", projectService.findProjectById(pid));

		Task t = taskService.findTaskById(tid);

		model.addAttribute("task", t);

		if (user.getRole() == Role.Admin) {
			return "Dashboard/Project/projectCreateTask";
		}

		return "Project/createTask";
	}

	// create notification for new tasks
	private void createNotificationTask(Task task, User user) {
		String title = "New Task";
		String body = "There is a new task - " + task.getTitle() + " added in " + task.getProject().getTitle() + " on "
				+ getTime() + ".";

		Notification notif = new Notification(title, body);
		notif.setOwner(user);

		notificationService.save(notif);
	}

	// save tasks
	@PostMapping("/saveTask")
	public String processCreateTask(HttpSession session, @RequestParam Long projectId, @ModelAttribute Task task) {

		User user = (User) session.getAttribute("user");

		Project project = projectService.findProjectById(projectId);

		task.setProject(project);

		this.taskService.save(task);

		if (user.getRole() == Role.Admin) {
			return "redirect:/projectSingleView/" + projectId;
		}

		createNotificationTask(task, user);

		return "redirect:/project/" + projectId;

	}

	// Delete Tasks
	@GetMapping("/project/{pid}/deleteTask/{tid}")
	public String getDeleteTask(@PathVariable Long pid, @PathVariable Long tid, HttpSession session, Model model) {

		Task task = taskService.findTaskById(tid);

		taskService.deleteTask(task);

		User user = (User) session.getAttribute("user");
		if (user.getRole() == Role.Admin) {
			return "redirect:/projectSingleView/" + pid;
		}

		return "redirect:/project/" + pid;
	}

	/**
	 * Gets all projects for admin
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/projects")
	public String getProjects(Model model, HttpSession httpSession) {
		List<Project> projects = projectService.getAllProjects();
		httpSession.getAttribute("user");
		model.addAttribute("user", httpSession.getAttribute("user"));
		model.addAttribute("listOfProjects", projects);

		return "Dashboard/Project/allProjects";
	}

	@GetMapping("/project/{id}/delete")
	public String deleteUser(Model model, HttpSession httpSession, @PathVariable Long id) {

		if (!renderUser(httpSession, model)) {
			return "redirect:/login";
		}

		Project project = projectService.findProjectById(id);

		for (ProjectRole r : project.getProjectRoles()) {
			this.projectRoleService.delete(r);
		}

		this.projectService.deleteByProjectId(id);

		User user = (User) httpSession.getAttribute("user");
		if (user.getRole() == Role.Admin) {
			return "redirect:/projects";
		}

		return "redirect:/profile/" + user.getId();
	}

	@GetMapping("/updateAll/project/{id}")
	public String editAllUser(@PathVariable Long id, Model model, HttpSession httpSession) {
		Project project = projectService.findProjectById(id);
		model.addAttribute("project", project);

		return "Dashboard/Project/editAllProjects";
	}

	@PostMapping("/saveAllProject")
	public String saveAllProject(@ModelAttribute Project project) {
		this.projectService.saveProject(project);

		return "redirect:/projects";
	}

	@GetMapping("/admin/createProject")
	public String adminCreateProject(Model model) {
		model.addAttribute("project", new Project());

		return "Dashboard/Project/adminCreateProject";
	}

	// TODO: 9/01/2023 Change method name
	@PostMapping("/saveAdminCreateProject")
	public String saveAdminUser(@ModelAttribute Project project) {
		this.projectService.saveProject(project);

		return "redirect:/projects";
	}

	@GetMapping("/projectSingleView/{id}")
	public String getProjectAdminPage(@PathVariable Long id, HttpSession httpSession, Model model) {
		User user = (User) httpSession.getAttribute("user");

		if (httpSession.getAttribute("user") == null) {
			return "redirect:/login";
		}

		model.addAttribute("user", user);

		Project project = projectService.findProjectById(id);
		List<ProjectRole> roles = projectRoleService.findProjectRolesByProject(project);

		model.addAttribute("project", project);
		model.addAttribute("roles", roles);
		model.addAttribute("user", user);
		httpSession.getAttribute("user");

		return "Dashboard/Project/projectSingleV";
	}

	@GetMapping("/projectSingleView/{id}/addRole")
	public String getAddRoleAdmin(@PathVariable Long id, HttpSession session, Model model) {

		if (!renderUser(session, model)) {
			return "redirect:/login";
		}

		User user = (User) session.getAttribute("user");

		model.addAttribute("user", user);

		model.addAttribute("isEdit", false);

		model.addAttribute("project", projectService.findProjectById(id));
		model.addAttribute("projectRole", new ProjectRole());

		model.addAttribute("approvedSkills", skillService.getAllApprovedSkills());

		return "Dashboard/Project/addRolesAdmin";
	}

	@GetMapping("/projectSingleView/{id}/viewFiles")
	public String viewUploadedFilesAdmin(@PathVariable Long id, HttpSession session, Model model) {

		User user = (User) session.getAttribute("user");

		model.addAttribute("user", user);

		model.addAttribute("project", projectService.findProjectById(id));
		model.addAttribute("projectRole", new ProjectRole());

		model.addAttribute("approvedSkills", skillService.getAllApprovedSkills());

		return "Dashboard/Project/projectFilesUploaded";
	}

	@GetMapping("/projectSingleView/{id}/tasks")
	public String viewTasksAdmin(@PathVariable Long id, HttpSession session, Model model) {
		List<Task> adminTasks = this.taskService.findTasksByProjectId(id);
		model.addAttribute("project", projectService.findProjectById(id));

		model.addAttribute("adminTasks", adminTasks);
		return "Dashboard/Project/viewProjectTasks";
	}

	/**
	 * Create tasks for admin
	 * 
	 * @param id
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping("/projectSingleView/{id}/createTask")
	public String getCreateTaskAdmin(@PathVariable Long id, HttpSession session, Model model) {

		User user = (User) session.getAttribute("user");

		model.addAttribute("user", user);

		model.addAttribute("isEdit", false);
		model.addAttribute("project", projectService.findProjectById(id));
		model.addAttribute("task", new Task());

		return "Dashboard/Project/projectCreateTask";
	}
}