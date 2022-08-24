package model.developers;

import model.Command;
import model.projects.ProjectDaoService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.Map;
import java.util.Objects;

public class DeveloperDelete implements Command {
        private final Connection connection;

        public DeveloperDelete(Connection connection) {
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
                engine.process("developers/developer-delete", simpleContext, resp.getWriter());
                resp.getWriter().close();
                return;
            }
            String firstName = req.getParameter("setFirstName");
            String lastName = req.getParameter("setLastName");

            String result = "";
            String error = "";

            try {
                DeveloperDaoService developerDaoService = new DeveloperDaoService(connection);
                result = developerDaoService.removeTheDeveloper(firstName, lastName);
            } catch (Exception e) {
                error = e.getMessage();
            }

            resp.setContentType("text/html");
            Context simpleContext = new Context(
                    req.getLocale(),
                    Map.of("message", result, "errorMessage", error)
            );

            engine.process("developers/developer-delete", simpleContext, resp.getWriter());
            resp.getWriter().close();
        }
    }
