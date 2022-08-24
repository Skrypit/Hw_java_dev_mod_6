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

public class CompanyUpdate implements Command {
    private final Connection connection;

    public CompanyUpdate(Connection connection) {
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
            engine.process("companies/company-update", simpleContext, resp.getWriter());
            resp.getWriter().close();
            return;
        }

        long id = Long.parseLong(req.getParameter("setId"));
        String setName = req.getParameter("setName");
        String residence = req.getParameter("setResidence");

        String result = "";
        String error = "";

        try {
            Company company = new Company();
            company.setId(id);
            company.setCompanyName(setName);
            company.setResidence(residence);
            CompanyDaoService companyDaoService = new CompanyDaoService(connection);
            result = companyDaoService.update(company);
        } catch (Exception e) {
            error = e.getMessage();
        }

        resp.setContentType("text/html");
        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("message", result, "errorMessage", error)
        );

        engine.process("companies/company-update", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}
