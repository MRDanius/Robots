package gui;

import java.awt.Point;

import javax.swing.Timer;

public class RobotController {
    private static final int MODEL_UPDATE_PERIOD_MS = 10;

    private final RobotModel model;
    private final Timer updateTimer;

    public RobotController(RobotModel model) {
        this.model = model;
        updateTimer = new Timer(MODEL_UPDATE_PERIOD_MS, e -> model.update(MODEL_UPDATE_PERIOD_MS));
        updateTimer.start();
    }

    public void setTargetPosition(Point point) {
        model.setTargetPosition(point.x, point.y);
    }

    public void updateFieldSize(int width, int height) {
        model.setFieldSize(width, height);
    }

    public void stop() {
        updateTimer.stop();
    }
}
