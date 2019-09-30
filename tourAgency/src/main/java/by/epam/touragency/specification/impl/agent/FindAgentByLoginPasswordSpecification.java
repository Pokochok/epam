package by.epam.touragency.specification.impl.agent;

import by.epam.touragency.entity.User;
import by.epam.touragency.specification.Specification;

import java.util.ArrayDeque;

public class FindAgentByLoginPasswordSpecification implements Specification<User> {
    private static final String FIND_SPECIFICATION_SQL_BY_LOGIN_PASSWORD = "SELECT id, name, surname, email, phone_number, login, " +
            "password, role, status FROM agents WHERE login=? AND password=?;";
    private String login;
    private String password;

    public FindAgentByLoginPasswordSpecification(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public String sqlQuery() {
        return FIND_SPECIFICATION_SQL_BY_LOGIN_PASSWORD;
    }
    @Override
    public ArrayDeque<Object> getParameterQueue() {
        ArrayDeque<Object> values = new ArrayDeque<>(2);
        values.push(password);
        values.push(login);
        return values;
    }
}
