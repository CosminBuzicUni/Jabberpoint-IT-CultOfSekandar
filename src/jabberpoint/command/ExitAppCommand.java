package jabberpoint.command;

public class ExitAppCommand implements Command {
    private final Runnable exitAction;

    public ExitAppCommand(Runnable exitAction) {
        this.exitAction = exitAction;
    }

    public ExitAppCommand() {
        this(() -> System.exit(0));
    }

    @Override
    public void execute() {
        exitAction.run();
    }
}
