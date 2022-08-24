package model.projects;

import model.Command;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

public class ProjectCreate implements Command {
    private final Connection connection;

    public ProjectCreate(Connection connection) {
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
            engine.process("projects/project-create", simpleContext, resp.getWriter());
            resp.getWriter().close();
            return;
        }
        String projectName = req.getParameter("setProjectName");
        String type = req.getParameter("setType");
        String description = req.getParameter("setDescription");

        String result = "";
        String error = "";

        try {
            Project project = new Project();
            project.setProjectName(projectName);
            project.setType(type);
            project.setDescription(description);
            project.setCreationDate(LocalDate.now());

            ProjectDaoService projectDaoService = new ProjectDaoService(connection);
            result = projectDaoService.create(project);
        } catch (Exception e) {
            error = e.getMessage();
        }

        resp.setContentType("text/html");
        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("message", result, "errorMessage", error)
        );

        engine.process("projects/project-create", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}