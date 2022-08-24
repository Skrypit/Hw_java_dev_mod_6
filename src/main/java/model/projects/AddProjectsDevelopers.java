package model.projects;

import model.Command;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.Map;
import java.util.Objects;

public class AddProjectsDevelopers implements Command {
    private final Connection connection;

    public AddProjectsDevelopers(Connection connection) {
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
            engine.process("projects/project-add-developer", simpleContext, resp.getWriter());
            resp.getWriter().close();
            return;
        }
        long projectId = Long.parseLong(req.getParameter("setProjectId"));
        long developerId = Long.parseLong(req.getParameter("setDeveloperId"));

        String result = "";
        String error = "";

        try {
            ProjectDaoService projectDaoService = new ProjectDaoService(connection);
            result = projectDaoService.addDeveloper(projectId, developerId);
        } catch (Exception e) {
            error = e.getMessage();
        }

        resp.setContentType("text/html");
        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("message", result, "errorMessage", error)
        );

        engine.process("projects/project-add-developer", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}