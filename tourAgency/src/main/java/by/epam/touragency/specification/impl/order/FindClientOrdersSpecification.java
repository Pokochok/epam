package by.epam.touragency.specification.impl.order;

import by.epam.touragency.entity.Order;
import by.epam.touragency.specification.Specification;

import java.util.ArrayDeque;

public class FindClientOrdersSpecification implements Specification<Order> {
    private static final String FIND_ALL_SPECIFICATION_SQL = "SELECT id, payment_state, id_tour, id_ticket, id_client, " +
            "id_agent FROM orders WHERE id_client = ?;";
    private int clientId;

    public FindClientOrdersSpecification(int clientId) {
        this.clientId = clientId;
    }

    @Override
    public String sqlQuery() {
        return FIND_ALL_SPECIFICATION_SQL;
    }

    @Override
    public ArrayDeque<Object> getParameterQueue() {
        ArrayDeque<Object> values = new ArrayDeque<>(1);
        values.add(clientId);
        return values;
    }
}
