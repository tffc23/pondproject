package com.fdmgroup.projectmanagment.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fdmgroup.projectmanagment.Model.Project;
import com.fdmgroup.projectmanagment.Model.ProjectType;
import com.fdmgroup.projectmanagment.Model.User;
import com.fdmgroup.projectmanagment.Repository.ProjectRepository;

@Service
public class ProjectService {

	private final ProjectRepository projectRepo ;
	
	public ProjectService(ProjectRepository projectRepo) {
		super() ;
		this.projectRepo = projectRepo ;
	}
		
	public List<Project> getAllProjects() {
		return this.projectRepo.findAll() ;
	}
	
	public Project findProjectById(Long id) {
        return this.projectRepo.findByProjectId(id);
	}
	
	public Project findByTitle(String title) {
        return this.projectRepo.findByTitle(title) ;
	}
	
	public List<Project> findByProjectType(ProjectType type) {
		return this.projectRepo.findByType(type) ;
	}

	public void deleteByProjectId(Long id) {
		this.projectRepo.deleteById(id);
	}
	
	public void saveProject(Project project) {
		this.projectRepo.save(project) ;
	}
	
	public List<Project> findByCreator(User creator)
	{
		return this.projectRepo.findByCreator(creator);
	}
	public Integer countProjects() {
		return this.projectRepo.countProjects();
	}
}
