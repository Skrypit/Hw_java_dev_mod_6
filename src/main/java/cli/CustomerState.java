package cli;

import service.customers.Customer;
import service.customers.CustomerDaoService;
import storage.ConnectionProvider;

import java.sql.SQLException;
import java.util.Scanner;

public class CustomerState extends CliState {

    CliState state;
    Scanner scanner;
    ConnectionProvider connectionProvider;

    public CustomerState(CliFSM fsm) throws SQLException {
        super(fsm);

        scanner = fsm.getScanner();
        state = fsm.getState();
        connectionProvider = fsm.getConnectionProvider();

        System.out.println("Please, choose a command number\n" +
                "1- Create new customer\n" +
                "2- Show customer info\n" +
                "3- Update customer\n" +
                "4- Delete customer\n" +
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
                    createCustomer();
                    break;
                case "2":
                    getCustomer();
                    break;
                case "3":
                    updateCustomer();
                    break;
                case "4":
                    deleteCustomer();
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

    private void createCustomer() throws SQLException {

        Customer customer = new Customer();
        setFirstName(customer, "1");
        setLastName(customer, "1");
        setEmail(customer, "1");


        try {
            new CustomerDaoService(connectionProvider.createConnection())
                    .create(customer);
            System.out.println("Customer created!\nChose next command");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        inputLoop();
    }

    private void setFirstName(Customer customer, String command) {
        while (true) {
            System.out.println("Please, enter first name");
            String firstName = scanner.nextLine();
            try {
                customer.setFirstName(firstName);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void setLastName(Customer customer, String command) {
        while (true) {
            System.out.println("Please, enter last name");
            String lastName = scanner.nextLine();
            try {
                customer.setLastName(lastName);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void setEmail(Customer customer, String command) {
        while (true) {
            System.out.println("Please, enter email");
            String email = scanner.nextLine();
            try {
                customer.setEmail(email);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void setId(Customer customer, String command) {
        while (true) {
            System.out.println("Please, enter the id to which implement update");
            String id = scanner.nextLine();
            try {
                customer.setId(Long.parseLong(id));
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void getCustomer() throws SQLException {
        Customer customer = new Customer();
        setFirstName(customer, "2");
        setLastName(customer, "2");

        try {
            Customer byName = new CustomerDaoService(connectionProvider
                    .createConnection())
                    .selectCustomerByName(customer);
            System.out.println(byName + "\nChose next command");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        inputLoop();
    }

    private void updateCustomer() throws SQLException {
        Customer customer = new Customer();
        setId(customer,"3");
        setFirstName(customer, "3");
        setLastName(customer, "3");
        setEmail(customer, "3");


        try {
            new CustomerDaoService(connectionProvider.createConnection())
                    .update(customer);
            System.out.println("Customer updated!\nChose next command");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        inputLoop();
    }

    private void deleteCustomer() throws SQLException {
        Customer customer = new Customer();
        setFirstName(customer, "3");
        setLastName(customer, "3");

        try {
            new CustomerDaoService(connectionProvider.createConnection())
                    .removeTheCustomer(customer);
            System.out.println("Customer deleted!\nChose next command");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        inputLoop();
    }

}
