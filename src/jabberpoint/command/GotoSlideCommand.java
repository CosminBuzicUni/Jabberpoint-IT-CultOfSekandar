package jabberpoint.command;

import jabberpoint.model.Presentation;

public class GotoSlideCommand implements Command {
    private final Presentation presentation;
    private final int targetSlide; // zero-based

    public GotoSlideCommand(Presentation presentation, int targetSlide) {
        this.presentation = presentation;
        this.targetSlide = targetSlide;
    }

    @Override
    public void execute() {
        presentation.setSlideNumber(targetSlide);
    }
}
