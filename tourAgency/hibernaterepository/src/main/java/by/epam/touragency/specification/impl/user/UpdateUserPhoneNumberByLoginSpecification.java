package by.epam.touragency.specification.impl.user;

import by.epam.touragency.entity.User;
import by.epam.touragency.specification.Specification;

import java.util.ArrayDeque;

public class UpdateUserPhoneNumberByLoginSpecification implements Specification<User> {
    private static final String UPDATE_PHONE_SPECIFICATION_SQL_BY_LOGIN =
            "UPDATE user SET phone_number=?1 WHERE login=?2;";
    private String phoneNumber;
    private String login;

    public UpdateUserPhoneNumberByLoginSpecification(String phoneNumber, String login) {
        this.phoneNumber = phoneNumber;
        this.login = login;
    }

    @Override
    public String sqlQuery() {
        return UPDATE_PHONE_SPECIFICATION_SQL_BY_LOGIN;
    }

    @Override
    public ArrayDeque<Object> getParameterQueue() {
        ArrayDeque<Object> values = new ArrayDeque<>(2);
        values.push(login);
        values.push(phoneNumber);
        return values;    }
}
