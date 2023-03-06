package com.fdmgroup.projectmanagment.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fdmgroup.projectmanagment.Model.Project;
import com.fdmgroup.projectmanagment.Model.ProjectFile;
import com.fdmgroup.projectmanagment.Repository.ProjectFileRepository;
import com.fdmgroup.projectmanagment.Repository.ProjectRepository;

@Service
public class ProjectFileService {
	
	private ProjectFileRepository projectFileRepository ;
	private ProjectRepository projectRepository ;

	public ProjectFileService(ProjectFileRepository projectFileRepository, ProjectRepository projectRepository ) {
		super();
		this.projectFileRepository = projectFileRepository;
		this.projectRepository = projectRepository ;
	}

	
	
	public ProjectFile saveProjectFile(MultipartFile file ,  Long projectId) throws IOException {
		
		 String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		 
		  try {
				 ProjectFile projectFile = new ProjectFile(fileName , file.getContentType() , file.getBytes()) ;
				 
				 Project project = projectRepository.findByProjectId(projectId);
				 projectFile.setProject(project);
					
				 return this.projectFileRepository.save(projectFile) ;
				 
		  } catch (IOException ex) {
			  System.out.println("--proj file service failure ");
		  }
		  
		
		return new ProjectFile() ;
	}
	
	
	public void deleteByProjectFileId(Long id) {
		this.projectFileRepository.deleteById(Math.toIntExact(id)); ;
	}
	
	public List<ProjectFile> getAllFilesPerProjectId(Long id) {
		return this.projectFileRepository.findByProjectProjectId(id) ;
	}
	
	public ProjectFile findFileById(Long id) {
		return this.projectFileRepository.findById(id) ;
	}
	

	

}
