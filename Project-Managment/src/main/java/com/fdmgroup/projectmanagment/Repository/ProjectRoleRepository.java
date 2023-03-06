package com.fdmgroup.projectmanagment.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fdmgroup.projectmanagment.Model.Project;
import com.fdmgroup.projectmanagment.Model.ProjectRole;
import com.fdmgroup.projectmanagment.Model.User;

@Repository
public interface ProjectRoleRepository extends  JpaRepository<ProjectRole, Long>
{
	@Query(value="SELECT project_roles_id FROM skill_project_roles WHERE skills_id = :value", nativeQuery = true )
	List<Long> findByMyColumn(@Param("value") String value) ;
	
	Optional<ProjectRole> findById(Long id);
	
	@Query(value="SELECT fk_project_id FROM project_role WHERE id = :projectRoleId", nativeQuery = true )
	Long findProjectById(@Param("projectRoleId") String projectRoleId);

	
	// Participant ID can be null since there can a role in a project that is not assigned to anyone yet
	@Query(value="SELECT fk_user_id FROM project_role WHERE id = :projectRoleId", nativeQuery = true )
	Optional<Long> findParticipantById(@Param("projectRoleId") String projectRoleId);
	
	Long countByProject(Project project);
	
	List<ProjectRole> findByParticipant(User participant);

	List<ProjectRole> findByProject(Project project);
	
	List<User> findParticipantsByProject(Project project);
	
}
