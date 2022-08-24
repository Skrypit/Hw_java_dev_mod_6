package model.developers;

import model.Command;
import model.projects.Project;
import model.projects.ProjectDaoService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

public class DeveloperCreate  implements Command {
    private final Connection connection;

    public DeveloperCreate(Connection connection) {
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
            engine.process("developers/developer-create", simpleContext, resp.getWriter());
            resp.getWriter().close();
            return;
        }
        String firstName = req.getParameter("setFirstName");
        String lastName = req.getParameter("setLastName");
        String age = req.getParameter("setAge");
        String sex = req.getParameter("setSex");

        String result = "";
        String error = "";

        try {
            Developer developer = new Developer();
            developer.setFirstName(firstName);
            developer.setLastName(lastName);
            developer.setAge(Integer.parseInt(age));
            developer.setSex(Developer.Sex.valueOf(sex));

            DeveloperDaoService developerDaoService = new DeveloperDaoService(connection);
            result = developerDaoService.create(developer);
        } catch (Exception e) {
            error = e.getMessage();
        }

        resp.setContentType("text/html");
        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("message", result, "errorMessage", error)
        );

        engine.process("developers/developer-create", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}