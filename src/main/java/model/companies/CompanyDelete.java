package model.companies;

import model.Command;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.Map;
import java.util.Objects;

public class CompanyDelete implements Command {
    private final Connection connection;

    public CompanyDelete(Connection connection) {
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
            engine.process("companies/company-delete", simpleContext, resp.getWriter());
            resp.getWriter().close();
            return;
        }
        String name = req.getParameter("setName");

        String result = "";
        String error = "";

        try {
            CompanyDaoService companyDaoService = new CompanyDaoService(connection);
            result = companyDaoService.removeByCompanyName(name);
        } catch (Exception e) {
            error = e.getMessage();
        }

        resp.setContentType("text/html");
        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("message", result, "errorMessage", error)
        );

        engine.process("companies/company-delete", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}

