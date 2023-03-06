package com.fdmgroup.projectmanagment.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import com.fdmgroup.projectmanagment.Model.Project;
import com.fdmgroup.projectmanagment.Model.ProjectFile;

import com.fdmgroup.projectmanagment.Repository.ProjectFileRepository;
import com.fdmgroup.projectmanagment.Repository.ProjectRepository;

@ExtendWith(MockitoExtension.class)
class ProjectFileServiceTest {

	@Mock
    private ProjectRepository pRepository;

    @Mock
    private ProjectFileRepository pfRepository;

    @InjectMocks
    private ProjectFileService pfService;
    
    
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
    public void testSaveProjectFile() throws IOException {
        
        Project project = new Project();
        project.setProjectId(1L);
        when(pRepository.findByProjectId(1L)).thenReturn(project);

        
        byte[] fileContent = "file content".getBytes();
        String fileName = "fileName.txt";
        String fileType = "text/plain";
        MockMultipartFile mockMultipartFile = new MockMultipartFile(fileName, fileName, fileType, fileContent);
        
        
        ProjectFile projectFile = new ProjectFile(fileName , fileType , fileContent) ;
        projectFile.setProject(project);

        when(pfRepository.save(any(ProjectFile.class))).thenReturn(projectFile) ;
        
        ProjectFile result = pfService.saveProjectFile(mockMultipartFile, 1L);

       
        assertEquals(fileName, result.getName());
        assertEquals(fileType, result.getFileType());
        assertEquals(fileContent, result.getData());
    }
	
	
	
	@Test
	public void testDeleteByProjectFileId() {
		 
		Long idToDelete = 1L;
		
		pfService.deleteByProjectFileId(idToDelete);
		verify(pfRepository, times(1)).deleteById(Math.toIntExact(idToDelete));
	}
	
	
	@Test
	public void testGetAllFilesPerProjectId() {
	    
	    Long projectId = 1L;
	    List<ProjectFile> expectedFiles = Arrays.asList(
	            new ProjectFile("file1.txt", "text/plain", new byte[0]),
	            new ProjectFile("file2.txt", "text/plain", new byte[0]),
	            new ProjectFile("file3.txt", "text/plain", new byte[0])
	    );
	    
	    when(pfRepository.findByProjectProjectId(projectId)).thenReturn(expectedFiles);
	    
	    List<ProjectFile> result = pfService.getAllFilesPerProjectId(projectId);
	    
	    verify(pfRepository, times(1)).findByProjectProjectId(projectId);
	    
	    assertEquals(expectedFiles, result);
	}
	
	@Test
	public void testFindFileById() {
	   
	    Long idToFind = 1L;
	    
	    ProjectFile expectedFile = new ProjectFile("file1.txt", "text/plain", new byte[0]);
	    
	    when(pfRepository.findById(idToFind)).thenReturn(expectedFile);
	    
	    ProjectFile result = pfService.findFileById(idToFind);
	   
	    verify(pfRepository, times(1)).findById(idToFind);
	    
	    assertEquals(expectedFile, result);
	}
}
