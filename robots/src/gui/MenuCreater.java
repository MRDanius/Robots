package gui;
import java.awt.event.KeyEvent;
import javax.swing.*;
import log.Logger;


public class MenuCreater {
    public static JMenuBar create(MainApplicationFrame owner) {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(createLookAndFeelMenu(owner));
        menuBar.add(createTestMenu());
        menuBar.add(createFileMenu(owner));

        return menuBar;
    }

    private static JMenu createLookAndFeelMenu(JFrame owner) {
        JMenu menu = new JMenu("Режим отображения");
        menu.setMnemonic(KeyEvent.VK_V);

        JMenuItem systemItem = new JMenuItem("Системная схема", KeyEvent.VK_S);
        systemItem.addActionListener(e -> {
            try{
                UIManager.setLookAndFeel((UIManager.getSystemLookAndFeelClassName()));
                SwingUtilities.updateComponentTreeUI(owner);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        menu.add(systemItem);

        JMenuItem crossItem = new JMenuItem("Универсальная схема", KeyEvent.VK_U);
        crossItem.addActionListener(e -> {
            try{
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                SwingUtilities.updateComponentTreeUI(owner);
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        menu.add(crossItem);

        return menu;
    }

    private static JMenu createTestMenu() {
        JMenu menu = new JMenu("Тесты");
        menu.setMnemonic(KeyEvent.VK_T);

        JMenuItem logItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
        logItem.addActionListener(e -> Logger.debug("Новая строка"));
        menu.add(logItem);

        return menu;
    }

    private static JMenu createFileMenu(MainApplicationFrame owner){
        JMenu menu = new JMenu("Файл");
        menu.setMnemonic(KeyEvent.VK_F);

        JMenuItem exitItem = new JMenuItem("Выход", KeyEvent.VK_X);
        exitItem.addActionListener(e -> owner.exitApplication());
        menu.add(exitItem);

        return menu;
    }
}
