package com.fdmgroup.projectmanagment.Controller;

import com.fdmgroup.projectmanagment.Model.*;
import com.fdmgroup.projectmanagment.Service.NotificationService;
import com.fdmgroup.projectmanagment.Service.PortfolioService;
import com.fdmgroup.projectmanagment.Service.ProjectRoleService;
import com.fdmgroup.projectmanagment.Service.ProjectService;
import com.fdmgroup.projectmanagment.Service.SkillService;
import com.fdmgroup.projectmanagment.Service.TraineeSkillService;
import com.fdmgroup.projectmanagment.Service.UserService;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Controller for display the profile for trainee, trainer, sales
 *
 * @author Tiffany
 */
@Controller
public class ProfileController {

	private UserService userService;
	private TraineeSkillService traineeSkillService;
	private ProjectService projectService;
	private ProjectRoleService projectRoleService;
	private SkillService skillService;
	private NotificationService notificationService;
	private PortfolioService portfolioService;

	public ProfileController(UserService userService, TraineeSkillService traineeSkillService,
			ProjectService projectService, ProjectRoleService projectRoleService, SkillService skillService,
			NotificationService notificationService, PortfolioService portfolioService) {
		super();
		this.userService = userService;
		this.traineeSkillService = traineeSkillService;
		this.projectService = projectService;
		this.projectRoleService = projectRoleService;
		this.skillService = skillService;
		this.notificationService = notificationService;
		this.portfolioService = portfolioService;
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

	public void renderUserProfile(User user, Model model) {

		List<Project> projects = projectService.findByCreator(user);
		model.addAttribute("listOfProjects", projects);
		model.addAttribute("listOfRoles", projectRoleService.findProjectRolesByParticipant(user));
	}

	// Portfolios
	@GetMapping("/portfolio/{id}")
	public String getPortfolioPage(@PathVariable Long id, HttpSession session, Model model) {

		if (!renderUser(session, model)) {
			return "redirect:/login";
		}

		User user = (User) session.getAttribute("user");
		Portfolio portfolio = portfolioService.findById(id);
		List<Project> projects = portfolio.getProjects();
		List<ProjectRole> roles = projectRoleService.findProjectRolesByParticipant(user);
		List<ProjectRole> portfolioRoles = new ArrayList<ProjectRole>();

		for (Project p : projects) {
			for (ProjectRole r : roles) {
				if (r.getProject().equals(p)) {
					portfolioRoles.add(r);
				}
			}
		}

		model.addAttribute("portfolio", portfolio);
		model.addAttribute("roles", portfolioRoles);

		return "Portfolio/portfolioDetail";
	}

	@GetMapping("/create/portfolio")
	public String getCreatePortfolio(HttpSession session, Model model) {
		if (!renderUser(session, model)) {
			return "redirect:/login";
		}

		User user = (User) session.getAttribute("user");

		Set<Project> projects = new HashSet<Project>(projectService.findByCreator(user));

		for (ProjectRole r : projectRoleService.findProjectRolesByParticipant(user)) {
			projects.add(r.getProject());
		}

		model.addAttribute("portfolio", new Portfolio(user));
		model.addAttribute("approvedSkills", skillService.getAllApprovedSkills());
		model.addAttribute("userProjects", projects);
		model.addAttribute("isEdit", false);

		return "Portfolio/createPortfolio";
	}

	@PostMapping("/savePortfolio")
	public String savePortfolio(HttpSession session, @ModelAttribute Portfolio portfolio,
			@RequestParam boolean isEdit) {
		
		User user = (User) session.getAttribute("user");
		this.portfolioService.save(portfolio);

		if (isEdit) {
			return "redirect:/portfolio/" + portfolio.getId();
		}
		return "redirect:/profile/" + user.getId();
	}

	@GetMapping("/portfolio/{id}/delete")
	public String deletePortfolio(@PathVariable Long id, HttpSession session) {
		Portfolio p = portfolioService.findById(id);
		this.portfolioService.delete(p);
		User u = (User) session.getAttribute("user");

		return "redirect:/profile/" + u.getId();
	}

	@GetMapping("/portfolio/{id}/edit")
	public String editPortfolio(@PathVariable Long id, HttpSession session, Model model) {
		if (!renderUser(session, model)) {
			return "redirect:/login";
		}

		Portfolio p = portfolioService.findById(id);

		User user = (User) session.getAttribute("user");

		Set<Project> projects = new HashSet<Project>(projectService.findByCreator(user));

		for (ProjectRole r : projectRoleService.findProjectRolesByParticipant(user)) {
			projects.add(r.getProject());
		}

		model.addAttribute("portfolio", p);
		model.addAttribute("approvedSkills", skillService.getAllApprovedSkills());
		model.addAttribute("userProjects", projects);
		model.addAttribute("isEdit", true);

		return "Portfolio/createPortfolio";
	}

	@GetMapping("/portfolio/{ptid}/delete/{prid}")
	public String deleteProjectFromPortfolio(@PathVariable Long ptid, @PathVariable Long prid) {
		Portfolio portfolio = portfolioService.findById(ptid);
		Project project = projectService.findProjectById(prid);

		List<Project> projects = portfolio.getProjects();

		for (Project p : projects) {
			if (p.equals(project)) {
				projects.remove(p);
				break;
			}
		}

		portfolio.setProjects(projects);
		portfolioService.save(portfolio);

		return "redirect:/portfolio/" + ptid;
	}

	/**
	 * User Profile
	 */
	@GetMapping("/profile/{id}")
	public String getProfilePage(@PathVariable Long id, HttpSession httpSession, Model model) {

		if (!renderUser(httpSession, model)) {
			return "redirect:/login";
		}

		User user = (User) httpSession.getAttribute("user");

		if (user.getId() != id) {
			model.addAttribute("isUser", false);
			user = userService.findById(id);
			model.addAttribute(user);
		} else {
			model.addAttribute("isUser", true);
		}

		if (user.getRole() == Role.Trainee)
			model.addAttribute("listOfTraineeSkills", traineeSkillService.getAllTraineeSkillsWithTrainee(user));
		model.addAttribute("portfolios", portfolioService.findByCreator(user));
		renderUserProfile(user, model);
		
		System.out.println("profile: "+portfolioService.findByCreator(user).size());

		if (user.getRole() == Role.Admin) {
			return "Dashboard/User/profile";
		}
		return "UserProfile/userProfile";
	}

	/**
	 * Skills
	 */
	@GetMapping("/addSkill")
	public String getAddSkillPage(HttpSession httpSession, Model model) {

		if (httpSession.getAttribute("user") == null) {
			return "redirect:/login";
		}

		User user;
		if (((user = (User) httpSession.getAttribute("user"))).getRole() != Role.Trainee) {
			return "redirect:/";
		}

		List<Skill> skillsTraineeNotHave = skillService.getAllApprovedSkills();
		List<Long> skillsTraineeHave = traineeSkillService.getAllSkillIdsByTrainee(user);

		for (Long skillId : skillsTraineeHave) {
			skillsTraineeNotHave.remove(skillService.getSkillById(skillId));
		}

		model.addAttribute("listOfApprovedSkills", skillsTraineeNotHave);
		model.addAttribute("skillLevels", SkillLevel.values());

		return "Skill/traineeAddSkill";
	}

	@PostMapping("/saveTraineeSkill/")
	public String processNewTraineeSkill(@RequestParam Long id, @RequestParam SkillLevel level,
			HttpSession httpSession) {
		TraineeSkill traineeSkill = new TraineeSkill();
		traineeSkill.setTrainee((User) httpSession.getAttribute("user"));
		traineeSkill.setSkillLevel(level);
		traineeSkill.setSkill(skillService.getSkillById(id));

		traineeSkillService.save(traineeSkill);

		if (((User) httpSession.getAttribute("user")).getRole() == Role.Admin) {
			return "redirect:/users/skills/" + ((User) httpSession.getAttribute("user")).getId();
		}
		return "redirect:/traineeSkill";
	}

	@GetMapping("/traineeSkill/{id}/delete")
	public String deleteSkill(@PathVariable Long id, HttpSession httpSession, Model model) {
		if (httpSession.getAttribute("user") == null) {
			return "redirect:/login";
		}

		if (((User) httpSession.getAttribute("user")).getRole() != Role.Trainee) {
			return "redirect:/";
		}

		TraineeSkill skill = traineeSkillService.getTraineeSkillById(id);
		traineeSkillService.delete(skill);
		return "redirect:/traineeSkill";
	}

	@GetMapping("/suggestSkill")
	public String getSuggestSkillPage(HttpSession httpSession, Model model) {

		if (httpSession.getAttribute("user") == null) {
			return "redirect:/login";
		}

		if (((User) httpSession.getAttribute("user")).getRole() != Role.Trainee) {
			return "redirect:/";
		}

		model.addAttribute("newSkill", new Skill());
		return "Skill/traineeSuggestSkill";
	}

	@PostMapping("/saveSuggestedSkill")
	public String processSuggestedSkill(@ModelAttribute Skill skill) {
		skill.setStatus(false);
		skillService.save(skill);
		return "redirect:/traineeSkill";
	}

	/**
	 * Trainer: approve skills
	 */
	@GetMapping("/skillRequests")
	public String getSkillRequestPage(HttpSession session, Model model) {

		if (session.getAttribute("user") == null) {
			return "redirect:/login";
		}

		List<Skill> unapprovedSkills = skillService.getAllUnapprovedSkills();
		model.addAttribute("skills", unapprovedSkills);

		return "Skill/trainerSkillRequests";
	}

	@GetMapping("/skillsRequests/approve/{id}")
	public String approveSkill(@PathVariable long id, Model model) {

		Skill foundSkill = this.skillService.getSkillById(id);
		foundSkill.setStatus(true);

		this.skillService.save(foundSkill);

		return "redirect:/skillRequests";
	}

	@GetMapping("/skillsRequests/reject/{id}")
	public String rejectSkill(@PathVariable long id, Model model) {

		this.skillService.delete(id);

		return "redirect:/skillRequests";
	}

	@GetMapping("/traineeSkill")
	public String getTraineeSkillPage(HttpSession httpSession, Model model) {
		if (httpSession.getAttribute("user") == null) {
			return "redirect:/login";
		}

		if (((User) httpSession.getAttribute("user")).getRole() != Role.Trainee) {
			return "redirect:/";
		}

		User user = (User) httpSession.getAttribute("user");
		model.addAttribute("listOfSkills", traineeSkillService.getAllTraineeSkillsWithTrainee(user));
		return "Skill/traineeViewSkill";
	}

	@GetMapping("/traineeSkill/{id}/edit")
	public String getChangeSkillLevelPage(@PathVariable Long id, HttpSession httpSession, Model model) {

		if (httpSession.getAttribute("user") == null) {
			return "redirect:/login";
		}

		if (((User) httpSession.getAttribute("user")).getRole() != Role.Trainee) {
			return "redirect:/";
		}

		model.addAttribute("traineeSkill", traineeSkillService.getTraineeSkillById(id));
		model.addAttribute("skillLevels", SkillLevel.values());
		return "Skill/changeSkillLevel";
	}

	@PostMapping("/updateTraineeSkill")
	public String updateSkillLevel(@ModelAttribute TraineeSkill traineeSkill) {
		TraineeSkill currentSkill = traineeSkillService.getTraineeSkillById(traineeSkill.getId());

		traineeSkill.setTrainee(currentSkill.getTrainee());
		traineeSkill.setSkill(currentSkill.getSkill());

		traineeSkillService.save(traineeSkill);
		return "redirect:/traineeSkill";
	}

	@GetMapping("/editSkillLevel")
	public String getChangeSkillLevelPage() {
		return "Skill/changeSkillLevel";
	}

	/**
	 * Edit Profile / Reset Password
	 */
	@GetMapping("/profile/{id}/edit")
	public String getEditProfilePage(@PathVariable long id, HttpSession httpSession, Model model) {

		if (!renderUser(httpSession, model)) {
			return "redirect:/login";
		}

		model.addAttribute("regions", Region.values());
		model.addAttribute("streams", Stream.values());

		return "UserProfile/editProfile";
	}

	@GetMapping("/resetPassword")
	public String getResetPasswordPage(HttpSession httpSession, Model model) {

		if (!renderUser(httpSession, model)) {
			return "redirect:/login";
		}

		return "UserProfile/resetPassword";
	}

	/**
	 * Get Portfolio of all the projects done by creator, ADMIN ONLY
	 * @param id
	 * @param model
	 * @param httpSession
	 * @return
	 */
	@GetMapping("/users/portfolio/{id}")
	public String getSingleUsersPortfolio(@PathVariable Long id, Model model, HttpSession httpSession) {
		User user = userService.findById(id);

		List<Project> projects = projectService.findByCreator(user);
		model.addAttribute("listOfProjects", projects);
		model.addAttribute("listOfRoles", projectRoleService.findProjectRolesByParticipant(user));

		model.addAttribute("portfolios", portfolioService.findByCreator(user));
		httpSession.getAttribute("user");

		return "Dashboard/User/userPortfolio";
	}
}
