package cli;

import lombok.RequiredArgsConstructor;

import java.sql.SQLException;

@RequiredArgsConstructor
public class CliState {
    protected final CliFSM fsm;

    public void init() throws SQLException {}
    public void companies() throws SQLException {}
    public void customers() throws SQLException {}
    public void projects() throws SQLException {}
    public void developers() throws SQLException {}
    public void unknownCommand(String cmd) {}
    public void toMainMenu() throws SQLException {}

}