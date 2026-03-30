package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class CoordinatesWindow extends JInternalFrame implements Observer {
    private final RobotModel model;
    private final JLabel xValueLabel = new JLabel();
    private final JLabel yValueLabel = new JLabel();

    public CoordinatesWindow(RobotModel model) {
        super("Координаты робота", true, true, true, true);
        this.model = model;
        this.model.addObserver(this);

        JPanel panel = new JPanel(new GridLayout(2, 2, 8, 8));
        panel.add(new JLabel("X:"));
        panel.add(xValueLabel);
        panel.add(new JLabel("Y:"));
        panel.add(yValueLabel);

        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.add(panel, BorderLayout.NORTH);

        getContentPane().add(rootPanel);
        updateLabels();
        pack();
    }

    private void updateLabels() {
        xValueLabel.setText(String.format("%.2f", model.getRobotPositionX()));
        yValueLabel.setText(String.format("%.2f", model.getRobotPositionY()));
    }

    @Override
    public void update(Observable o, Object arg) {
        SwingUtilities.invokeLater(this::updateLabels);
    }
}
