package com.fdmgroup.projectmanagment.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fdmgroup.projectmanagment.Model.Project;
import com.fdmgroup.projectmanagment.Model.ProjectRole;
import com.fdmgroup.projectmanagment.Model.Skill;
import com.fdmgroup.projectmanagment.Model.User;
import com.fdmgroup.projectmanagment.Repository.ProjectRoleRepository;

@Service
public class ProjectRoleService {
	
	private final ProjectRoleRepository projectRoleRepository;

	public ProjectRoleService(ProjectRoleRepository projectRoleRepository) {
		super();
		this.projectRoleRepository = projectRoleRepository;
	}
	
	public void save(ProjectRole projectRole)
	{
		this.projectRoleRepository.save(projectRole);
	}
	
	public void delete(ProjectRole projectRole)
	{
		this.projectRoleRepository.delete(projectRole);
	}
	
	public ProjectRole findProjectRoleById(Long id)
	{
		return projectRoleRepository.findById(id).orElse(null);
	}
	
	public void deleteProjectRoleById(Long id) {
		this.projectRoleRepository.deleteById(id);
	}
	
	/**
	 * Find the list of project roles that require the given skill
	 * @param skill
	 * @return the list of project roles
	 */
	public List<ProjectRole> findProjectRoleIdBySkill(Skill skill)
	{
		List<Long> projectRoleIds = projectRoleRepository.findByMyColumn(skill.getId().toString());
		
		List<ProjectRole> projectRoles = new ArrayList<ProjectRole>();
		
		for (Long projectRoleId: projectRoleIds)
		{
			Optional<ProjectRole> newProjectRole = projectRoleRepository.findById(projectRoleId);
			newProjectRole.ifPresent(projectRoles::add);
		}
		
		return projectRoles;
	}
	
	/**
	 * Find all the projects that have roles which require the given skill
	 * @param skill
	 * @return the list of projects
	 */
	public List<Long> getAllProjectIdsBySkill(Skill skill)
	{
		List<Long> projectRoleIds = projectRoleRepository.findByMyColumn(skill.getId().toString());
		List<Long> projectIds = new ArrayList<Long>();
		
		for (Long projectRoleId: projectRoleIds)
		{
			projectIds.add(projectRoleRepository.findProjectById(projectRoleId.toString()));
		}
		return projectIds;
	}
	
	/**
	 * Find the participant id by the given project role
	 * @param role
	 * @return the participant id or null if the project role has not been assigned to anyone
	 */
	public Long getParticipantId(ProjectRole role)
	{
		return projectRoleRepository.findParticipantById(role.getId().toString()).orElse(null);
	}
	
	/**
	 * Find All the project roles the user has participated in
	 * @param participant
	 * @return the list of project roles with the same participant
	 */
	public List<ProjectRole> findProjectRolesByParticipant(User participant)
	{
		return projectRoleRepository.findByParticipant(participant);
	}
	
	public List<ProjectRole> findProjectRolesByProject(Project project) {
		return projectRoleRepository.findByProject(project);
	}

	public List<User> findParticipantsByProject(Project project) {
		return projectRoleRepository.findParticipantsByProject(project);
	}
	
}
