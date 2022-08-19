package cli;

import service.projects.Project;
import service.projects.ProjectDaoService;
import storage.ConnectionProvider;

import java.sql.SQLException;
import java.util.Scanner;

public class ProjectState extends CliState {

    CliState state;
    Scanner scanner;
    ConnectionProvider connectionProvider;

    public ProjectState(CliFSM fsm) throws SQLException {
        super(fsm);
        scanner = fsm.getScanner();
        state = fsm.getState();
        connectionProvider = fsm.getConnectionProvider();

        System.out.println("Please, choose a command number\n" +
                "1- Create new project\n" +
                "2- Show project info\n" +
                "3- Update project\n" +
                "4- Delete project\n" +
                "5- Show all projects\n" +
                "6- Show sum of all salaries on the project...\n" +
                "7- Back\n" +
                "0- Exit program"
        );
        inputLoop();
    }

    private void inputLoop() throws SQLException {
        while (true) {
            String command = scanner.nextLine();

            switch (command) {
                case "0":
                    System.exit(0);
                    break;
                case "1":
                    createProject();
                    break;
                case "2":
                    getProject();
                    break;
                case "3":
                    updateProject();
                    break;
                case "4":
                    deleteProject();
                    break;
                case "5":
                    getAllProjects();
                    break;
                case "6":
                    getSalaries();
                    break;
                case "7":
                    toMainMenu();
                    break;
                default:
                    unknownCommand(command);
            }
        }
    }


    @Override
    public void init() throws SQLException {
        inputLoop();
    }

    @Override
    public void unknownCommand(String cmd) {
        System.out.println("Unknown command: " + cmd + "\nPlease, enter the right command");
    }

    @Override
    public void toMainMenu() throws SQLException {
        new CliFSM(connectionProvider);
    }


    private void createProject() throws SQLException {

        Project project = new Project();
        setProjectName(project, "1");
        setType(project, "1");
        setDescription(project, "1");

        try {
            new ProjectDaoService(connectionProvider.createConnection())
                    .create(project);
            System.out.println("Project created!\nChose next command");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        inputLoop();
    }

    private void setProjectName(Project project, String command) {
        while (true) {
            System.out.println("Please, enter project name");
            String projectName = scanner.nextLine();
            try {
                project.setProjectName(projectName);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void setType(Project project, String command) {
        while (true) {
            System.out.println("Please, enter company type");
            String type = scanner.nextLine();
            try {
                project.setType(type);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void setDescription(Project project, String command) {
        while (true) {
            System.out.println("Please, enter company description");
            String description = scanner.nextLine();
            try {
                project.setDescription(description);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    private void setId(Project project, String command) {
        while (true) {
            System.out.println("Please, enter the id to which implement update");
            String id = scanner.nextLine();
            try {
                project.setId(Long.parseLong(id));
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void getProject() throws SQLException {
        Project project = new Project();
        setProjectName(project, "2");

        try {
            Project byName = new ProjectDaoService(connectionProvider
                    .createConnection())
                    .selectProjectByName(project);
            System.out.println(byName + "\nChose next command");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        inputLoop();
    }

    private void updateProject() throws SQLException {
        Project project = new Project();
        setId(project,"3");
        setProjectName(project, "3");
        setType(project, "3");
        setDescription(project, "3");

        try {
            new ProjectDaoService(connectionProvider
                    .createConnection())
                    .update(project);
            System.out.println("Project updated!\nChose next command");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        inputLoop();
    }

    private void deleteProject() throws SQLException {
        Project project = new Project();
        setProjectName(project, "4");

        try {
            new ProjectDaoService(connectionProvider
                    .createConnection())
                    .removeTheProject(project);
            System.out.println("Project deleted!\nChose next command");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        inputLoop();
    }

    private void getAllProjects() throws SQLException {

        try {
            new ProjectDaoService(connectionProvider
                    .createConnection())
                    .showListOfProjects();
            System.out.println("\nChose next command");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        inputLoop();
    }

    private void getSalaries() throws SQLException {
        System.out.println("Please, enter project name");
        String level = scanner.nextLine();
        try {
            new ProjectDaoService(connectionProvider
                    .createConnection())
                    .showSumOfAllSalaries(level);
            System.out.println("\nChose next command");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        inputLoop();
    }

}
