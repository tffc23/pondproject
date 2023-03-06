package com.fdmgroup.projectmanagment.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.projectmanagment.Model.ProjectFile;

@Repository
public interface ProjectFileRepository extends JpaRepository<ProjectFile, Integer> {

	List<ProjectFile> findByProjectProjectId(Long id) ;
	
	ProjectFile findById(Long id) ;
}
