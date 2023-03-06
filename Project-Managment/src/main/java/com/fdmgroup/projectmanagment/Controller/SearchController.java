package com.fdmgroup.projectmanagment.Controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

@Controller
public class SearchController {
	private SkillService skillService;
	private ProjectService projectService;
	private ProjectRoleService projectRoleService;
	private UserService userService;
	private TraineeSkillService traineeSkillService;
	private NotificationService notificationService;

	public SearchController(SkillService skillService, ProjectService projectService,
			ProjectRoleService projectRoleService, UserService userService, TraineeSkillService traineeSkillService,
			NotificationService notificationService) {
		super();
		this.skillService = skillService;
		this.projectService = projectService;
		this.projectRoleService = projectRoleService;
		this.userService = userService;
		this.traineeSkillService = traineeSkillService;
		this.notificationService = notificationService;
	}

	public boolean renderUser(HttpSession session, Model model) {

		if (session.getAttribute("user") == null) {
			return false;
		}

		User user = (User) session.getAttribute("user");
		model.addAttribute("user", user);
		model.addAttribute("hasNotif", notificationService.countNotifactionsByUser(user));

		return true;
	}

	@GetMapping("searchTrainee/{skillName}")
	public String getSearchTraineePageSkill(HttpSession session, Model model, @PathVariable String skillName) {
		
		if (!renderUser(session, model)) {
			
			return "redirect:/login";
		}
		
		if (skillService.getSkillByTitle(skillName) == null) {


			skillService.getSkillByTitle(skillName);

			List<User> trainees = userService.findByLastName(skillName);
			String skillBlank = "No Skills Yet";
			List<String> skills1 = new ArrayList<String>();

			for (User trainee : trainees) {

				if (!traineeSkillService.getAllTraineeSkillsWithTrainee(trainee).isEmpty()) {
					String skill1 = traineeSkillService.getAllTraineeSkillsWithTrainee(trainee).get(0).getSkill()
							.getTitle();
					skills1.add(skill1);
				}

				if (traineeSkillService.getAllTraineeSkillsWithTrainee(trainee).size() < 2) {
					skills1.add(skillBlank);
				}
			}

			model.addAttribute("listOf1stSkills", skills1);

			model.addAttribute("trainees", trainees);

		} else {

			

			Skill skill = skillService.getSkillByTitle(skillName);

			List<User> trainees = traineeSkillService.getAllTraineesWithSkill(skill);

			String skillBlank = "No Skills Yet";

			List<String> skills1 = new ArrayList<String>();

			for (User trainee : trainees) {
				String skill1 = traineeSkillService.getAllTraineeSkillsWithTrainee(trainee).get(0).getSkill()
						.getTitle();
				skills1.add(skill1);

				if (traineeSkillService.getAllTraineeSkillsWithTrainee(trainee).size() < 2) {
					skills1.add(skillBlank);
				}

			}

			model.addAttribute("listOf1stSkills", skills1);

			model.addAttribute("trainees", trainees);

		}
		model.addAttribute("hasQuery", true);
		model.addAttribute("trainee", true);

		return "Search/search";

	}

	@GetMapping("searchProject/{skillName}")
	public String getSearchProjectPage(HttpSession session, Model model, @PathVariable String skillName) {

		if (!renderUser(session, model)) {
			return "redirect:/login";
		}

		if (skillService.getSkillByTitle(skillName) == null) {

			Project project = projectService.findByTitle(skillName);
			ArrayList<Project> projects = new ArrayList<>();

			if (project != null) {
				projects.add(project);
			}

			model.addAttribute("projects", projects);

		} else {

			Skill skill = skillService.getSkillByTitle(skillName);

			List<Long> projectIds = projectRoleService.getAllProjectIdsBySkill(skill);

			Set<Project> projectsSet = new HashSet<Project>();

			for (Long projectId : projectIds) {

				projectsSet.add(projectService.findProjectById(projectId));
			}

			List<Project> projects = new ArrayList<Project>(projectsSet);

			model.addAttribute("projects", projects);

		}

		model.addAttribute("hasQuery", true);
		model.addAttribute("trainee", false);

		return "Search/search";

	}

}
