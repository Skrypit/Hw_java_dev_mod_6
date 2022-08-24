package model.customers;

import model.Command;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.Map;
import java.util.Objects;

public class CustomerUpdate implements Command {
    private final Connection connection;

    public CustomerUpdate(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        if (!Objects.equals(req.getMethod(), "POST")) {
            resp.setContentType("text/html");

            Context simpleContext = new Context(
                    req.getLocale(),
                    Map.of("message", "",
                            "errorMessage", "")
            );
            engine.process("customers/customer-update", simpleContext, resp.getWriter());
            resp.getWriter().close();
            return;
        }

        long id = Long.parseLong(req.getParameter("setId"));
        String firstName = req.getParameter("setFirstName");
        String lastName = req.getParameter("setLastName");
        String email = req.getParameter("setEmail");

        String result = "";
        String error = "";

        try {
            Customer customer = new Customer();
            customer.setId(id);
            customer.setFirstName(firstName);
            customer.setLastName(lastName);
            customer.setEmail(email);

            CustomerDaoService customerDaoService = new CustomerDaoService(connection);
            result = customerDaoService.update(customer);
        } catch (Exception e) {
            error = e.getMessage();
        }

        resp.setContentType("text/html");
        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("message", result, "errorMessage", error)
        );

        engine.process("customers/customer-update", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}