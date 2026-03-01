package gui;
import javax.swing.*;
import java.io.*;
import java.util.Properties;

public class ConfigManager {
    //имя файла
    private static final String CONFIG_FILE_NAME = ".robot_config.properties";

    //Ключи для свойств
    private static final String KEY_X = ".x";
    private static final String KEY_Y = ".y";
    private static final String KEY_W = ".width";
    private static final String KEY_H = ".height";
    private static final String KEY_MAX = ".isMaximized";

    public static String getConfigPath(){
        return System.getProperty("user.home") + File.separator + CONFIG_FILE_NAME;
    }

    public static void saveState(JInternalFrame[] frames) {

        Properties props = new Properties();
        String path = getConfigPath();

        try(FileOutputStream fos = new FileOutputStream(path)) {
           for (JInternalFrame frame : frames) {
               String name = frame.getName();
               if (name == null || name.isEmpty()) continue;

               props.setProperty(name + KEY_X, String.valueOf(frame.getX()));
               props.setProperty(name + KEY_Y, String.valueOf(frame.getY()));
               props.setProperty(name + KEY_W, String.valueOf(frame.getWidth()));
               props.setProperty(name + KEY_H, String.valueOf(frame.getHeight()));
               props.setProperty(name + KEY_MAX, String.valueOf(frame.isMaximum()));
           }

           props.store(fos, "Конфигурация состояния приложения");
            System.out.println("Состояние сохранено в: " + path);
        }catch (IOException e){ //выход программы не прерываем просто логируем ошибку
            System.err.println("Ошибка при сохранении конфигурации: " + e.getMessage());
        }

    }

    public static void loadState(JInternalFrame[] frames){
        Properties props = new Properties();
        String path = getConfigPath();
        File configFile = new File(path);

        if (!configFile.exists()) {
            System.out.println("Файл конфигурации не найден");
            return;
        }

        try(FileInputStream fis = new FileInputStream(configFile)){
            props.load(fis);

            for (JInternalFrame frame : frames) {
                String name = frame.getName();
                if (name == null || name.isEmpty()) continue;

                // Читаем значения, если они есть в файле
                String xStr = props.getProperty(name + KEY_X);
                String yStr = props.getProperty(name + KEY_Y);
                String wStr = props.getProperty(name + KEY_W);
                String hStr = props.getProperty(name + KEY_H);
                String maxStr = props.getProperty(name + KEY_MAX);

                if (xStr != null && yStr != null) {
                    int x = Integer.parseInt(xStr);
                    int y = Integer.parseInt(yStr);
                    int w = Integer.parseInt(wStr);
                    int h = Integer.parseInt(hStr);
                    boolean isMax = Boolean.parseBoolean(maxStr);

                    frame.setBounds(x, y, w, h);

                    if (isMax) {
                        try {
                            frame.setMaximum(true);
                        }catch (Exception e) {
                            System.err.println("Не удалось развернуть окно: " + e.getMessage());
                        }
                    }
                }
            }
            System.out.println("Состояние восстановлено из: " + path);
        }catch (IOException | NumberFormatException e){
            System.err.println("Ошибка при чтении конфигурации: " + e.getMessage());
        }
    }



}
