package model.projects;

import model.Command;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProjectShowAll implements Command {
    private final Connection connection;

    public ProjectShowAll(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        if (!Objects.equals(req.getMethod(), "POST")) {
            resp.setContentType("text/html");

            Context simpleContext = new Context(
                    req.getLocale(),
                    Map.of("project", "null", "errorMessage", "")
            );
            engine.process("projects/project-get-all", simpleContext, resp.getWriter());
            resp.getWriter().close();
            return;
        }

        List <ProjectFormat> projects = null;
        String error = "";

        try {
            ProjectDaoService projectDaoService = new ProjectDaoService(connection);
            projects = projectDaoService.showListOfProjects();
        } catch (Exception e) {
            error = e.getMessage();
        }

        resp.setContentType("text/html");
        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("project", projects.size() == 0 ? "null" :
                        projects, "errorMessage", error)
        );
        engine.process("projects/project-get-all", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}

