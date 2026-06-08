import java.awt.MenuBar;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.function.IntFunction;

import javax.swing.JOptionPane;

/** <p>The controller for the menu</p>
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.1 2002/12/17 Gert Florijn
 * @version 1.2 2003/11/19 Sylvia Stuurman
 * @version 1.3 2004/08/17 Sylvia Stuurman
 * @version 1.4 2007/07/16 Sylvia Stuurman
 * @version 1.5 2010/03/03 Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 */
public class MenuController extends MenuBar {

	private final Frame parent;
	private final Command openCommand;
	private final Command newCommand;
	private final Command saveCommand;
	private final Command nextCommand;
	private final Command prevCommand;
	private final Command exitCommand;
	private final IntFunction<Command> gotoCommandFactory;

	private static final long serialVersionUID = 227L;

	protected static final String ABOUT = "About";
	protected static final String FILE = "File";
	protected static final String EXIT = "Exit";
	protected static final String GOTO = "Go to";
	protected static final String HELP = "Help";
	protected static final String NEW = "New";
	protected static final String NEXT = "Next";
	protected static final String OPEN = "Open";
	protected static final String PAGENR = "Page number?";
	protected static final String PREV = "Prev";
	protected static final String SAVE = "Save";
	protected static final String VIEW = "View";

	public static final String TESTFILE = "test.xml";
	public static final String SAVEFILE = "dump.xml";

	public MenuController(Frame parent, Command openCommand, Command newCommand, Command saveCommand,
						  Command nextCommand, Command prevCommand, Command exitCommand,
						  IntFunction<Command> gotoCommandFactory) {
		this.parent = parent;
		this.openCommand = openCommand;
		this.newCommand = newCommand;
		this.saveCommand = saveCommand;
		this.nextCommand = nextCommand;
		this.prevCommand = prevCommand;
		this.exitCommand = exitCommand;
		this.gotoCommandFactory = gotoCommandFactory;

		MenuItem menuItem;
		Menu fileMenu = new Menu(FILE);

		fileMenu.add(menuItem = mkMenuItem(OPEN));
		menuItem.addActionListener(actionEvent -> openCommand.execute());

		fileMenu.add(menuItem = mkMenuItem(NEW));
		menuItem.addActionListener(actionEvent -> newCommand.execute());

		fileMenu.add(menuItem = mkMenuItem(SAVE));
		menuItem.addActionListener(actionEvent -> saveCommand.execute());

		fileMenu.addSeparator();

		fileMenu.add(menuItem = mkMenuItem(EXIT));
		menuItem.addActionListener(actionEvent -> exitCommand.execute());

		add(fileMenu);

		Menu viewMenu = new Menu(VIEW);

		viewMenu.add(menuItem = mkMenuItem(NEXT));
		menuItem.addActionListener(actionEvent -> nextCommand.execute());

		viewMenu.add(menuItem = mkMenuItem(PREV));
		menuItem.addActionListener(actionEvent -> prevCommand.execute());

		viewMenu.add(menuItem = mkMenuItem(GOTO));
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				String pageNumberStr = JOptionPane.showInputDialog((Object) PAGENR);
				if (pageNumberStr == null) return;
				try {
					int pageNumber = Integer.parseInt(pageNumberStr);
					gotoCommandFactory.apply(pageNumber - 1).execute();
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(parent, "\"" + pageNumberStr + "\" is not a valid page number",
							"Invalid input", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		add(viewMenu);

		Menu helpMenu = new Menu(HELP);
		helpMenu.add(menuItem = mkMenuItem(ABOUT));
		menuItem.addActionListener(actionEvent -> AboutBox.show(parent));
		setHelpMenu(helpMenu);
	}

	public MenuItem mkMenuItem(String name) {
		return new MenuItem(name, new MenuShortcut(name.charAt(0)));
	}
}
