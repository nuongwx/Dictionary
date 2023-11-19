package src;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class EditorFrame extends JFrame {
    public EditorFrame(TrieNode node) {
        setTitle("Editor");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JTextField wordField = new JTextField();
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setMinimumSize(new Dimension(400, 200));

        DefaultTableModel model = new DefaultTableModel(new Object[]{"Definition", ""}, 0);
        JTable definitionField = new JTable(model);
        definitionField.setFillsViewportHeight(true);

        topPanel.add(wordField);
        topPanel.add(scrollPane);
        add(topPanel);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

        JPanel bottomRightPanel = new JPanel();
        bottomRightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JPanel bottomLeftPanel = new JPanel();
        bottomLeftPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton saveButton = new JButton("Save");
        JButton deleteButton = new JButton("Delete");
        JButton cancelButton2 = new JButton("Exit");
        JButton addButton = new JButton("Add definition row");

        bottomLeftPanel.add(addButton);

        bottomRightPanel.add(saveButton);
        bottomRightPanel.add(deleteButton);
        bottomRightPanel.add(cancelButton2);

        bottomPanel.add(bottomLeftPanel);
        bottomPanel.add(bottomRightPanel);

        add(bottomPanel);

        scrollPane.setViewportView(definitionField);

        wordField.setText(node.word);
        for (String definition : node.definitions) {
            model.addRow(new Object[]{definition, "-"});
        }

        // implements the interface https://tips4java.wordpress.com/2009/07/12/table-button-column/
        // or just override the default thing
        definitionField.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 1) {
                    return new JButton("✘");
                }
                return c;
            }
        });

        definitionField.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                if (column == 1) {
                    JButton button = new JButton("✘");
                    button.addActionListener(evt -> {
                        // https://stackoverflow.com/a/34158450
                        definitionField.getCellEditor().stopCellEditing();
                        model.removeRow(row);
                    });
                    return button;
                } else {
                    return super.getTableCellEditorComponent(table, value, isSelected, row, column);
                }
            }
        });
        wordField.addActionListener(evt -> {
            TrieNode existing = GUI.dict.get(wordField.getText());
            if (existing == null) {
                return;
            }
            model.setRowCount(0);
            for (String definition : existing.definitions) {
                model.addRow(new Object[]{definition});
            }

        });
        wordField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                event();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                event();
            }

            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                event();
            }

            public void event() {
                TrieNode existing = GUI.dict.get(wordField.getText());
                if (existing == null) {
                    return;
                }
                model.setRowCount(0);
                for (String definition : existing.definitions) {
                    model.addRow(new Object[]{definition});
                }
            }
        });
        saveButton.addActionListener(evt -> {
            try {
                definitionField.getCellEditor().stopCellEditing();
            } catch (NullPointerException e) {
                // do nothing
            }
            String word = wordField.getText();
            if (word.isBlank()) {
                JOptionPane.showMessageDialog(null, "Word cannot be blank");
                return;
            }
            ArrayList<String> definitions = new ArrayList<>();
            for (int i = 0; i < definitionField.getRowCount(); i++) {
                definitions.add(definitionField.getValueAt(i, 0).toString().trim());
                if (definitions.getLast() == null || definitions.getLast().isBlank()) {
                    definitions.removeLast();
                }
            }
            if (definitions.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Definition cannot be blank");
                return;
            }
//            TrieNode dictNode = GUI.dict.get(word);
            GUI.dict.edit(node, wordField.getText().toUpperCase(), definitions);
            GUI.dict.save();
        });
        deleteButton.addActionListener(evt -> {
            int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this word?", "Warning", JOptionPane.YES_NO_OPTION);
            if (result != JOptionPane.YES_OPTION) {
                return;
            }
            GUI.dict.delete(node);
            GUI.dict.save();
            JOptionPane.showMessageDialog(null, "Word deleted");
            dispose();
//                setDetailsFrame(null);

        });
        cancelButton2.addActionListener(evt -> dispose());
        addButton.addActionListener(evt -> model.addRow(new Object[]{""}));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        revalidate();
        repaint();
        pack();
        setVisible(true);
    }
}
