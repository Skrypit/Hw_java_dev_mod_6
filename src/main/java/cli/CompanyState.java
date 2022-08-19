package cli;

import service.companies.Company;
import service.companies.CompanyDaoService;
import storage.ConnectionProvider;

import java.sql.SQLException;
import java.util.Scanner;

public class CompanyState  extends CliState{
    CliState state;
    Scanner scanner;
    ConnectionProvider connectionProvider;

    public CompanyState(CliFSM fsm) throws SQLException {
        super(fsm);

        scanner = fsm.getScanner();
        state = fsm.getState();
        connectionProvider = fsm.getConnectionProvider();

        System.out.println("Please, choose a command number\n" +
                "1- Create new company\n" +
                "2- Show company info\n" +
                "3- Update company info\n" +
                "4- Delete company\n" +
                "5- Back\n" +
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
                    createCompany();
                    break;
                case "2":
                    getCompany();
                    break;
                case "3":
                    updateCompany();
                    break;
                case "4":
                    deleteCompany();
                    break;
                case "5":
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

    private void createCompany() throws SQLException {

        Company company = new Company();
        setCompanyName(company, "1");
        setResidence(company, "1");

        try {
            new CompanyDaoService(connectionProvider.createConnection())
                    .create(company);
            System.out.println("The company created!\nChose next command");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        inputLoop();
    }

    private void setCompanyName(Company company, String command) {
        while (true) {
            System.out.println("Please, enter company name");
            String companyName = scanner.nextLine();
            try {
                company.setCompanyName(companyName);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void setResidence(Company company, String command) {
        while (true) {
            System.out.println("Please, enter residence");
            String residence = scanner.nextLine();
            try {
                company.setResidence(residence);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void setId(Company company, String command) {
        while (true) {
            System.out.println("Please, enter the id to which implement update");
            String id = scanner.nextLine();
            try {
                company.setId(Long.parseLong(id));
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    private void getCompany() throws SQLException {
        Company company = new Company();
        setCompanyName(company, "2");

        try {
            Company byName = new CompanyDaoService(connectionProvider
                    .createConnection())
                    .selectCompanyByName(company);
            System.out.println(byName + "\nChose next command");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        inputLoop();
    }

    private void updateCompany() throws SQLException {
        Company company = new Company();
        setId(company,"3");
        setCompanyName(company, "3");
        setResidence(company,"3");

        try {
            new CompanyDaoService(connectionProvider
                    .createConnection())
                    .update(company);
            System.out.println("Company updated!\nChose next command");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        inputLoop();
    }

    private void deleteCompany() throws SQLException {
        Company company = new Company();
        setCompanyName(company, "3");

        try {
            new CompanyDaoService(connectionProvider
                    .createConnection())
                    .removeByCompanyName(company);
            System.out.println("Company deleted!\nChose next command");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        inputLoop();
    }

}
