package cli;

import service.developers.Developer;
import service.developers.DeveloperDaoService;
import storage.ConnectionProvider;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class DeveloperState extends CliState {

    CliState state;
    Scanner scanner;
    ConnectionProvider connectionProvider;

    public DeveloperState(CliFSM fsm) throws SQLException {
        super(fsm);

        scanner = fsm.getScanner();
        state = fsm.getState();
        connectionProvider = fsm.getConnectionProvider();

        System.out.println("Please, choose a command number\n" +
                "1- Create new developer\n" +
                "2- Show developer info\n" +
                "3- Update developer\n" +
                "4- Delete developer\n" +
                "5- Show all developers which work on the project...\n" +
                "6- Show all developers who know the language...\n" +
                "7- Show all developers who have skills level...\n" +
                "8- Back\n" +
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
                    createDeveloper();
                    break;
                case "2":
                    getDeveloper();
                    break;
                case "3":
                    updateDeveloper();
                    break;
                case "4":
                    deleteDeveloper();
                    break;
                case "5":
                    getByProject();
                    break;
                case "6":
                    getByLanguage();
                    break;
                case "7":
                    getByLevel();
                    break;
                case "8":
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

    public void unknownCommand(String cmd) {
        state.unknownCommand(cmd);
    }

    @Override
    public void toMainMenu() throws SQLException {
        new CliFSM(connectionProvider);
    }

    private void createDeveloper() throws SQLException {

        Developer developer = new Developer();
        setFirstName(developer, "1");
        setLastName(developer, "1");
        setAge(developer, "1");
        setSex(developer, "1");
        try {
            new DeveloperDaoService(connectionProvider.createConnection()).create(developer);
            System.out.println("Developer created!\nChoose next command");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        inputLoop();
    }

    private void setFirstName(Developer developer, String command) {
        while (true) {
            System.out.println("Please, enter first name");
            String firstName = scanner.nextLine();
            try {
                developer.setFirstName(firstName);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void setLastName(Developer developer, String command) {
        while (true) {
            System.out.println("Please, enter last name");
            String lastName = scanner.nextLine();
            try {
                developer.setLastName(lastName);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void setAge(Developer developer, String command) {
        while (true) {
            System.out.println("Please, enter age");
            try {
                int age = Integer.parseInt(scanner.nextLine());
                developer.setAge(age);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void setSex(Developer developer, String command) {
        while (true) {
            System.out.println("Please, enter sex (male, female or other)");
            String sex = scanner.nextLine();
            try {
                developer.setSex(Developer.Sex.valueOf(sex));
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void setId(Developer developer, String command) {
        while (true) {
            System.out.println("Please, enter the id to which implement update");
            String id = scanner.nextLine();
            try {
                developer.setId(Long.parseLong(id));
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void getDeveloper() throws SQLException {

        Developer developer = new Developer();
        setFirstName(developer, "2");
        setLastName(developer, "2");

        try {
            Developer byName = new DeveloperDaoService(connectionProvider.createConnection()).selectDevelopersByName(developer);
            System.out.println(byName);
            System.out.println("\nChoose next command");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        inputLoop();
    }

    private void updateDeveloper() throws SQLException {
        Developer developer = new Developer();
        setId(developer,"3");
        setFirstName(developer, "3");
        setLastName(developer, "3");
        setAge(developer, "3");
        setSex(developer, "3");


        try {
            new DeveloperDaoService(connectionProvider.createConnection()).update(developer);
            System.out.println("Developer updated!\nChoose next command");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        inputLoop();
    }


    private void deleteDeveloper() throws SQLException {
        Developer developer = new Developer();
        setFirstName(developer, "4");
        setLastName(developer, "4");
        try {
            new DeveloperDaoService(connectionProvider.createConnection())
                    .removeTheDeveloper(developer);
            System.out.println("Developer deleted!\nChoose next command");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        inputLoop();
    }

    private void getByProject() throws SQLException {
        System.out.println("Please, enter project name");
        String project = scanner.nextLine();

        try {
            List<Developer> byProject = new DeveloperDaoService(connectionProvider
                    .createConnection())
                    .selectDeveloperByProject(project);
            System.out.println(byProject);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        inputLoop();
    }

    private void getByLanguage() throws SQLException {
        System.out.println("Please, enter language (Java, JS, C++)");
        String language = scanner.nextLine();

        try {
            List<Developer> byLanguage = new DeveloperDaoService(connectionProvider
                    .createConnection())
                    .selectDevelopersByLanguage(language);
            System.out.println(byLanguage);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        inputLoop();
    }

    private void getByLevel() throws SQLException {
        System.out.println("Please, enter level (Junior, Middle, Senior)");
        String level = scanner.nextLine();
        try {
            List<Developer> byLevel = new DeveloperDaoService(connectionProvider
                    .createConnection())
                    .selectDevelopersByLevel(level);
            System.out.println(byLevel);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        inputLoop();
    }
}


