package gui;

import java.util.Observable;

public class RobotModel extends Observable {
    public static final int ROBOT_WIDTH = 30;
    public static final int ROBOT_HEIGHT = 10;

    private static final double MAX_LINEAR_SPEED = 0.12;
    private static final double POSITION_EPSILON = 0.001;
    private static final double ROBOT_RADIUS = ROBOT_WIDTH / 2.0;

    private double robotPositionX = 100;
    private double robotPositionY = 100;
    private double robotDirection = 0;

    private double targetPositionX = 150;
    private double targetPositionY = 100;

    private int fieldWidth;
    private int fieldHeight;

    public synchronized double getRobotPositionX() {
        return robotPositionX;
    }

    public synchronized double getRobotPositionY() {
        return robotPositionY;
    }

    public synchronized double getRobotDirection() {
        return robotDirection;
    }

    public synchronized int getTargetPositionX() {
        return round(targetPositionX);
    }

    public synchronized int getTargetPositionY() {
        return round(targetPositionY);
    }

    public synchronized void setTargetPosition(int x, int y) {
        double clampedX = clampX(x);
        double clampedY = clampY(y);
        if (isSame(targetPositionX, clampedX) && isSame(targetPositionY, clampedY)) {
            return;
        }

        targetPositionX = clampedX;
        targetPositionY = clampedY;
        notifyModelChanged();
    }

    public synchronized void setFieldSize(int width, int height) {
        int normalizedWidth = Math.max(0, width);
        int normalizedHeight = Math.max(0, height);

        double clampedRobotX = clampToField(robotPositionX, normalizedWidth);
        double clampedRobotY = clampToField(robotPositionY, normalizedHeight);
        double clampedTargetX = clampToField(targetPositionX, normalizedWidth);
        double clampedTargetY = clampToField(targetPositionY, normalizedHeight);

        boolean changed = fieldWidth != normalizedWidth
                || fieldHeight != normalizedHeight
                || !isSame(robotPositionX, clampedRobotX)
                || !isSame(robotPositionY, clampedRobotY)
                || !isSame(targetPositionX, clampedTargetX)
                || !isSame(targetPositionY, clampedTargetY);

        fieldWidth = normalizedWidth;
        fieldHeight = normalizedHeight;
        robotPositionX = clampedRobotX;
        robotPositionY = clampedRobotY;
        targetPositionX = clampedTargetX;
        targetPositionY = clampedTargetY;

        if (changed) {
            notifyModelChanged();
        }
    }

    public synchronized void update(double duration) {
        if (fieldWidth <= 0 || fieldHeight <= 0 || isTargetReached()) {
            return;
        }

        double maxStep = MAX_LINEAR_SPEED * duration;
        double deltaX = targetPositionX - robotPositionX;
        double deltaY = targetPositionY - robotPositionY;

        double stepX = calculateAxisStep(deltaX, maxStep);
        double stepY = calculateAxisStep(deltaY, maxStep);

        double newX = clampX(robotPositionX + stepX);
        double newY = clampY(robotPositionY + stepY);
        double actualStepX = newX - robotPositionX;
        double actualStepY = newY - robotPositionY;

        robotPositionX = newX;
        robotPositionY = newY;

        if (Math.abs(actualStepX) > POSITION_EPSILON || Math.abs(actualStepY) > POSITION_EPSILON) {
            robotDirection = angleTo(0, 0, actualStepX, actualStepY);
        }

        if (isTargetReached()) {
            robotPositionX = targetPositionX;
            robotPositionY = targetPositionY;
        }
        notifyModelChanged();
    }

    private void notifyModelChanged() {
        setChanged();
        notifyObservers();
    }

    private boolean isTargetReached() {
        return isSame(robotPositionX, targetPositionX) && isSame(robotPositionY, targetPositionY);
    }

    private double clampX(double value) {
        return clampToField(value, fieldWidth);
    }

    private double clampY(double value) {
        return clampToField(value, fieldHeight);
    }

    private static double clampToField(double value, int fieldSize) {
        if (fieldSize <= 0) {
            return value;
        }

        double center = fieldSize / 2.0;
        double min = Math.min(ROBOT_RADIUS, center);
        double max = Math.max(fieldSize - ROBOT_RADIUS, center);
        return applyLimits(value, min, max);
    }

    private static boolean isSame(double first, double second) {
        return Math.abs(first - second) <= POSITION_EPSILON;
    }

    private static double calculateAxisStep(double delta, double maxStep) {
        if (Math.abs(delta) <= maxStep) {
            return delta;
        }
        return Math.signum(delta) * maxStep;
    }

    private static int round(double value) {
        return (int) (value + 0.5);
    }

    private static double applyLimits(double value, double min, double max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    private static double asNormalizedRadians(double angle) {
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;
        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }
}
