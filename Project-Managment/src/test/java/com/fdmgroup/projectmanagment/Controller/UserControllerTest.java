package com.fdmgroup.projectmanagment.Controller;

import com.fdmgroup.projectmanagment.Model.*;
import com.fdmgroup.projectmanagment.Service.NotificationService;
import com.fdmgroup.projectmanagment.Service.ProjectService;
import com.fdmgroup.projectmanagment.Service.SkillService;
import com.fdmgroup.projectmanagment.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class UserControllerTest {
    @Mock
    UserService userService;
    @Mock
    ProjectService projectService;
    @Mock
    SkillService skillService;
    @Mock
    NotificationService notificationService;
    @InjectMocks
    UserController userController;
    @Mock
    Model model;
    @Mock
    HttpSession httpSession;
    @Mock
    User mockUser;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void getUsers_test() {
        String result = userController.getUsers(model);

        verify(userService, times(1)).getAllUsers();

        assertEquals("Dashboard/User/allUsers", result);
    }


    @Test
    void deleteUserById_test() {
        Long id = 1L;

        String result = userController.deleteUser(id);

        verify(userService, times(1)).delete(id);
        assertEquals("redirect:/users", result);
    }

    @Test
    void deleteUserProfile_test() {
        Long id = 1L;
        String result = userController.deleteUserProfile(id, httpSession);

        verify(userService, times(1)).delete(id);
        verify(httpSession, times(1)).removeAttribute("user");

        assertEquals("redirect:/login", result);
    }

    @Test
    void getCreatedUser_test() {
        String result = userController.getCreatedUser(model);

        assertEquals("Auth/register", result);
    }

    @Test
    void processCreateUser_test() {
        User user = new User();
        String result = userController.processCreateUser(user);

        verify(userService, times(1)).save(user);

        assertEquals("redirect:/login", result);
    }

    @Test
    void editAlleUser() {
        Long id = 1L;
        User user = new User();

        when(userService.findById(id)).thenReturn(user);

        String result = userController.editAlleUser(id, model, httpSession);

        verify(userService, times(1)).findById(id);
        assertEquals("Dashboard/User/editAllUser",result);
    }

    @Test
    void saveAllUser(){
        User user = new User();
        String result = userController.saveAllUser(user);

        verify(userService, times(1)).save(user);
        assertEquals("redirect:/users", result);
    }

    @Test
    void editUserDisabled_test(){
        User user = new User();
        Long id = 1L;

        when(userService.findById(id)).thenReturn(user);
        String result = userController.editUser(id, model, httpSession);
        verify(userService, times(1)).findById(id);

        verify(model, times(1)).addAttribute("user", user);

        assertEquals("Dashboard/User/editUser", result);
    }
    @Test
    void editUserEnabled_test(){
        User user = new User();
        user.setEnabled(true);
        Long id = 1L;

        when(userService.findById(id)).thenReturn(user);
        String result = userController.editUser(id, model, httpSession);
        verify(userService, times(1)).findById(id);

        verify(model, times(1)).addAttribute("user", user);

        assertEquals("Dashboard/User/editUser", result);
    }
    
    @Test
    void getLoginPage_no_user_test(){
    	
    	when(httpSession.getAttribute("user")).thenReturn(mockUser);
    	
    	String result = userController.getLoginPage(model, httpSession);
    	
    	assertEquals("Dashboard/User/profile", result);
    }
    
    @Test
    void getLoginPage_test(){
        String result = userController.getLoginPage(model, httpSession);

        when(httpSession.getAttribute("user")).thenReturn("Dashboard/profile");
        verify(httpSession, times(1)).getAttribute("user");


        assertEquals("Auth/login", result);
    }

    @Test
    void endingSessionToLogout(){
        String result = userController.endingSessionToLogout(httpSession);
        verify(httpSession).removeAttribute("passenger");
        verify(httpSession).invalidate();

        assertEquals("redirect:/login",result);
    }

    @Test
    void getRegisterPage_test(){
        String result = userController.getRegisterPage(model);

        verify(model).addAttribute("user", new User());

        assertEquals(result,"Auth/register");
    }

    @Test
    void adminCreateUser_test(){
        String result = userController.adminCreateUser(model);

        verify(model).addAttribute("user", new User());

        assertEquals(result,"Dashboard/User/adminCreateU");
    }

    @Test
    void getDashboard_test(){
        String result = userController.getDashboard(model);

        verify(userService).countUsers();
        verify(projectService).countProjects();
        verify(projectService).countProjects();

        assertEquals(result, "Dashboard/index");
    }
    @Test
    void validateLoginPass_test() {
        User user = new User("Sarab","Singh","sarab","sarab@gmail.com","123456",Region.AU,Role.Admin,true);

        when(userService.checkLogin(user.getUsername(), user.getPassword())).thenReturn(user);

        userController.validateLogin(httpSession,user,model);

        verify(userService).checkLogin(user.getUsername(), user.getPassword());

        verify(httpSession).setAttribute("user", user);


        assertNotNull(userService.checkLogin(user.getUsername(), user.getPassword()));


        String test = "redirect:/";

        assertEquals(test, userController.validateLogin(httpSession,user,model));
    }

    @Test
    void validateLoginFail_test() {
        User user = new User("Sarab","Singh","sarab","sarab@gmail.com","123456",Region.AU,Role.Admin,true);

        when(userService.checkLogin(user.getUsername(), "s")).thenReturn(user);

        userController.validateLogin(httpSession,user,model);

        verify(model).addAttribute("error","INCORRECT_LOGIN_DETAILS");


        assertNull(userService.checkLogin(user.getUsername(), user.getPassword()));


        String test = "Auth/failedLogin";

        assertEquals(test, userController.validateLogin(httpSession,user,model));
    }

    @Test
    void filterMockProjects_test(){
        String result = userController.filterMockProjects("mock",model);

        verify(model).addAttribute("projects",projectService.findByProjectType(ProjectType.Mock));

        assertEquals(result,"Home/home");
    }

    @Test
    void filterMockProjectsClients_test(){
        String result = userController.filterMockProjects("client",model);

        verify(model).addAttribute("projects",projectService.findByProjectType(ProjectType.Client));

        assertEquals(result,"Home/home");
    }

    @Test
    void saveAdminCreate_test(){
        String result = userController.saveAdminUser(new User());
        verify(userService).save(new User());

        assertEquals(result,"redirect:/users");
    }
    
    @Test
    void getHomePage_user_test() {
    	
    	String result = userController.getHomePage(httpSession, model);
    	
    	assertEquals(result, "Landing/index");
    }
    
    @Test
    void getHomePage_no_user_test() {
    	
    	User user = new User();
    	
    	when(httpSession.getAttribute("user")).thenReturn(user);
    	
    	String result = userController.getHomePage(httpSession, model);

    	assertEquals(result, "Home/home");
    }
    
    @Test
    void getHomePage_is_admin_test() {
 
    	when(httpSession.getAttribute("user")).thenReturn(mockUser);
    	when(mockUser.getRole()).thenReturn(Role.Admin);
    	
    	String result = userController.getHomePage(httpSession, model);

    	assertEquals(result, "redirect:/dashboard");
    }
    
    @Test
    void processUpdateUser_is_admin_test() {
    	
    	when(httpSession.getAttribute("user")).thenReturn(mockUser);
    	when(mockUser.getRole()).thenReturn(Role.Admin);
    	
    	String result = userController.processUpdatedUser(mockUser, httpSession, model);
    	
    	assertEquals(result, "redirect:/profile/" + mockUser.getId());
    }
    
    @Test
    void processUpdateUser_updated_test() {
    	
    	when(httpSession.getAttribute("user")).thenReturn(mockUser);
    	when(mockUser.isEnabled()).thenReturn(true);
    	
    	String result = userController.processUpdatedUser(mockUser, httpSession, model);
    	
    	assertEquals(result, "redirect:/profile/" + mockUser.getId());
    }
}