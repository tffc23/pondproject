package com.fdmgroup.projectmanagment.Controller;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import com.fdmgroup.projectmanagment.Model.Project;
import com.fdmgroup.projectmanagment.Model.ProjectFile;
import com.fdmgroup.projectmanagment.Model.Role;
import com.fdmgroup.projectmanagment.Model.User;
import com.fdmgroup.projectmanagment.Service.NotificationService;
import com.fdmgroup.projectmanagment.Service.ProjectFileService;
import com.fdmgroup.projectmanagment.Service.ProjectRoleService;
import com.fdmgroup.projectmanagment.Service.ProjectService;
import com.fdmgroup.projectmanagment.Service.SkillService;
import com.fdmgroup.projectmanagment.Service.TraineeSkillService;
import com.fdmgroup.projectmanagment.Service.UserService;

import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class ProjectFileControllerTest {

	
	@Mock
	ProjectService pService ;
	@Mock
	ProjectRoleService prService ;
	@Mock
	SkillService sService ;
	@Mock
	TraineeSkillService tsService ;
	@Mock
	UserService uService ;
	@Mock
	NotificationService nService;
	@Mock
	ProjectFileService pfService ;
	@Mock
	Project project;
	
	@Mock
	Model model;
	
	@Mock
	HttpSession session;
	
	@InjectMocks
	ProjectFileController pfController ;
	
	
	
	@BeforeEach
	void setUp() {
	        
       MockitoAnnotations.openMocks(this);
	   
	}
	
	
	 @Test
	  public void RedirectToLogin() throws Exception {
	    when(session.getAttribute("user")).thenReturn(null);

	    String result = pfController.getUploadFilePage(1L, model, session);
	        
	    assertEquals("redirect:/login", result);
	  }
	
	@Test
    public void testGetFile() {
		
		
		Long FILE_ID = 1L;
	    String FILE_NAME = "test.txt";
	    String FILE_TYPE = "text/plain";
	    byte[] FILE_DATA = "Test Data".getBytes();
	    
		ProjectFile projectFile = new ProjectFile();
	    projectFile.setId(FILE_ID);
	    projectFile.setName(FILE_NAME);
	    projectFile.setFileType(FILE_TYPE);
	    projectFile.setData(FILE_DATA);
		
		when(pfService.findFileById(FILE_ID)).thenReturn(projectFile);

        ResponseEntity<Resource> response = pfController.getFile(FILE_ID);

        assertEquals(MediaType.parseMediaType(FILE_TYPE), response.getHeaders().getContentType());
        
        assertEquals("attachment; filename=\"" + FILE_NAME + "\"",
            response.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION).get(0));
        
        assertArrayEquals(FILE_DATA, ((ByteArrayResource) response.getBody()).getByteArray());
    }

	
	@Test
    public void returnAdminUploadFileView() throws Exception {
        User user = new User();
        user.setRole(Role.Admin);
        when(session.getAttribute("user")).thenReturn(user);

        String result = pfController.getUploadFilePage(1L, model, session);
        assertEquals("Dashboard/Project/uploadFileAdmin", result);
    }
	
	@Test
    public void returnUploadFileView() throws Exception {
        User user = new User();
        user.setRole(Role.Trainee);
        when(session.getAttribute("user")).thenReturn(user);

        String result = pfController.getUploadFilePage(1L, model, session);
        assertEquals("Project/uploadFile", result);
    }
	

	
	@Test
	public void deleteReturnCorrectRedirect() throws Exception {
		
		Long fileId = 1L;
		Long projectId = 1L;
		
		when(project.getProjectId()).thenReturn(projectId);
		ProjectFile projectFile = new ProjectFile();
		projectFile.setId(fileId);
		projectFile.setProject(project);
		when(pfService.findFileById(fileId)).thenReturn(projectFile);
		
		String result = pfController.deleteFileFromProjectPage(fileId);
		
		assertEquals("redirect:/project/" + projectId, result);
		verify(pfService).deleteByProjectFileId(fileId);
	}

}
