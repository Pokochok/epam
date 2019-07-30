package by.epam.dbworker.specification.impl.agentspecification;

import by.epam.dbworker.entity.User;
import by.epam.dbworker.specification.Specification;

import java.util.ArrayDeque;

public class FindAgentByLoginSpecification implements Specification<User> {
    private static final String FIND_SPECIFICATION_SQL_BY_LOGIN = "SELECT id, name, surname, email, phone_number, login, " +
            "password, role, status FROM agents WHERE login=?;";
    private String login;
    private ArrayDeque values;

    public FindAgentByLoginSpecification(String login) {
        this.login = login;
        values = new ArrayDeque(1);
        values.push(login);
    }

    @Override
    public boolean specify(User entity) {
        return login.equals(entity.getLogin());
    }

    @Override
    public String sqlQuery() {
        return FIND_SPECIFICATION_SQL_BY_LOGIN;
    }

    @Override
    public ArrayDeque getParameterQueue() {
        return values;
    }
}
