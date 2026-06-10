package jabberpoint.view;

import jabberpoint.command.Command;
import jabberpoint.command.ExitAppCommand;
import jabberpoint.command.GotoSlideCommand;
import jabberpoint.command.NewPresentationCommand;
import jabberpoint.command.NextSlideCommand;
import jabberpoint.command.OpenFileCommand;
import jabberpoint.command.PrevSlideCommand;
import jabberpoint.command.SaveFileCommand;
import jabberpoint.controller.KeyController;
import jabberpoint.controller.MenuController;
import jabberpoint.model.Presentation;

import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import javax.swing.JFrame;

/**
 * <p>The application window for a slideviewcomponent</p>
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.1 2002/12/17 Gert Florijn
 * @version 1.2 2003/11/19 Sylvia Stuurman
 * @version 1.3 2004/08/17 Sylvia Stuurman
 * @version 1.4 2007/07/16 Sylvia Stuurman
 * @version 1.5 2010/03/03 Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
*/

public class SlideViewerFrame extends JFrame {
	private static final long serialVersionUID = 3227L;

	private static final String JABTITLE = "Jabberpoint 1.6 - OU";
	public final static int WIDTH = 1200;
	public final static int HEIGHT = 800;

	public SlideViewerFrame(String title, Presentation presentation) {
		super(title);
		SlideViewerComponent slideViewerComponent = new SlideViewerComponent(presentation, this);
		presentation.addObserver(slideViewerComponent);
		setupWindow(slideViewerComponent, presentation);
	}

	public void setupWindow(SlideViewerComponent slideViewerComponent, Presentation presentation) {
		setTitle(JABTITLE);

		Command exit = new ExitAppCommand();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exit.execute();
			}
		});

		getContentPane().add(slideViewerComponent);

		Command next = new NextSlideCommand(presentation);
		Command prev = new PrevSlideCommand(presentation);
		Command open = new OpenFileCommand(presentation, this, MenuController.TESTFILE);
		Command newCmd = new NewPresentationCommand(presentation);
		Command save = new SaveFileCommand(presentation, this, MenuController.SAVEFILE);

		addKeyListener(new KeyController(next, prev, exit));
		setMenuBar(new MenuController(this, open, newCmd, save, next, prev, exit,
				pageNum -> new GotoSlideCommand(presentation, pageNum)));

		setSize(new Dimension(WIDTH, HEIGHT));
		setVisible(true);
	}
}
