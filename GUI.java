import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class GUI {

    public static Dictionary dict;
    static JFrame dictionaryFrame;
    static JSplitPane splitPane;

    static GuiDictionaryPanel guiDictionaryPanel;

    static JPanel detailsFrame;
    static JFrame editorFrame;

    public static JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JToggleButton tbMenuItem = new JToggleButton("Dictionary");
        tbMenuItem.setModel(new DefaultButtonModel());
        tbMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMainFrame(splitPane);
            }
        });
        menuBar.add(tbMenuItem);

        JToggleButton tbMenuItem2 = new JToggleButton("Quiz");
        tbMenuItem2.setModel(new DefaultButtonModel());
        tbMenuItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMainFrame(quiz(true));
            }
        });
        menuBar.add(tbMenuItem2);

        JToggleButton tbMenuItem3 = new JToggleButton("Manage");
        tbMenuItem3.setModel(new DefaultButtonModel());
        tbMenuItem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMainFrame(dictManager());
            }
        });
        menuBar.add(tbMenuItem3);

        return menuBar;
    }

    public static JPanel dictManager() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JButton addButton = new JButton("Reload");
        panel.add(addButton);
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dict = new Dictionary("slang.txt");
            }
        });

        return panel;

    }

    public static void updateMainFrame(Container panel) {
//        dictionaryFrame.removeAll();
        dictionaryFrame.setContentPane(panel);
        dictionaryFrame.repaint();
        dictionaryFrame.revalidate();
    }

    private static void createAndShowGUI() {
        new GUI();

        JFrame.setDefaultLookAndFeelDecorated(true);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, guiDictionaryPanel, detailsFrame);
        splitPane.setDividerLocation(400);
        splitPane.setResizeWeight(0.5);

        dictionaryFrame = new JFrame("SlangDict");
        dictionaryFrame.setJMenuBar(createMenuBar());
        dictionaryFrame.setContentPane(motd());
        dictionaryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        dictionaryFrame.setSize(800, 600);
        dictionaryFrame.setMinimumSize(new Dimension(640, 360));
        dictionaryFrame.setVisible(true);

        motd();
        quiz(true);


    }

//    private static JSplitPane searchPanel() {
////        JSplitPane frame = new JSplitPane("BoxLayoutDemo");
//        JPanel frame = new JPanel();
//        frame.setLayout(new BorderLayout());
//
//        JPanel topPanel = new JPanel();
//        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
//        JTextField textField = new JTextField();
//        textField.setColumns(20);
//        textField.setMinimumSize(new Dimension(100, 20));
//        JButton addButton = new JButton("➕");
//        JComboBox<String> comboBox = new JComboBox<>(new String[]{"Word", "Definition"});
//        topPanel.add(textField);
//        topPanel.add(comboBox);
//        topPanel.add(addButton);
//        topPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        frame.add(topPanel, BorderLayout.PAGE_START);
//
//        JPanel centrePanel = new JPanel();
//        centrePanel.setLayout(new BoxLayout(centrePanel, BoxLayout.Y_AXIS));
//
//        DefaultListModel<TrieNode> listModel = new DefaultListModel<>();
//        listModel.addAll(dict.getFromPrefix(""));
//        JList<TrieNode> list = new JList<>(listModel);
//        list.setCellRenderer(new ListRenderer());
//        JScrollPane scrollPane = new JScrollPane(list);
//        centrePanel.add(scrollPane);
//        frame.add(centrePanel, BorderLayout.CENTER);
//
//        JPanel buttonPane = new JPanel();
//        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
//        buttonPane.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        frame.add(buttonPane, BorderLayout.PAGE_END);
//        frame.revalidate();
//        frame.repaint();
//
//        JPanel historyPane = new JPanel();
//        historyPane.setLayout(new BoxLayout(historyPane, BoxLayout.Y_AXIS));
//
//        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, frame, historyPane);
//        splitPane.setResizeWeight(0.5);
//
//        addButton.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                TrieNode node = new TrieNode();
//                JFrame editorFrame = editorFrame(node);
//                editorFrame.repaint();
//                editorFrame.pack();
//                editorFrame.setVisible(true);
//            }
//        });
//
//        comboBox.addItemListener(new java.awt.event.ItemListener() {
//            public void itemStateChanged(java.awt.event.ItemEvent evt) {
//                listModel.clear();
//                listModel.addAll(List.of(getSearchResult(comboBox, textField)));
//            }
//        });
//
//        textField.addKeyListener(new java.awt.event.KeyAdapter() {
//            public void keyReleased(java.awt.event.KeyEvent evt) {
//                listModel.clear();
//                listModel.addAll(List.of(getSearchResult(comboBox, textField)));
//
//            }
//        });
//        list.addMouseListener(new java.awt.event.MouseAdapter() {
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                super.mouseReleased(e);
//                TrieNode node = list.getSelectedValue();
//                if (node == null) {
//                    return;
//                } else if (!node.isEndOfWord) {
//                    ((DefaultListModel<TrieNode>) list.getModel()).removeElement(node);
//                    return;
//                }
//                setDetailsFrame(node);
//                if (!dict.history.isEmpty() && dict.history.getFirst() == node) {
//                    return;
//                } else {
//                    dict.history.addFirst(node);
//                }
//
//                historyPane.removeAll();
//                JList<TrieNode> histList = new JList<>(dict.history.toArray(new TrieNode[0]));
//                JScrollPane histScrollPane = new JScrollPane(histList);
//                historyPane.add(histScrollPane);
//                historyPane.revalidate();
//                historyPane.repaint();
//
//                splitPane.setDividerLocation(splitPane.getDividerLocation());
//
//            }
//
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                if (evt.getClickCount() == 2) {
//                    TrieNode node = list.getSelectedValue();
//                    if (!node.isEndOfWord) {
//                        return;
//                    }
//                    editorFrame = editorFrame(node);
//                    editorFrame.revalidate();
//                    editorFrame.repaint();
//                    editorFrame.pack();
//                    editorFrame.setVisible(true);
//                }
//            }
//        });
//        return splitPane;
//    }

    public static JPanel setDetailsFrame(TrieNode node) {
        return GuiDictionaryPanel.setDetailsFrame(node);
//        if (detailsFrame == null) {
//            detailsFrame = new JPanel();
//        } else {
//            detailsFrame.removeAll();
//        }
//        if(node == null) {
//            splitPane.setRightComponent(detailsFrame);
//            splitPane.setDividerLocation(splitPane.getDividerLocation());
//            splitPane.repaint();
//            splitPane.revalidate();
//            return detailsFrame;
//        }
//
//        JPanel wrapperPanel = new JPanel();
//        wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));
//
//        JLabel wordLabel = new JLabel(node.word);
//        wordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        wrapperPanel.add(wordLabel);
//
//        JPanel definitionPanel = new JPanel();
//        definitionPanel.setLayout(new BoxLayout(definitionPanel, BoxLayout.Y_AXIS));
//        for (String definition : node.definitions) {
//            JLabel definitionLabel = new JLabel(definition);
//            definitionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//            definitionPanel.add(definitionLabel);
//        }
//        wrapperPanel.add(definitionPanel);
//
//        detailsFrame.add(wrapperPanel);
//        detailsFrame.repaint();
//        detailsFrame.revalidate();
//
//        splitPane.setRightComponent(detailsFrame);
//        splitPane.setDividerLocation(splitPane.getDividerLocation());
//        splitPane.repaint();
//        splitPane.revalidate();
//
//        return wrapperPanel;
    }

//    public static TrieNode[] getSearchResult(JComboBox<String> comboBox, JTextField textField) {
//        TrieNode[] nodes;
//        if (Objects.requireNonNull(comboBox.getSelectedItem()).toString().equals("Word")) {
//            nodes = dict.getFromPrefix((textField.getText())).toArray(new TrieNode[0]);
//        } else {
//            nodes = dict.getFromDefinition((textField.getText())).toArray(new TrieNode[0]);
//        }
//        return nodes;
//    }

//    public static JFrame editorFrame(TrieNode node) {
//        JFrame frame = new JFrame("Editor");
//
//        JPanel wrapperPanel = new JPanel();
//        wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));
//
//        JPanel topPanel = new JPanel();
//        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
//
//        JTextField wordField = new JTextField();
//        JScrollPane scrollPane = new JScrollPane();
//        scrollPane.setMinimumSize(new Dimension(400, 200));
//
//        DefaultTableModel model = new DefaultTableModel(new Object[]{"Definition", ""}, 0);
//        JTable definitionField = new JTable(model);
//        definitionField.setFillsViewportHeight(true);
//
//        topPanel.add(wordField);
//        topPanel.add(scrollPane);
//        wrapperPanel.add(topPanel);
//
//        JPanel bottomPanel = new JPanel();
//        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
//        JPanel bottomRightPanel = new JPanel();
//        bottomRightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
//        JPanel bottomLeftPanel = new JPanel();
//        bottomLeftPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
//
//        JButton saveButton = new JButton("Save");
//        JButton deleteButton = new JButton("Delete");
//        JButton cancelButton2 = new JButton("Cancel");
//        JButton addButton = new JButton("Add definition row");
//
//        bottomLeftPanel.add(addButton);
//
//        bottomRightPanel.add(saveButton);
//        bottomRightPanel.add(deleteButton);
//        bottomRightPanel.add(cancelButton2);
//
//        bottomPanel.add(bottomLeftPanel);
//        bottomPanel.add(bottomRightPanel);
//
//        wrapperPanel.add(bottomPanel);
//
//        scrollPane.setViewportView(definitionField);
//
//        wordField.setText(node.word);
//        for (String definition : node.definitions) {
//            model.addRow(new Object[]{definition, "-"});
//        }
//
//
//        // implements the interface https://tips4java.wordpress.com/2009/07/12/table-button-column/
//        // or just override the default thing
//        definitionField.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
//            @Override
//            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//                if (column == 1) {
//                    return new JButton("✘");
//                }
//                return c;
//            }
//        });
//
//        definitionField.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()) {
//            @Override
//            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//                if (column == 1) {
//                    JButton button = new JButton("✘");
//                    button.addActionListener(new java.awt.event.ActionListener() {
//                        public void actionPerformed(java.awt.event.ActionEvent evt) {
//                            // https://stackoverflow.com/a/34158450
//                            definitionField.getCellEditor().stopCellEditing();
//                            model.removeRow(row);
//                        }
//                    });
//                    return button;
//                } else {
//                    return super.getTableCellEditorComponent(table, value, isSelected, row, column);
//                }
//            }
//        });
//
//        wordField.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                TrieNode existing = dict.get(wordField.getText());
//                if (existing == null) {
//                    return;
//                }
//                model.setRowCount(0);
//                for (String definition : existing.definitions) {
//                    model.addRow(new Object[]{definition});
//                }
//
//            }
//        });
//
//        saveButton.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                try {
//                    definitionField.getCellEditor().stopCellEditing();
//                } catch (NullPointerException e) {
//                    // do nothing
//                }
//                String word = wordField.getText();
//                if (word.isBlank()) {
//                    JOptionPane.showMessageDialog(null, "Word cannot be blank");
//                    return;
//                }
//                ArrayList<String> definitions = new ArrayList<>();
//                for (int i = 0; i < definitionField.getRowCount(); i++) {
//                    definitions.add(definitionField.getValueAt(i, 0).toString().trim());
//                    if (definitions.getLast() == null || definitions.getLast().isBlank()) {
//                        definitions.removeLast();
//                    }
//                }
//                if (definitions.isEmpty()) {
//                    JOptionPane.showMessageDialog(null, "Definition cannot be blank");
//                    return;
//                }
//                int merge = 0;
//                TrieNode dictNode = dict.get(word);
//                if (dictNode == node) { // if the word is not changed, changes are made only to the definition
//                    node.definitions.clear();
//                    node.definitions = new ArrayList<>();
//                    for (int i = 0; i < definitionField.getRowCount(); i++) {
//                        node.definitions.add(definitionField.getValueAt(i, 0).toString().trim());
//                        if (node.definitions.getLast() == null || node.definitions.getLast().isBlank()) {
//                            node.definitions.removeLast();
//                        }
//                    }
//                    return;
//                }
//                if (dictNode != null && dictNode.isEndOfWord) {
//                    Object[] options = {"Append", "Overwrite", "Cancel"};
//                    merge = JOptionPane.showOptionDialog(null, "Word already exists, do you want to overwrite or append the definition?", "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
//                    if (merge == 2) {
//                        return;
//                    }
//                }
//                dict.edit(node, wordField.getText().toUpperCase(), definitions, merge == 0);
//                JOptionPane.showMessageDialog(null, "Word saved");
//                guiDictionaryPanel.updateList();
//            }
//        });
//
//        deleteButton.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this word?", "Warning", JOptionPane.YES_NO_OPTION);
//                if (result != JOptionPane.YES_OPTION) {
//                    return;
//                }
//                dict.delete(node);
//                JOptionPane.showMessageDialog(null, "Word deleted");
//                editorFrame.setVisible(false);
//                guiDictionaryPanel.updateList();
//            }
//        });
//
//        cancelButton2.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                editorFrame.setVisible(false);
//            }
//        });
//
//        addButton.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                model.addRow(new Object[]{""});
//            }
//        });
//
//
////        wrapperPanel.add(wordField);
////        wrapperPanel.add(scrollPane);
////
////        wrapperPanel.add(saveButton);
////        wrapperPanel.add(deleteButton);
////        wrapperPanel.add(cancelButton2);
////        wrapperPanel.add(addButton);
//        wrapperPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//
//        frame.add(wrapperPanel);
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        frame.pack();
//
//        return frame;
//    }

    private static JPanel quiz(boolean word) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Quiz");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);

        JPanel choosePanel = new JPanel();
        choosePanel.setLayout(new BoxLayout(choosePanel, BoxLayout.X_AXIS));
        JButton wordButton = new JButton("Word");
        JButton definitionButton = new JButton("Definition");
        choosePanel.add(wordButton);
        choosePanel.add(definitionButton);
        panel.add(choosePanel);

        wordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                panel.removeAll();
                panel.add(quiz(true));
                panel.revalidate();
                panel.repaint();
            }
        });

        definitionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                panel.removeAll();
                panel.add(quiz(false));
                panel.revalidate();
                panel.repaint();
            }
        });
        // ^i know this is bad but i don't have the energy to :pensive:

        JPanel questionPanel = questionPanel(word);
        panel.add(questionPanel);

        JPanel bottomPanel = new JPanel();
        JButton nextButton = new JButton("Next");
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                questionPanel.removeAll();
                questionPanel.add(questionPanel(word));
                questionPanel.revalidate();
                questionPanel.repaint();
            }
        });
        bottomPanel.add(nextButton);
        panel.add(bottomPanel);
        return panel;
    }

    private static JPanel questionPanel(boolean word) {
        JPanel questionPanel = new JPanel();

        List<TrieNode> nodes = dict.quiz();
        TrieNode ans = nodes.get(new Random().nextInt(nodes.size()));

        JLabel question;
        if (word) {
            question = new JLabel(ans.definitions.get(0));
        } else {
            question = new JLabel(ans.word);
        }
        question.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionPanel.add(question);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 2));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        for (TrieNode node : nodes) {
            JButton button;
            if (word) {
                button = new JButton(node.word);
            } else {
                button = new JButton(node.definitions.get(0));
            }
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setPreferredSize(new Dimension(400, 100));

            button.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    JButton clickedButton = (JButton) evt.getSource();
                    if (node == ans) {
                        clickedButton.setBackground(java.awt.Color.GREEN);
                        for (Component iiterButton : buttonPanel.getComponents()) {
                            iiterButton.setEnabled(false);
                        }
                    } else {
                        clickedButton.setBackground(java.awt.Color.RED);
                    }
                    clickedButton.setEnabled(false);
                }
            });
            buttonPanel.add(button);
        }

        JPanel wrapperPanel = new JPanel();
        wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));
        wrapperPanel.add(questionPanel);
        wrapperPanel.add(buttonPanel);
        return wrapperPanel;
    }

    private static JPanel motd() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel welcomeText = new JLabel("Welcome to SlangDict");
        welcomeText.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeText.setFont(new Font("Serif", Font.BOLD, 20));
        panel.add(welcomeText);

        panel.add(setDetailsFrame(dict.motd()));

        return panel;
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            dict = new Dictionary("slang.txt");
            guiDictionaryPanel = new GuiDictionaryPanel();
            createAndShowGUI();
        });
    }
}
