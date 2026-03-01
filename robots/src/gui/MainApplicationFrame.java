package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

import log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */
public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();
    private LogWindow logWindow;
    private GameWindow gameWindow;

    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width  - inset*2,
                screenSize.height - inset*2);

        setContentPane(desktopPane);


        logWindow = createLogWindow();
        logWindow.setName("logWindow");
        addWindow(logWindow);

        gameWindow = new GameWindow();
        gameWindow.setName("gameWindow");
        gameWindow.setSize(400,  400);
        addWindow(gameWindow);

        ConfigManager.loadState(new JInternalFrame[]{logWindow, gameWindow});

        setJMenuBar(MenuCreater.create(this));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                exitApplication();
            }
        });
    }

    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

//    protected JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
//
//        //Set up the lone menu.
//        JMenu menu = new JMenu("Document");
//        menu.setMnemonic(KeyEvent.VK_D);
//        menuBar.add(menu);
//
//        //Set up the first menu item.
//        JMenuItem menuItem = new JMenuItem("New");
//        menuItem.setMnemonic(KeyEvent.VK_N);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_N, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("new");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
//
//        //Set up the second menu item.
//        menuItem = new JMenuItem("Quit");
//        menuItem.setMnemonic(KeyEvent.VK_Q);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("quit");
    ////        menuItem.addActionListener(this);
//        menu.add(menuItem);
//
//        return menuBar;
//    }

//    private void exitApplication()
//    {
//        int result = JOptionPane.showConfirmDialog(
//                this, //where show(parent)
//                "Вы действительно хотите выйти?", //what ask
//                "Выход", //Window name
//                JOptionPane.YES_NO_OPTION, //key type
//                JOptionPane.QUESTION_MESSAGE // image type
//        );
//
//        if (result == JOptionPane.YES_NO_OPTION){
//            System.exit(0);
//        }
//
//    }
        //package private сознательно(Для MenuCreater)
        void exitApplication()
        {
            ConfigManager.saveState(new JInternalFrame[]{logWindow, gameWindow});

            Object[] options = {"Нет", "Да"};

            JOptionPane pane = new JOptionPane(
                    "Вы действительно хотите выйти?", //message
                    JOptionPane.QUESTION_MESSAGE, //icon type
                    JOptionPane.YES_NO_OPTION, //options
                    null, //icon
                    options, //custom name of button
                    options[0] //default assignment
            );

            JDialog dialog = pane.createDialog(this, "Выход");
            dialog.setVisible(true);

            Object selectedValue = pane.getValue();

            if (selectedValue != null && selectedValue.toString().equals("Да")){
                System.exit(0);
            }
        }
}
