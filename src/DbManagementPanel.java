package src;

import javax.swing.*;

public class DbManagementPanel extends JPanel {
    public DbManagementPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JButton reloadButton = new JButton("Reload");
        add(reloadButton);
        reloadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GUI.reload();
            }
        });

        JButton saveButton = new JButton("Save");
        add(saveButton);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GUI.dict.save();
            }
        });
    }
}
