package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import log.Logger;

public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final RobotModel robotModel = new RobotModel();
    private final RobotController robotController = new RobotController(robotModel);

    private LogWindow logWindow;
    private GameWindow gameWindow;
    private CoordinatesWindow coordinatesWindow;

    public MainApplicationFrame() {
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(
                inset,
                inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2
        );

        setContentPane(desktopPane);

        logWindow = createLogWindow();
        logWindow.setName("logWindow");
        addWindow(logWindow);

        gameWindow = new GameWindow(robotModel, robotController);
        gameWindow.setName("gameWindow");
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);

        coordinatesWindow = new CoordinatesWindow(robotModel);
        coordinatesWindow.setName("coordinatesWindow");
        coordinatesWindow.setLocation(420, 10);
        coordinatesWindow.setSize(220, 120);
        addWindow(coordinatesWindow);

        ConfigManager.loadState(new JInternalFrame[]{logWindow, gameWindow, coordinatesWindow});

        setJMenuBar(MenuCreater.create(this));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });
    }

    protected LogWindow createLogWindow() {
        LogWindow window = new LogWindow(Logger.getDefaultLogSource());
        window.setLocation(10, 10);
        window.setSize(300, 800);
        setMinimumSize(window.getSize());
        window.pack();
        Logger.debug("Протокол работает");
        return window;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    void exitApplication() {
        ConfigManager.saveState(new JInternalFrame[]{logWindow, gameWindow, coordinatesWindow});

        Object[] options = {"Нет", "Да"};
        JOptionPane pane = new JOptionPane(
                "Вы действительно хотите выйти?",
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION,
                null,
                options,
                options[0]
        );

        JDialog dialog = pane.createDialog(this, "Выход");
        dialog.setVisible(true);

        Object selectedValue = pane.getValue();
        if (selectedValue != null && selectedValue.toString().equals("Да")) {
            robotController.stop();
            System.exit(0);
        }
    }
}
