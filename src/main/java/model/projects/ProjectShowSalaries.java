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

public class ProjectShowSalaries implements Command {
    private final Connection connection;

    public ProjectShowSalaries(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        if (!Objects.equals(req.getMethod(), "POST")) {
            resp.setContentType("text/html");

            Context simpleContext = new Context(
                    req.getLocale(),
                    Map.of("sum", "", "errorMessage", "")
            );
            engine.process("projects/project-get-sum", simpleContext, resp.getWriter());
            resp.getWriter().close();
            return;
        }
        String name = req.getParameter("setProjectName");

        String sum = null;
        String error = "";

        try {
            ProjectDaoService projectDaoService = new ProjectDaoService(connection);
            sum = projectDaoService.showSumOfAllSalaries(name);
        } catch (Exception e) {
            error = e.getMessage();
        }

        resp.setContentType("text/html");
        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("sum", sum == null ? "null" :
                        sum, "errorMessage", error)
        );
        engine.process("projects/project-get-sum", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}

