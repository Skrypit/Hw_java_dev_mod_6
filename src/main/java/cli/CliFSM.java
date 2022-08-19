package cli;

import lombok.Getter;
import storage.ConnectionProvider;

import java.sql.SQLException;
import java.util.Scanner;

public class CliFSM {
    @Getter
    private CliState state;

    @Getter
    private Scanner scanner;

    @Getter
    private ConnectionProvider connectionProvider;

    String helloMessage =
            "Please, choose a command number from the list below\n" +
                    "1- Developers\n" +
                    "2- Companies\n" +
                    "3- Customers\n" +
                    "4- Projects\n" +
                    "0- Exit program";

    public CliFSM(ConnectionProvider connectionProvider) throws SQLException {
        this.connectionProvider = connectionProvider;

        state = new IdleState(this);

        scanner = new Scanner(System.in);

        startInputLoop();
    }

    private void startInputLoop() throws SQLException {
        System.out.println(helloMessage);
        while (true) {
            String command = scanner.nextLine();

            switch (command) {
                case "0":
                    System.exit(0);
                    break;
                case "1":
                    developers();
                    break;
                case "2":
                    companies();
                    break;
                case "3":
                    customers();
                    break;
                case "4":
                    projects();
                    break;
                default:
                    unknownCommand(command);
            }
        }
    }

    public void developers() throws SQLException {
        state.developers();
    }

    public void companies() throws SQLException {
        state.companies();
    }

    public void customers() throws SQLException {
        state.customers();
    }

    public void projects() throws SQLException {
        state.projects();
    }

    public void unknownCommand(String cmd) {
        state.unknownCommand(cmd);
    }

    public void setState(CliState state) throws SQLException {
        this.state = state;
        state.init();
    }
}