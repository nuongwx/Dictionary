import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class GuiEditorFrame extends JFrame {
    static Dictionary dict = GUI.dict;

    public static void show(TrieNode node) {
        JFrame frame = new JFrame("Editor");

        JPanel wrapperPanel = new JPanel();
        wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));

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
        wrapperPanel.add(topPanel);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        JPanel bottomRightPanel = new JPanel();
        bottomRightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JPanel bottomLeftPanel = new JPanel();
        bottomLeftPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton saveButton = new JButton("Save");
        JButton deleteButton = new JButton("Delete");
        JButton cancelButton2 = new JButton("Cancel");
        JButton addButton = new JButton("Add definition row");

        bottomLeftPanel.add(addButton);

        bottomRightPanel.add(saveButton);
        bottomRightPanel.add(deleteButton);
        bottomRightPanel.add(cancelButton2);

        bottomPanel.add(bottomLeftPanel);
        bottomPanel.add(bottomRightPanel);

        wrapperPanel.add(bottomPanel);

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
                    button.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            // https://stackoverflow.com/a/34158450
                            definitionField.getCellEditor().stopCellEditing();
                            model.removeRow(row);
                        }
                    });
                    return button;
                } else {
                    return super.getTableCellEditorComponent(table, value, isSelected, row, column);
                }
            }
        });

        wordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TrieNode existing = dict.get(wordField.getText());
                if (existing == null) {
                    return;
                }
                model.setRowCount(0);
                for (String definition : existing.definitions) {
                    model.addRow(new Object[]{definition});
                }

            }
        });

        //https://stackoverflow.com/a/29743335
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
                TrieNode existing = dict.get(wordField.getText());
                if (existing == null) {
                    return;
                }
                model.setRowCount(0);
                for (String definition : existing.definitions) {
                    model.addRow(new Object[]{definition});
                }
            }
        });

        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
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
                int merge = 0;
                TrieNode dictNode = dict.get(word);
                if (dictNode == node) { // if the word is not changed, changes are made only to the definition
                    node.definitions.clear();
                    node.definitions = new ArrayList<>();
                    for (int i = 0; i < definitionField.getRowCount(); i++) {
                        node.definitions.add(definitionField.getValueAt(i, 0).toString().trim());
                        if (node.definitions.getLast() == null || node.definitions.getLast().isBlank()) {
                            node.definitions.removeLast();
                        }
                    }
                    return;
                }
                if (dictNode != null && dictNode.isEndOfWord) {
                    Object[] options = {"Append", "Overwrite", "Cancel"};
                    merge = JOptionPane.showOptionDialog(null, "Word already exists, do you want to overwrite or append the definition?", "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                    if (merge == 2) {
                        return;
                    }
                }
                dict.edit(node, wordField.getText().toUpperCase(), definitions, merge == 0);
                JOptionPane.showMessageDialog(null, "Word saved");
                GuiDictionaryPanel.updateList();
            }
        });

        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this word?", "Warning", JOptionPane.YES_NO_OPTION);
                if (result != JOptionPane.YES_OPTION) {
                    return;
                }
                dict.delete(node);
                JOptionPane.showMessageDialog(null, "Word deleted");
                frame.setVisible(false);
                GuiDictionaryPanel.updateList();
            }
        });

        cancelButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                frame.setVisible(false);
            }
        });

        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                model.addRow(new Object[]{""});
            }
        });

        wrapperPanel.setAlignmentX(Component.CENTER_ALIGNMENT);


        frame.add(wrapperPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.revalidate();
        frame.pack();
        frame.setVisible(true);
    }
}
