public class NewPresentationCommand implements Command {
    private final Presentation presentation;

    public NewPresentationCommand(Presentation presentation) {
        this.presentation = presentation;
    }

    @Override
    public void execute() {
        presentation.clear();
    }
}
