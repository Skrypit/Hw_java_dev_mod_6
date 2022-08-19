package cli;

import java.sql.SQLException;

public class IdleState extends CliState {

    public IdleState(CliFSM fsm) {
        super(fsm);
    }

    @Override
    public void developers() throws SQLException {
        fsm.setState(new DeveloperState(fsm));
    }

    @Override
    public void companies() throws SQLException {
        fsm.setState(new CompanyState(fsm));
    }

    @Override
    public void customers() throws SQLException {
        fsm.setState(new CustomerState(fsm));
    }

    @Override
    public void projects() throws SQLException {
        fsm.setState(new ProjectState(fsm));
    }
    @Override
    public void unknownCommand(String cmd) {
        System.out.println("Unknown command: " + cmd + "\nPlease, enter the right command");
    }
}

