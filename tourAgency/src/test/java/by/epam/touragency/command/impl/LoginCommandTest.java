package by.epam.touragency.command.impl;

import by.epam.touragency.config.WebAppTestContext;
import by.epam.touragency.entity.Role;
import by.epam.touragency.entity.User;
import by.epam.touragency.entity.UserPrincipal;
import by.epam.touragency.resource.ConfigurationManager;
import org.apache.commons.httpclient.Credentials;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;

import static by.epam.touragency.util.PageMsgConstant.HOME_PAGE_PATH;
import static by.epam.touragency.util.ParameterConstant.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitWebConfig(WebAppTestContext.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoginCommandTest {
    @Mock
    SecurityContext securityContext;

    @Mock
    Authentication authentication;

    @Mock
    UserPrincipal principal;

    @Mock
    LoginCommand loginCommandMock;

    @InjectMocks
    LoginCommand loginCommand;

    private MockMvc mockMvc;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(loginCommand).build();
    }

    @Test
    @DisplayName("Success execution")
    void execute() throws Exception {
        User client = new User.UserBuilder().setName("test").setSurname("test").setEmail("test@test.com").setLogin("test")
                .setPassword("test").setPhoneNumber("+0000000000").setRole(Role.CLIENT).setStatus("ACTIVE").setId(1).build();
        UserPrincipal userPrincipal = new UserPrincipal(client);
        when(loginCommandMock.isValid(any(SecurityContext.class))).thenReturn(true);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        mockMvc.perform(get("/login_setter").principal(userPrincipal))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl(ConfigurationManager.getProperty(HOME_PAGE_PATH)))
        .andExpect(model().attribute(ATTR_NAME_USER_ID, userPrincipal.getUserId()))
        .andExpect(model().attribute(ATTR_NAME_USER_NAME, userPrincipal.getRealUserName()))
        .andExpect(model().attribute(ATTR_NAME_USER_SURNAME, userPrincipal.getUserSurname()))
        .andExpect(model().attribute(ATTR_NAME_USER_EMAIL, userPrincipal.getUserEmail()))
        .andExpect(model().attribute(ATTR_NAME_USER_PHONE_NUMBER, userPrincipal.getUserPhoneNumber()))
        .andExpect(model().attribute(ATTR_NAME_USER_LOGIN, userPrincipal.getUsername()))
        .andExpect(model().attribute(ATTR_NAME_USER_STATUS, userPrincipal.getUserStatus()))
        .andExpect(model().attribute(ATTR_NAME_USER_ROLE, userPrincipal.getUserRole()));
    }
}