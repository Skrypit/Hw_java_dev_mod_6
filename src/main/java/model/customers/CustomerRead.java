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

public class CustomerRead implements Command {
    private final Connection connection;

    public CustomerRead(Connection connection) {
        this.connection = connection;
    }
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        if (!Objects.equals(req.getMethod(), "POST")) {
            resp.setContentType("text/html");

            Context simpleContext = new Context(
                    req.getLocale(),
                    Map.of("customer", "", "errorMessage", "")
            );
            engine.process("customers/customer-get-by-name", simpleContext, resp.getWriter());
            resp.getWriter().close();
            return;
        }
        String firstName = req.getParameter("setFirstName");
        String lastName = req.getParameter("setLastName");
        Customer customer = null;
        String error = "";

        try {
            CustomerDaoService customerDaoService = new CustomerDaoService(connection);
            customer = customerDaoService.selectCustomerByName(firstName, lastName);
        } catch (Exception e) {
            error = e.getMessage();
        }

        resp.setContentType("text/html");
        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("customer", customer == null ? "null" :
                        customer, "errorMessage", error)
        );
        engine.process("customers/customer-get-by-name", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}