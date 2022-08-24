package controller;

import controller.storage.ConnectionProvider;
import model.Command;
import model.TableSelect;
import model.companies.CompanyCreate;
import model.companies.CompanyDelete;
import model.companies.CompanyMainPage;
import model.companies.CompanyRead;
import model.companies.CompanyUpdate;
import model.customers.*;
import model.developers.*;
import model.projects.*;
import org.thymeleaf.TemplateEngine;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

public class CommandService {
    private Connection connection;
    private final Map<String, Command> commands;

    public CommandService() {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            connection = new ConnectionProvider().createConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        commands = new HashMap<>();

        commands.put("GET /", new TableSelect());

        commands.put("GET /projects", new ProjectMainPage());
        commands.put("GET /project/create", new ProjectCreate(connection));
        commands.put("POST /project/create", new ProjectCreate(connection));
        commands.put("GET /project/add-developer", new AddProjectsDevelopers(connection));
        commands.put("POST /project/add-developer", new AddProjectsDevelopers(connection));
        commands.put("GET /project/delete", new ProjectDelete(connection));
        commands.put("POST /project/delete", new ProjectDelete(connection));
        commands.put("GET /project/get-by-name", new ProjectRead(connection));
        commands.put("POST /project/get-by-name", new ProjectRead(connection));
        commands.put("GET /project/update", new ProjectUpdate(connection));
        commands.put("POST /project/update", new ProjectUpdate(connection));
        commands.put("GET /project/get-sum", new ProjectShowSalaries(connection));
        commands.put("POST /project/get-sum", new ProjectShowSalaries(connection));
        commands.put("GET /project/get-all", new ProjectShowAll(connection));
        commands.put("POST /project/get-all", new ProjectShowAll(connection));


        commands.put("GET /customers", new CustomerMainPage());
        commands.put("GET /customer/create", new CustomerCreate(connection));
        commands.put("POST /customer/create", new CustomerCreate(connection));
        commands.put("GET /customer/delete", new CustomerDelete(connection));
        commands.put("POST /customer/delete", new CustomerDelete(connection));
        commands.put("GET /customer/get-by-name", new CustomerRead(connection));
        commands.put("POST /customer/get-by-name", new CustomerRead(connection));
        commands.put("GET /customer/update", new CustomerUpdate(connection));
        commands.put("POST /customer/update", new CustomerUpdate(connection));


        commands.put("GET /developers", new DeveloperMainPage());
        commands.put("GET /developer/create", new DeveloperCreate(connection));
        commands.put("POST /developer/create", new DeveloperCreate(connection));
        commands.put("GET /developer/get-by-name", new DeveloperRead(connection));
        commands.put("POST /developer/get-by-name", new DeveloperRead(connection));
        commands.put("GET /developer/update", new DeveloperUpdate(connection));
        commands.put("POST /developer/update", new DeveloperUpdate(connection));
        commands.put("GET /developer/delete", new DeveloperDelete(connection));
        commands.put("POST /developer/delete", new DeveloperDelete(connection));
        commands.put("GET /developer/get-by-language", new DeveloperSelectByLanguage(connection));
        commands.put("POST /developer/get-by-language", new DeveloperSelectByLanguage(connection));
        commands.put("GET /developer/get-by-level", new DeveloperSelectByLevel(connection));
        commands.put("POST /developer/get-by-level", new DeveloperSelectByLevel(connection));
        commands.put("GET /developer/get-by-project", new DeveloperSelectByProject(connection));
        commands.put("POST /developer/get-by-project", new DeveloperSelectByProject(connection));


        commands.put("GET /companies", new CompanyMainPage());
        commands.put("GET /company/create", new CompanyCreate(connection));
        commands.put("POST /company/create", new CompanyCreate(connection));
        commands.put("GET /company/get-by-name", new CompanyRead(connection));
        commands.put("POST /company/get-by-name", new CompanyRead(connection));
        commands.put("GET /company/update", new CompanyUpdate(connection));
        commands.put("POST /company/update", new CompanyUpdate(connection));
        commands.put("GET /company/delete", new CompanyDelete(connection));
        commands.put("POST /company/delete", new CompanyDelete(connection));
    }

    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        String requestUri = req.getRequestURI();
        String commandKey = req.getMethod() + " " + requestUri;
        commands.get(commandKey).process(req, resp, engine);
    }
}
