package by.epam.touragency.repository.impl;

import by.epam.touragency.connectionpool.PropertyHolder;
import by.epam.touragency.entity.*;
import by.epam.touragency.exception.RepositoryException;
import by.epam.touragency.specification.Specification;
import by.epam.touragency.specification.impl.order.AddOrderSpecification;
import by.epam.touragency.specification.impl.order.FindAllOrdersSpecification;
import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import junit.framework.Assert;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public class OrderRepositoryTest {
    private static Flyway flyway;

    @BeforeAll
    public static void initDb() throws IOException, SQLException {
        EmbeddedPostgres pg = EmbeddedPostgres.start();
        Connection c = pg.getPostgresDatabase().getConnection();
        String url = pg.getJdbcUrl("postgres", "postgres");
        PropertyHolder propertyHolder = PropertyHolder.getInstance(url);
        flyway = Flyway.configure().dataSource(url, "postgres", "").load();
        flyway.migrate();
    }

    @AfterAll
    public static void destroy(){
        flyway.clean();
    }

    User client = new User.UserBuilder().setName("test").setSurname("test").setEmail("test@test.com").setLogin("test")
            .setPassword("test").setPhoneNumber("+0000000000").setRole(Role.CLIENT).setStatus("ACTIVE").setId(1).build();
    User agent = new User.UserBuilder().setName("test").setSurname("test").setEmail("test@test.com").setLogin("test")
            .setPassword("test").setPhoneNumber("+0000000000").setRole(Role.AGENT).setStatus("ACTIVE").build();
    Tour tour = new Tour.TourBuilder().setTourName("test").setAdultsNumber(0).setArrivalCity("test").setArrivalCountry("test")
            .setArrivalDate(1000l).setChildrenNumber(0).setDepartureCity("test").setDepartureDate(1000l).setHotel("test")
            .setNutrition("test").setPrice(new BigDecimal(1)).setStatus("AVAILABLE").setId(0).build();
    Ticket ticket = new Ticket.TicketBuilder().setTicketNumber(0).setArrivalCity("test").setArrivalDateTime(1000l).setDepartureCity("test")
            .setDepartureDateTime(1000l).setFlightNumber(0).setId(0).build();

    @Test
    public void getInstance() {
        OrderRepository orderRepository = OrderRepository.getInstance();
        boolean actual = orderRepository != null;
        Assert.assertTrue(actual);
    }

    @Test
    public void add() throws RepositoryException {
        Order order = new Order.OrderBuilder().setTour(tour).setTicket(ticket).setPaymentState(true).setClient(client).setAgent(agent).setId(1000).build();
        Specification specification = new AddOrderSpecification(order);
        int expected = OrderRepository.getInstance().query(new FindAllOrdersSpecification()).size() + 1;
        OrderRepository.getInstance().add(order, specification);
        int actual = OrderRepository.getInstance().query(new FindAllOrdersSpecification()).size();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void query() throws RepositoryException {
        boolean actual = OrderRepository.getInstance().query(new FindAllOrdersSpecification()).isEmpty();
        Assert.assertFalse(actual);
    }
}