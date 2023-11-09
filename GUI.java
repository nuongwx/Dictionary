import javax.swing.*;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class GUI {

    public static Dictionary dict;
    static JFrame wordSearchFrame;
    static JFrame dictionaryFrame;
    static JSplitPane splitPane;

    static JPanel detailsFrame;
    static JFrame editorFrame;

    static JFrame historyFrame;

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

        return menuBar;
    }

    public static void updateMainFrame(Container panel) {
//        dictionaryFrame.removeAll();
        dictionaryFrame.setContentPane(panel);
        dictionaryFrame.repaint();
        dictionaryFrame.revalidate();
    }

    private static void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
//        wordSearchFrame = searchPanel();
//        editorFrame = new JFrame("Editor");
//        historyFrame = new JFrame("History");
//        wordSearchFrame.setVisible(true);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, searchPanel(), detailsFrame);
        splitPane.setDividerLocation(400);

        dictionaryFrame = new JFrame("SlangDict");
        dictionaryFrame.setJMenuBar(createMenuBar());
        dictionaryFrame.setContentPane(motd());
        dictionaryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dictionaryFrame.setSize(800, 600);
        dictionaryFrame.setVisible(true);

        motd();
        quiz(true);


    }

    public static void showHistory() {
        historyFrame = new JFrame("History");

        JPanel wrapperPanel = new JPanel();
        wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));
        JList<TrieNode> list = new JList<>(dict.history.toArray(new TrieNode[0]));

        JScrollPane scrollPane = new JScrollPane(list);
        wrapperPanel.add(scrollPane);

        historyFrame.repaint();
        historyFrame.add(wrapperPanel);
        historyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        historyFrame.pack();
        historyFrame.setVisible(true);
    }

    private static JPanel searchPanel() {
//        JSplitPane frame = new JSplitPane("BoxLayoutDemo");
        JPanel frame = new JPanel();
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        JTextField textField = new JTextField();
        textField.setColumns(20);
        JButton historyButton = new JButton("History");
        JComboBox comboBox = new JComboBox(new String[]{"Word", "Definition"});
        topPanel.add(textField);
        topPanel.add(comboBox);
        topPanel.add(historyButton);
        topPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        frame.add(topPanel, BorderLayout.PAGE_START);

        historyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showHistory();
            }
        });

        JPanel centrePanel = new JPanel();
        centrePanel.setLayout(new BoxLayout(centrePanel, BoxLayout.Y_AXIS));
        JList<TrieNode> list = new JList<>(dict.getFromPrefix("").toArray(new TrieNode[0]));
        list.setCellRenderer(new ListRenderer());
        JScrollPane scrollPane = new JScrollPane(list);
        centrePanel.add(scrollPane);
        frame.add(centrePanel, BorderLayout.CENTER);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        buttonPane.add(okButton);
        buttonPane.add(cancelButton);
        buttonPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TrieNode node = new TrieNode();
                JFrame editorFrame = editorFrame(node);
                editorFrame.repaint();
                editorFrame.pack();
                editorFrame.setVisible(true);
            }
        });

        comboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                list.setListData(getSearchResult(comboBox, textField, 0));
            }
        });
        textField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                list.setListData(getSearchResult(comboBox, textField, 150));
            }
        });
        list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 3) {
                    TrieNode node = list.getSelectedValue();
                    editorFrame = editorFrame(node);
                    editorFrame.repaint();
                    editorFrame.pack();
                    editorFrame.setVisible(true);
                } else {
                    TrieNode node = list.getSelectedValue();
                    setDetailsFrame(node);
                    dict.history.add(node);
//                    detailsFrame.setVisible(true);
                    System.out.println(node);
                }
            }
        });

        frame.add(buttonPane, BorderLayout.PAGE_END);
        frame.revalidate();
        frame.repaint();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);
        return frame;
    }

    private static JPanel setDetailsFrame(TrieNode node) {
        if (detailsFrame == null) {
            detailsFrame = new JPanel();
        } else {
            detailsFrame.removeAll();
        }

        JPanel wrapperPanel = new JPanel();
        wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));

        JLabel wordLabel = new JLabel(node.word);
        wordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        wrapperPanel.add(wordLabel);

        JPanel definitionPanel = new JPanel();
        definitionPanel.setLayout(new BoxLayout(definitionPanel, BoxLayout.Y_AXIS));
        for (String definition : node.definitions) {
            JLabel definitionLabel = new JLabel(definition);
            definitionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            definitionPanel.add(definitionLabel);
        }
        wrapperPanel.add(definitionPanel);

        detailsFrame.add(wrapperPanel);
        detailsFrame.repaint();
        detailsFrame.revalidate();

        splitPane.setRightComponent(detailsFrame);
        splitPane.setDividerLocation(splitPane.getDividerLocation());
        splitPane.repaint();
        splitPane.revalidate();

        return wrapperPanel;
    }

    private static TrieNode[] getSearchResult(JComboBox comboBox, JTextField textField, int delay) {
        TrieNode[] nodes = new TrieNode[0];
//        Timer timer = new Timer(delay, new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
        if (comboBox.getSelectedItem().toString().equals("Word")) {
            nodes = dict.getFromPrefix((textField.getText())).toArray(new TrieNode[0]);
        } else {
            nodes = dict.getFromDefinition((textField.getText())).toArray(new TrieNode[0]);
        }
//            }
//        });
//        timer.setRepeats(false);
//        timer.start();
        return nodes;
    }

    private static JFrame editorFrame(TrieNode node) {
        JFrame frame = new JFrame("Editor");

        JPanel wrapperPanel = new JPanel();
        wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));

        JTextField wordField = new JTextField();
        JScrollPane scrollPane = new JScrollPane();

        DefaultTableModel model = new DefaultTableModel(new Object[]{"Definition"}, 0);
        JTable definitionField = new JTable(model);
        JButton saveButton = new JButton("Save");
        JButton deleteButton = new JButton("Delete");
        JButton cancelButton2 = new JButton("Cancel");
        JButton addButton = new JButton("Add");

        scrollPane.setViewportView(definitionField);

        wordField.setText(node.word);
        for (String definition : node.definitions) {
            model.addRow(new Object[]{definition});
        }

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

        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String word = wordField.getText();
                if (word.isBlank()) {
                    JOptionPane.showMessageDialog(null, "Word cannot be blank");
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
                dict.edit(node, wordField.getText().toUpperCase(), definitions, merge == 0);
            }
        });

        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dict.delete(node);
                JOptionPane.showMessageDialog(null, "Word deleted");
                editorFrame.setVisible(false);
            }
        });

        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                model.addRow(new Object[]{""});
            }
        });


        wrapperPanel.add(wordField);
        wrapperPanel.add(scrollPane);
        wrapperPanel.add(saveButton);
        wrapperPanel.add(deleteButton);
        wrapperPanel.add(cancelButton2);
        wrapperPanel.add(addButton);
        wrapperPanel.setAlignmentX(Component.CENTER_ALIGNMENT);


        frame.add(wrapperPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();

        return frame;
    }

    private static JPanel quiz(boolean word) {
//        JFrame frame = new JFrame("Quiz");

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
//
////        pressing enter on the frame will trigger the button
//        frame.getRootPane().setDefaultButton(nextButton);
//
//
//        frame.add(panel);
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);
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
        JFrame frame = new JFrame();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel welcomeText = new JLabel("Welcome to SlangDict");
        welcomeText.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeText.setFont(new Font("Serif", Font.BOLD, 20));
        panel.add(welcomeText);

//        JLabel label = new JLabel(dict.motd().toString());
//        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(setDetailsFrame(dict.motd()));

        return panel;
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            dict = new Dictionary("slang.txt");
            createAndShowGUI();
        });
    }
}
