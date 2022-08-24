package model.developers;

import model.Command;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.Map;
import java.util.Objects;


public class DeveloperRead implements Command {
    private final Connection connection;

    public DeveloperRead(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        if (!Objects.equals(req.getMethod(), "POST")) {
            resp.setContentType("text/html");

            Context simpleContext = new Context(
                    req.getLocale(),
                    Map.of("developer", "", "errorMessage", "")
            );
            engine.process("developers/developer-get-by-name", simpleContext, resp.getWriter());
            resp.getWriter().close();
            return;
        }
        String firstName = req.getParameter("setFirstName");
        String lastName = req.getParameter("setLastName");

        Developer developer = null;
        String error = "";

        try {
            DeveloperDaoService developerDaoService = new DeveloperDaoService(connection);
            developer = developerDaoService.selectDevelopersByName(firstName, lastName);
        } catch (Exception e) {
            error = e.getMessage();
        }

        resp.setContentType("text/html");
        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("developer", developer == null ? "null" :
                        developer, "errorMessage", error)
        );
        engine.process("developers/developer-get-by-name", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}
