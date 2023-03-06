package com.fdmgroup.projectmanagment.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fdmgroup.projectmanagment.Model.Project;
import com.fdmgroup.projectmanagment.Model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
	
	Task findTaskById(Long id);

	List<Task> findByProject(Project project);

	@Query("SELECT t FROM Task t WHERE t.id = :id")
	List<Task> findTasksByProjectId(Long id);

}
