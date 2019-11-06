package by.epam.touragency.handler;

import by.epam.touragency.resource.MessageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

import static by.epam.touragency.util.PageMsgConstant.LOGIN_ERROR_MSG_KEY;
import static by.epam.touragency.util.ParameterConstant.ATTR_NAME_ERROR_LOGIN;
import static by.epam.touragency.util.ParameterConstant.ATTR_NAME_LANGUAGE;

@Component
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {
    @Autowired
    private MessageManager messageManager;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        Locale language = (Locale) request.getSession().getAttribute(ATTR_NAME_LANGUAGE);
        request.setAttribute(ATTR_NAME_ERROR_LOGIN, messageManager.getProperty(LOGIN_ERROR_MSG_KEY, language));
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/fail_login");
        requestDispatcher.forward(request, response);
    }
}