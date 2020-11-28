package com.rentpal.accounts.controller;

import com.rentpal.accounts.service.interfaces.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

/**
 * This class performs unit test on UserController class.
 */
public class UserControllerTest extends ControllerAbstract{

    /**
     * The User controller.
     */
    @Autowired
    UserController userController;

    /**
     * The User service.
     */
    @MockBean
    UserService userService;

    /**
     * The Message source.
     */
    @MockBean
    MessageSource messageSource;

    private String email="test@gmail.com";

    private String password="Pass@123";

    private String token="ABC123";

    /**
     * Test get login.
     *
     * @throws Exception the exception
     */
    @Test
    void testGetLogin() throws Exception {
        MvcResult result=mvc.perform(MockMvcRequestBuilders.get("/login")).andReturn();
        Assertions.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    /**
     * Test get register.
     *
     * @throws Exception the exception
     */
    @Test
    void testGetRegister() throws Exception {
        MvcResult result=mvc.perform(MockMvcRequestBuilders.get("/register")).andReturn();
        Assertions.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    /**
     * Test post register for invalid CSRF.
     *
     * @throws Exception the exception
     */
    @Test
    void testPostRegisterForInvalidCRSF() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/register").with(csrf().useInvalidToken()))
                .andReturn();
        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), mvcResult.getResponse().getStatus());
    }

    /**
     * Test register for empty user.
     *
     * @throws Exception the exception
     */
    @Test
    void testRegisterForEmptyUser() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/register").with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andReturn();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    /**
     * Test register for invalid user.
     *
     * @throws Exception the exception
     */
    @Test
    void testRegisterForInvalidUser() throws Exception {
        MultiValueMap<String, String> params=new LinkedMultiValueMap<>();
        params.set("email","email");
        params.set("password","password");
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/register").params(params).with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)).andReturn();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    /**
     * Test register for invalid confirm password.
     *
     * @throws Exception the exception
     */
    @Test
    void testRegisterForInvalidConfirmPassword() throws Exception {
        MultiValueMap<String, String> params=new LinkedMultiValueMap<>();
        params.set("email",email);
        params.set("password", password);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/register").params(params).with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)).andReturn();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    /**
     * Test register for success.
     *
     * @throws Exception the exception
     */
    @Test
    void testRegisterForSuccess() throws Exception {
        MultiValueMap<String, String> params=new LinkedMultiValueMap<>();
        params.set("email",email);
        params.set("password", password);
        params.set("confirmPassword", password);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/register").params(params).with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)).andReturn();
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    /**
     * Test get verify for invalid token.
     *
     * @throws Exception the exception
     */
    @Test
    void testGetVerifyForInvalidToken() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/verify")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)).andReturn();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    /**
     * Test get verify.
     *
     * @throws Exception the exception
     */
    @Test
    void testGetVerify() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/verify").param("token", token)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)).andReturn();
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    /**
     * Test get reset for token.
     *
     * @throws Exception the exception
     */
    @Test
    void testGetResetForToken() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/reset").param("token", token)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)).andReturn();
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    /**
     * Test get reset without token.
     *
     * @throws Exception the exception
     */
    @Test
    void testGetResetWithoutToken() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/reset")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)).andReturn();
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    /**
     * Test post reset for invalid email.
     *
     * @throws Exception the exception
     */
    @Test
    void testPostResetForInvalidEmail() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/reset")
                .param("email","test").with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)).andReturn();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    /**
     * Test post reset.
     *
     * @throws Exception the exception
     */
    @Test
    void testPostReset() throws Exception{
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/reset")
                .param("email",email).with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)).andReturn();
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    /**
     * Test put reset for empty password.
     *
     * @throws Exception the exception
     */
    @Test
    void testPutResetForEmptyPassword() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/reset")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)).andReturn();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    /**
     * Test put reset for invalid password.
     *
     * @throws Exception the exception
     */
    @Test
    void testPutResetForInvalidPassword() throws Exception{
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/reset")
                .param("password","pass")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)).andReturn();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    /**
     * Test put reset for invalid confirm password.
     *
     * @throws Exception the exception
     */
    @Test
    void testPutResetForInvalidConfirmPassword() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/reset")
                .param("password", password)
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)).andReturn();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    /**
     * Test put reset for invalid token.
     *
     * @throws Exception the exception
     */
    @Test
    void testPutResetForInvalidToken() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/reset")
                .param("password", password)
                .param("confirmpassword", password)
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)).andReturn();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    /**
     * Test put reset.
     *
     * @throws Exception the exception
     */
    @Test
    void testPutReset() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/reset")
                .param("password", password)
                .param("confirmpassword", password)
                .param("token", token)
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)).andReturn();
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }
}
