package src;

import javax.swing.*;

public class DbManagementPanel extends JPanel {
    public DbManagementPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JButton reloadButton = new JButton("Reload");
        add(reloadButton);
        reloadButton.addActionListener(evt -> GUI.reload());

        JButton saveButton = new JButton("Save");
        add(saveButton);
        saveButton.addActionListener(evt -> GUI.dict.save());
    }
}
