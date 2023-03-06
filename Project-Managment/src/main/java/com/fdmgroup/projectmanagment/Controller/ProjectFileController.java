package com.fdmgroup.projectmanagment.Controller;

import com.fdmgroup.projectmanagment.Model.ProjectFile;
import com.fdmgroup.projectmanagment.Model.Role;
import com.fdmgroup.projectmanagment.Model.User;
import com.fdmgroup.projectmanagment.Service.NotificationService;
import com.fdmgroup.projectmanagment.Service.ProjectFileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

@Controller
public class ProjectFileController {

    private ProjectFileService projectFileService;
    private NotificationService notificationService;

    @Autowired
    public ProjectFileController(ProjectFileService projectFileService,
                                 NotificationService notificationService) {
        super();
        this.projectFileService = projectFileService;
        this.notificationService = notificationService;
    }

    public boolean renderUser(HttpSession session, Model model) {

        if (session.getAttribute("user") == null) {
            return false;
        }

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("hasNotif", notificationService.countNotifactionsByUser(user));
        return true;
    }



    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable Long id) {

        ProjectFile projectFile = projectFileService.findFileById(id);
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(projectFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + projectFile.getName() + "\"")
                .body(new ByteArrayResource(projectFile.getData()));
    }



	@GetMapping("/uploadFile/{projectId}")
	public String getUploadFilePage(@PathVariable Long projectId, Model model, HttpSession httpSession) {

		if (!renderUser(httpSession, model)) {
			return "redirect:/login";
		}

		model.addAttribute("projectId", projectId);
		
		User user = (User) httpSession.getAttribute("user");
		
		if (user.getRole() == Role.Admin) {
			return "Dashboard/Project/uploadFileAdmin";
		}
		return "Project/uploadFile";
	}

	@GetMapping("/delete/{id}")
	public String deleteFileFromProjectPage(@PathVariable Long id ) {
		
		ProjectFile projectFile = projectFileService.findFileById(id) ;
		Long projectId = projectFile.getProject().getProjectId() ;
		
		projectFileService.deleteByProjectFileId(id) ;
		
		return "redirect:/project/" + projectId ;
	}


    /**
     * Delete for admin page
     * @param pid
     * @param fid
     * @param session
     * @param model
     * @return
     */

    @GetMapping("/projectSingleView/{pid}/viewFiles/delete/{fid}")
    public String removeProjectFile(@PathVariable Long pid, @PathVariable Long fid, HttpSession session, Model model) {
        projectFileService.deleteByProjectFileId(fid);

        return "redirect:/projectSingleView/" + pid;
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("projectId") String projectId,HttpSession httpSession) throws NumberFormatException, IOException {

        ProjectFile projectFile = projectFileService.saveProjectFile(file, Long.parseLong(projectId));


        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(projectFile.getName())
                .toUriString();



        User user = (User) httpSession.getAttribute("user");

        String adminUrl = "redirect:/projectSingleView/" + projectId;
        String url = "redirect:/project/" + projectId;

        if (user.getRole() == Role.Admin) {
            return adminUrl;
        }

        return url;
    }


}