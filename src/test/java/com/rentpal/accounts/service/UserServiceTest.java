package com.rentpal.accounts.service;

import com.rentpal.accounts.common.DTOModelMapper;
import com.rentpal.accounts.exception.APIRequestException;
import com.rentpal.accounts.model.Token;
import com.rentpal.accounts.model.TokenId;
import com.rentpal.accounts.model.User;
import com.rentpal.accounts.repository.TokenRepository;
import com.rentpal.accounts.repository.UserRepository;
import com.rentpal.accounts.service.interfaces.EmailService;
import com.rentpal.accounts.service.interfaces.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.mail.MessagingException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * This class performs unit test on UserService class.
 */
@SpringBootTest
public class UserServiceTest {

    /**
     * The User service.
     */
    @Autowired
    UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private EmailService emailService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TokenRepository tokenRepository;

    private Long userId=1l;

    private String email="test@gmail.com";

    private String password="Pass@123";

    private String id="123";

    /**
     * Test password mismatch for new user.
     */
    @Test
    void testPasswordMismatchForNewUser() {
        User user= Mockito.mock(User.class);
        when(user.getEmail()).thenReturn(email);
        when(user.getPassword()).thenReturn(password);
        Exception exception = assertThrows(APIRequestException.class, () -> {
            userService.addUser(user, "test@123");
        });
        assertEquals("error.register.password.mismatch", exception.getMessage());
    }

    /**
     * Test if email exist for new user.
     */
    @Test
    void testIfEmailExistForNewUser(){
        User user= Mockito.mock(User.class);
        when(user.getEmail()).thenReturn(email);
        when(user.getPassword()).thenReturn(password);
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);
        Exception exception = assertThrows(APIRequestException.class, () -> {
            userService.addUser(user, password);
        });
        assertEquals("error.user.email.exists", exception.getMessage());
    }

    /**
     * Test new user.
     *
     * @throws MessagingException the messaging exception
     */
    @Test
    void testNewUser() throws MessagingException {
        User user= Mockito.mock(User.class);
        when(user.getEmail()).thenReturn(email);
        when(user.getPassword()).thenReturn(password);
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.save(user)).thenReturn(mock(User.class));
        userService.addUser(user, password);
    }

    /**
     * Test if verification token exists.
     */
    @Test
    void testIfVerificationTokenExists(){
        when(tokenRepository.findById(id)).thenReturn(Optional.empty());
        Exception exception = assertThrows(APIRequestException.class, () -> {
            userService.verifyAccountForUser(id);
        });
        assertEquals("error.user.verification", exception.getMessage());
    }

    /**
     * Test if verification token expired.
     */
    @Test
    void testIfVerificationTokenExpired(){
        Token token=mock(Token.class);
        when(tokenRepository.findById(id)).thenReturn(Optional.of(token));
        when(token.getCreationTime()).thenReturn(0l);
        when(token.getTokenId()).thenReturn(mock(TokenId.class));
        when(token.getTokenId().getUser()).thenReturn(mock(User.class));
        when(token.getTokenId().getUser().getId()).thenReturn(userId);
        Exception exception = assertThrows(APIRequestException.class, () -> {
            userService.verifyAccountForUser(id);
        });
        assertEquals("error.user.verification", exception.getMessage());
    }

    /**
     * Test if account verified.
     */
    @Test
    void testIfAccountVerified(){
        Token token=mock(Token.class);
        when(tokenRepository.findById(id)).thenReturn(Optional.of(token));
        when(token.getCreationTime()).thenReturn(System.currentTimeMillis());
        when(token.getTokenId()).thenReturn(mock(TokenId.class));
        when(token.getTokenId().getUser()).thenReturn(mock(User.class));
        when(token.getTokenId().getUser().getId()).thenReturn(userId);
        userService.verifyAccountForUser(id);
    }

    /**
     * Test reset password link.
     */
    @Test
    void testResetPasswordLink(){
        User user=mock(User.class);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(user.isVerified()).thenReturn(true);
    }

    /**
     * Test if token exists for user.
     */
    @Test
    void testIfTokenExistsForUser(){
        when(tokenRepository.findById(id)).thenReturn(Optional.empty());
        Exception exception = assertThrows(APIRequestException.class, () -> {
            userService.getUserEmailForToken(id);
        });
        assertEquals("error.user.reset", exception.getMessage());
    }

    /**
     * Test if token expired for user.
     */
    @Test
    void testIfTokenExpiredForUser(){
        Token token=mock(Token.class);
        when(tokenRepository.findById(id)).thenReturn(Optional.of(token));
        when(token.getCreationTime()).thenReturn(0l);
        Exception exception = assertThrows(APIRequestException.class, () -> {
            userService.getUserEmailForToken(id);
        });
        assertEquals("error.user.reset", exception.getMessage());
    }

    /**
     * Test get user email for token.
     */
    @Test
    void testGetUserEmailForToken(){
        Token token=mock(Token.class);
        when(tokenRepository.findById(id)).thenReturn(Optional.of(token));
        when(token.getCreationTime()).thenReturn(System.currentTimeMillis());
        when(token.getTokenId()).thenReturn(mock(TokenId.class));
        when(token.getTokenId().getUser()).thenReturn(mock(User.class));
        when(token.getTokenId().getUser().getEmail()).thenReturn(email);
        assertEquals(email, userService.getUserEmailForToken(id));
    }

    /**
     * Test reset password match.
     */
    @Test
    void testResetPasswordMatch(){
        Exception exception = assertThrows(APIRequestException.class, () -> {
            userService.resetPassword(id, email, "test@123");
        });
        assertEquals("error.register.password.mismatch", exception.getMessage());
    }

    /**
     * Test if token exist for reset.
     */
    @Test
    void testIfTokenExistForReset(){
        when(tokenRepository.findById(id)).thenReturn(Optional.empty());
        Exception exception = assertThrows(APIRequestException.class, () -> {
            userService.resetPassword(id, password, password);
        });
        assertEquals("error.user.reset", exception.getMessage());
    }

    /**
     * Test if token expired for reset.
     */
    @Test
    void testIfTokenExpiredForReset(){
        Token token=mock(Token.class);
        when(tokenRepository.findById(id)).thenReturn(Optional.of(token));
        when(token.getCreationTime()).thenReturn(0l);
        Exception exception = assertThrows(APIRequestException.class, () -> {
            userService.resetPassword(id, password, password);
        });
        assertEquals("error.user.reset", exception.getMessage());
    }

    /**
     * Test reset password.
     */
    @Test
    void testResetPassword(){
        Token token=mock(Token.class);
        when(tokenRepository.findById(id)).thenReturn(Optional.of(token));
        when(token.getCreationTime()).thenReturn(System.currentTimeMillis());
        when(token.getTokenId()).thenReturn(mock(TokenId.class));
        when(token.getTokenId().getUser()).thenReturn(mock(User.class));
        when(token.getTokenId().getUser().getId()).thenReturn(userId);
        userService.resetPassword(id, password, password);
    }

    /**
     * Test password mismatch for change.
     */
    @Test
    void testPasswordMismatchForChange(){
        Exception exception = assertThrows(APIRequestException.class, () -> {
            userService.changePassword(password, "Test@123");
        });
        assertEquals("error.register.password.mismatch", exception.getMessage());
    }
}
