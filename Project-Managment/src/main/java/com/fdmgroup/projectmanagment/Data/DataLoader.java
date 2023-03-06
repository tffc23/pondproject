package com.fdmgroup.projectmanagment.Data;

import com.fdmgroup.projectmanagment.Model.Notification;
import com.fdmgroup.projectmanagment.Model.Portfolio;
import com.fdmgroup.projectmanagment.Model.Project;
import com.fdmgroup.projectmanagment.Model.ProjectRole;
import com.fdmgroup.projectmanagment.Model.ProjectType;
import com.fdmgroup.projectmanagment.Model.Region;
import com.fdmgroup.projectmanagment.Model.Role;
import com.fdmgroup.projectmanagment.Model.Skill;
import com.fdmgroup.projectmanagment.Model.SkillLevel;
import com.fdmgroup.projectmanagment.Model.Stream;
import com.fdmgroup.projectmanagment.Model.Task;
import com.fdmgroup.projectmanagment.Model.TraineeSkill;
import com.fdmgroup.projectmanagment.Model.User;
import com.fdmgroup.projectmanagment.Repository.NotificationRepository;
import com.fdmgroup.projectmanagment.Repository.PortfolioRepository;
import com.fdmgroup.projectmanagment.Repository.ProjectRepository;
import com.fdmgroup.projectmanagment.Repository.ProjectRoleRepository;
import com.fdmgroup.projectmanagment.Repository.SkillRepository;
import com.fdmgroup.projectmanagment.Repository.TaskRepository;
import com.fdmgroup.projectmanagment.Repository.TraineeSkillRepository;
import com.fdmgroup.projectmanagment.Repository.UserRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

	private final UserRepository userRepository;
	private final NotificationRepository notificationRepository;
	private final ProjectRepository projectRepository;
	private final ProjectRoleRepository projectRoleRepository;
	private final SkillRepository skillRepository;
	private final TraineeSkillRepository traineeSkillRepository;
	private final TaskRepository taskRepository;
	private final PortfolioRepository portfolioRepository;

	@Autowired
	public DataLoader(UserRepository userRepository, NotificationRepository notificationRepository,
			ProjectRepository projectRepository, ProjectRoleRepository projectRoleRepository,
			SkillRepository skillRepository, TraineeSkillRepository traineeSkillRepository,
			TaskRepository taskRepository, PortfolioRepository portfolioRepository) {
		super();
		this.userRepository = userRepository;
		this.notificationRepository = notificationRepository;
		this.projectRepository = projectRepository;
		this.projectRoleRepository = projectRoleRepository;
		this.skillRepository = skillRepository;
		this.traineeSkillRepository = traineeSkillRepository;
		this.taskRepository = taskRepository;
		this.portfolioRepository = portfolioRepository;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

		/**
		 * Users
		 */
		// Admin
		User user1 = new User("Sarabjeet", "Singh", "f", "sarab@email.com", "f", Region.AU, Role.Admin, true);

		// Sales
		User sales1 = new User("Peter", "Parker", "spiderman", "spiderman@mail.com", "webshooting", Region.US,
				Role.Sales, true);

		// Trainer
		User trainer1 = new User("Bruce", "Wayne", "batman", "iambatman@arkham.com", "mary", Region.US, Role.Trainer,
				true);

		// Trainees
		User tiffany = new User("Tiffany", "C", "tiff", "tiff@email.com", "password", Region.AU, Role.Trainee, true);
		User lisa = new User("Lisa", "S", "lisa", "lisa@gmail.com", "ilovedogs", Region.AU, Role.Trainee, true);
		User trainee1 = new User("John", "Jones", "john", "john@email.com", "password", Region.UK, Role.Trainee, true);
		User trainee2 = new User("Richard", "Grayson", "nightwing", "rg@email.com", "password", Region.AU, Role.Trainee,
				true);
		User trainee3 = new User("Jason", "Todd", "redhood", "jt@email.com", "password", Region.AU, Role.Trainee, true);
		User trainee4 = new User("Tim", "Drake", "redrobin", "td@email.com", "password", Region.AU, Role.Trainee, true);
		User trainee5 = new User("Damian", "Wayne", "robin", "dw@email.com", "password", Region.UK, Role.Trainee, true);
		User trainee6 = new User("Stephanie", "Brown", "spoiler", "bg@email.com", "password", Region.UK, Role.Trainee,
				true);
		User eric = new User("Eric", "Z", "eric", "eric@email.com", "password", Region.AU, Role.Trainee, true);
		User yun = new User("Yunzhang", "Y", "yang", "yy@email.com", "password", Region.AU, Role.Trainee, true);
		User sar = new User("Sarabjeet", "Singh", "sarab", "sarabAnother@email.com", "password", Region.AU,
				Role.Trainee, true);

		/**
		 * Stream
		 */
		tiffany.setStream(Stream.DEV);
		lisa.setStream(Stream.DEV);
		trainee1.setStream(Stream.BA);
		trainee2.setStream(Stream.TESTING);
		trainee3.setStream(Stream.BA);
		trainee4.setStream(Stream.CLOUD);
		trainee5.setStream(Stream.DEV);
		trainee6.setStream(Stream.BI);
		eric.setStream(Stream.DEV);
		yun.setStream(Stream.DEV);
		sar.setStream(Stream.DEV);

		/**
		 * Projects
		 */
		Project elevator = new Project(tiffany, "Elevator Project", ProjectType.Mock,
				"Multi-threaded Elevator Project");
		Project springBoot = new Project(trainee1, "Spring Boot Project", ProjectType.Mock, "Web development project");
		Project projectManagement = new Project(trainee2, "Project Management Project", ProjectType.Mock,
				"Scrum Project Management Project");
		Project client1 = new Project(sales1, "Wayne Enterprises, Inc. Project", ProjectType.Client,
				"Web Development Project");
		Project pond = new Project(sar, "The Pond", ProjectType.Mock, "Pond Project System Project");

		/**
		 * Project role
		 */
		ProjectRole developer1 = new ProjectRole(elevator, "Developer", "Coding");
		ProjectRole tester1 = new ProjectRole(elevator, "Tester", "Testing");
		developer1.setParticipant(tiffany);
		tester1.setParticipant(lisa);

		ProjectRole dev2 = new ProjectRole(springBoot, "Developer", "Java");
		dev2.setParticipant(trainee1);

		ProjectRole po1 = new ProjectRole(projectManagement, "Product Owner", "Project Management");
		ProjectRole sm1 = new ProjectRole(projectManagement, "Scrum Master", "the master of scrum");
		po1.setParticipant(trainee2);
		sm1.setParticipant(trainee3);

		ProjectRole po2 = new ProjectRole(client1, "Project Owner", "Project Owner");
		ProjectRole dev3 = new ProjectRole(client1, "Developer", "Web development");
		po2.setParticipant(trainee4);
		dev3.setParticipant(trainee5);

		ProjectRole fdev1 = new ProjectRole(pond, "Front-end developer", "Front-end coding");
		ProjectRole fdev2 = new ProjectRole(pond, "Front-end developer", "Front-end coding");
		ProjectRole bdev1 = new ProjectRole(pond, "Back-end developer", "Back-end coding");
		ProjectRole bdev2 = new ProjectRole(pond, "Product Owner, Back-end developer", "Back-end coding");
		ProjectRole bdev3 = new ProjectRole(pond, "Scrum Master, Back-end developer", "Back-end coding");

		/**
		 * Notifications
		 */
		Notification notif1 = new Notification("Invite", "Do you want to join our team");
		Notification notif2 = new Notification("Hello", "How are you?");

		/**
		 * Skills
		 */
		// Approved
		Skill python = new Skill("Python", "Programing Language");
		Skill java = new Skill("Java", "Programming Language");
		Skill italian = new Skill("Italian", "Language");
		Skill pm = new Skill("Leadership",
				"Leading the work of a team to achieve all project goals within the given constraints");
		Skill javaScript = new Skill("Java Script", "Scripting language");

		python.setStatus(true);
		java.setStatus(true);
		italian.setStatus(true);
		pm.setStatus(true);
		javaScript.setStatus(true);

		// Unapproved
		Skill fighting = new Skill("Fighting", "Ninjitsu");
		Skill c = new Skill("C", "Programing Language");
		Skill unix = new Skill("Unix", "Operating system");

		fighting.setStatus(false);
		c.setStatus(false);
		unix.setStatus(false);

		/**
		 * Trainee Skills
		 */
		TraineeSkill traineeSkill1 = new TraineeSkill(tiffany, java, SkillLevel.Experienced);
		TraineeSkill traineeSkill2 = new TraineeSkill(lisa, java, SkillLevel.Advanced);

		TraineeSkill traineeSkill3 = new TraineeSkill(tiffany, italian, SkillLevel.Basic);
		TraineeSkill traineeSkill4 = new TraineeSkill(lisa, italian, SkillLevel.Advanced);

		TraineeSkill ts5 = new TraineeSkill(trainee1, java, SkillLevel.Experienced);

		TraineeSkill ts6 = new TraineeSkill(trainee2, pm, SkillLevel.Advanced);
		TraineeSkill ts7 = new TraineeSkill(trainee2, java, SkillLevel.Advanced);

		TraineeSkill ts8 = new TraineeSkill(trainee3, pm, SkillLevel.Advanced);
		TraineeSkill ts9 = new TraineeSkill(trainee3, italian, SkillLevel.Basic);

		TraineeSkill ts10 = new TraineeSkill(trainee4, pm, SkillLevel.Experienced);
		TraineeSkill ts11 = new TraineeSkill(trainee4, python, SkillLevel.Advanced);

		TraineeSkill ts12 = new TraineeSkill(trainee5, java, SkillLevel.Advanced);

		TraineeSkill ts13 = new TraineeSkill(sar, java, SkillLevel.Advanced);
		TraineeSkill ts14 = new TraineeSkill(sar, python, SkillLevel.Advanced);

		TraineeSkill ts17 = new TraineeSkill(yun, java, SkillLevel.Advanced);
		TraineeSkill ts18 = new TraineeSkill(yun, python, SkillLevel.Advanced);

		TraineeSkill ts19 = new TraineeSkill(eric, java, SkillLevel.Advanced);
		TraineeSkill ts21 = new TraineeSkill(eric, pm, SkillLevel.Advanced);

		TraineeSkill ts22 = new TraineeSkill(eric, javaScript, SkillLevel.Experienced);
		TraineeSkill ts23 = new TraineeSkill(yun, javaScript, SkillLevel.Experienced);

		/**
		 * Project tasks
		 */
		@SuppressWarnings("deprecation")
		Task t1 = new Task("Sprint 3 Review", "Review sprint 3.", new Date(123, 0, 12));
		@SuppressWarnings("deprecation")
		Task t2 = new Task("Presentation", "Prepare presentation slides.", new Date(123, 0, 13));

		/**
		 * Portfolios
		 */
		Portfolio p1 = new Portfolio(tiffany, "Back-end programming projects", "Projects require programming skills");
		p1.setCreatedDate(LocalDate.of(2023, 1, 5));
		p1.setSkills(Arrays.asList(python, java));
		p1.setProjects(Arrays.asList(elevator, pond));

		/**
		 * Set trainee skills
		 */
		// Skills.setTraineeSkills
		java.setTraineeSkills(Arrays.asList(traineeSkill1, traineeSkill2, ts5, ts7, ts12, ts13, ts17, ts19));
		python.setTraineeSkills(List.of(ts11, ts14, ts18));
		italian.setTraineeSkills(Arrays.asList(traineeSkill3, traineeSkill4, ts9));
		pm.setTraineeSkills(Arrays.asList(ts6, ts8, ts10, ts21));
		javaScript.setTraineeSkills(List.of(ts22, ts23));

		// Trainee.setTraineeSkills
		tiffany.setTraineeSkills(Arrays.asList(traineeSkill1, traineeSkill3));
		lisa.setTraineeSkills(Arrays.asList(traineeSkill2, traineeSkill4));
		trainee1.setTraineeSkills(List.of(ts5));
		trainee2.setTraineeSkills(Arrays.asList(ts6, ts7));
		trainee3.setTraineeSkills(Arrays.asList(ts8, ts9));
		trainee4.setTraineeSkills(Arrays.asList(ts10, ts11));
		trainee5.setTraineeSkills(List.of(ts12));
		sar.setTraineeSkills(Arrays.asList(ts13, ts14));
		yun.setTraineeSkills(Arrays.asList(ts17, ts18, ts23));
		eric.setTraineeSkills(Arrays.asList(ts19, ts21, ts22));

		/**
		 * Set role skills
		 */
		// Role.setSkills
		developer1.setSkills(Arrays.asList(java, italian));
		tester1.setSkills(Arrays.asList(java, italian));
		dev2.setSkills(List.of(java));
		po1.setSkills(Arrays.asList(pm, java));
		sm1.setSkills(List.of(pm));
		po2.setSkills(Arrays.asList(pm, java));
		dev3.setSkills(List.of(java));

		fdev1.setSkills(List.of(java, javaScript));
		fdev2.setSkills(List.of(java, javaScript));
		bdev1.setSkills(List.of(java));
		bdev2.setSkills(List.of(java, pm));
		bdev3.setSkills(List.of(java, pm));

		// Role.setParticipant
		fdev1.setParticipant(yun);
		fdev2.setParticipant(eric);
		bdev1.setParticipant(sar);
		bdev2.setParticipant(tiffany);
		bdev3.setParticipant(lisa);

		// Skill.setProjectRoles
		java.setProjectRoles(Arrays.asList(developer1, tester1, dev2, po1, po2, fdev1, fdev2, bdev1, bdev2, bdev3));
		italian.setProjectRoles(Arrays.asList(developer1, tester1));
		pm.setProjectRoles((Arrays.asList(po1, sm1, po2, bdev2, bdev3)));
		javaScript.setProjectRoles(List.of(fdev1, fdev2));

		/**
		 * Set notifications
		 */
		notif1.setSender(lisa);
		notif2.setSender(yun);
		notif1.setOwner(tiffany);
		notif2.setOwner(tiffany);
		tiffany.setNotifications(Arrays.asList(notif1, notif2));

		/**
		 * Set tasks
		 */
		t1.setProject(elevator);
		t2.setProject(elevator);

		/**
		 * ProfileFile.setProject
		 */
		// pf1.setProject(pond);

		/**
		 * Project.setProfileFiles
		 */
		// pond.setProjectFiles(List.of(pf1));

		/**
		 * Repositories
		 */
		// User Repo
		userRepository.save(user1);
		userRepository.save(trainer1);
		userRepository.save(sales1);

		userRepository.save(tiffany);
		userRepository.save(lisa);
		userRepository.save(trainee1);
		userRepository.save(trainee2);
		userRepository.save(trainee3);
		userRepository.save(trainee4);
		userRepository.save(trainee5);
		userRepository.save(trainee6);

		userRepository.save(sar);
		userRepository.save(eric);
		userRepository.save(yun);

		// Project Repo
		projectRepository.save(elevator);
		projectRepository.save(springBoot);
		projectRepository.save(projectManagement);
		projectRepository.save(client1);
		projectRepository.save(pond);

		// Project Role Repo
		projectRoleRepository.save(developer1);
		projectRoleRepository.save(tester1);
		projectRoleRepository.save(dev2);
		projectRoleRepository.save(po1);
		projectRoleRepository.save(sm1);
		projectRoleRepository.save(po2);
		projectRoleRepository.save(dev3);

		projectRoleRepository.save(fdev1);
		projectRoleRepository.save(fdev2);
		projectRoleRepository.save(bdev1);
		projectRoleRepository.save(bdev2);
		projectRoleRepository.save(bdev3);

		// Notifications Repo
		notificationRepository.save(notif1);
		notificationRepository.save(notif2);

		// Skills Repo
		skillRepository.save(python);
		skillRepository.save(java);
		skillRepository.save(italian);
		skillRepository.save(fighting);
		skillRepository.save(pm);
		skillRepository.save(c);
		skillRepository.save(unix);
		skillRepository.save(javaScript);

		// Trainee Skill Repo
		traineeSkillRepository.save(traineeSkill1);
		traineeSkillRepository.save(traineeSkill2);
		traineeSkillRepository.save(traineeSkill3);
		traineeSkillRepository.save(traineeSkill4);
		traineeSkillRepository.save(ts5);
		traineeSkillRepository.save(ts6);
		traineeSkillRepository.save(ts7);
		traineeSkillRepository.save(ts8);
		traineeSkillRepository.save(ts9);
		traineeSkillRepository.save(ts10);
		traineeSkillRepository.save(ts11);
		traineeSkillRepository.save(ts12);
		traineeSkillRepository.saveAll(List.of(ts13, ts14, ts17, ts18, ts19, ts21, ts22, ts23));

		// Task Repo
		taskRepository.save(t1);
		taskRepository.save(t2);

		// Portfolio Repo
		portfolioRepository.save(p1);

	}

}