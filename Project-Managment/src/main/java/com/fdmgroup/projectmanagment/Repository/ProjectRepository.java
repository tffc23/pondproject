package com.fdmgroup.projectmanagment.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fdmgroup.projectmanagment.Model.Project;
import com.fdmgroup.projectmanagment.Model.ProjectType;
import com.fdmgroup.projectmanagment.Model.User;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {
	
	Project findByProjectId(Long id) ;
	
	Project findByTitle(String title) ;
	
	List<Project> findByType(ProjectType type) ;
	
	List<Project> findByCreator(User creator);

	@Query("SELECT COUNT(*) FROM Project")
	Integer countProjects();
	
}
