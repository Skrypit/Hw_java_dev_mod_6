package model.projects;

import model.Command;
import model.developers.Developer;
import model.developers.DeveloperDaoService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.Map;
import java.util.Objects;

public class ProjectRead implements Command {
        private final Connection connection;

        public ProjectRead(Connection connection) {
            this.connection = connection;
        }

        @Override
        public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
            if (!Objects.equals(req.getMethod(), "POST")) {
                resp.setContentType("text/html");

                Context simpleContext = new Context(
                        req.getLocale(),
                        Map.of("project", "", "errorMessage", "")
                );
                engine.process("projects/project-get-by-name", simpleContext, resp.getWriter());
                resp.getWriter().close();
                return;
            }
            String name = req.getParameter("setProjectName");

            Project project = null;
            String error = "";

            try {
                ProjectDaoService projectDaoService = new ProjectDaoService(connection);
                project = projectDaoService.selectProjectByName(name);
            } catch (Exception e) {
                error = e.getMessage();
            }

            resp.setContentType("text/html");
            Context simpleContext = new Context(
                    req.getLocale(),
                    Map.of("project", project == null ? "null" :
                            project, "errorMessage", error)
            );
            engine.process("projects/project-get-by-name", simpleContext, resp.getWriter());
            resp.getWriter().close();
        }
    }

