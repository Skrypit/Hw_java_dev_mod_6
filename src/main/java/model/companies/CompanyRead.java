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

public class CompanyRead implements Command {
    private final Connection connection;

    public CompanyRead(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        if (!Objects.equals(req.getMethod(), "POST")) {
            resp.setContentType("text/html");

            Context simpleContext = new Context(
                    req.getLocale(),
                    Map.of("company", "", "errorMessage", "")
            );
            engine.process("companies/company-get-by-name", simpleContext, resp.getWriter());
            resp.getWriter().close();
            return;
        }
        String name = req.getParameter("setCompanyName");
        Company company = null;
        String error = "";

        try {
            CompanyDaoService companyDaoService = new CompanyDaoService(connection);
            company = companyDaoService.selectCompanyByName(name);
        } catch (Exception e) {
            error = e.getMessage();
        }

        resp.setContentType("text/html");
        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("company", company == null ? "null" :
                        company, "errorMessage", error)
        );
        engine.process("companies/company-get-by-name", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}