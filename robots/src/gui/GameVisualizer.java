package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

public class GameVisualizer extends JPanel implements Observer {
    private final RobotModel model;
    private final RobotController controller;

    public GameVisualizer(RobotModel model, RobotController controller) {
        this.model = model;
        this.controller = controller;
        this.model.addObserver(this);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setTargetPosition(e.getPoint());
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                synchronizeFieldSize();
            }

            @Override
            public void componentShown(ComponentEvent e) {
                synchronizeFieldSize();
            }
        });

        setDoubleBuffered(true);
        EventQueue.invokeLater(this::synchronizeFieldSize);
    }

    protected void setTargetPosition(Point point) {
        controller.setTargetPosition(point);
    }

    protected void synchronizeFieldSize() {
        controller.updateFieldSize(getWidth(), getHeight());
    }

    private static int round(double value) {
        return (int) (value + 0.5);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        double robotX = model.getRobotPositionX();
        double robotY = model.getRobotPositionY();
        double direction = model.getRobotDirection();
        int targetX = model.getTargetPositionX();
        int targetY = model.getTargetPositionY();

        Graphics2D g2d = (Graphics2D) g;
        drawRobot(g2d, round(robotX), round(robotY), direction);
        drawTarget(g2d, targetX, targetY);
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int width, int height) {
        g.fillOval(centerX - width / 2, centerY - height / 2, width, height);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int width, int height) {
        g.drawOval(centerX - width / 2, centerY - height / 2, width, height);
    }

    private void drawRobot(Graphics2D g, int x, int y, double direction) {
        AffineTransform originalTransform = g.getTransform();
        AffineTransform rotatedTransform = new AffineTransform(originalTransform);
        rotatedTransform.rotate(direction, x, y);
        g.setTransform(rotatedTransform);

        g.setColor(Color.MAGENTA);
        fillOval(g, x, y, RobotModel.ROBOT_WIDTH, RobotModel.ROBOT_HEIGHT);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, RobotModel.ROBOT_WIDTH, RobotModel.ROBOT_HEIGHT);
        g.setColor(Color.WHITE);
        fillOval(g, x + 10, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x + 10, y, 5, 5);

        g.setTransform(originalTransform);
    }

    private void drawTarget(Graphics2D g, int x, int y) {
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }

    @Override
    public void update(Observable o, Object arg) {
        EventQueue.invokeLater(this::repaint);
    }
}
