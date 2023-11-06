import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class GUI {

    public static Dictionary dict;
    static JFrame wordSearchFrame;
    static JFrame editorFrame;

    static JFrame historyFrame;
    private static void createAndShowGUI() {
        wordSearchFrame = searchPanel();
        editorFrame = new JFrame("Editor");
        historyFrame = new JFrame("History");
        wordSearchFrame.setVisible(true);



    }

    public static void showHistory() {
        historyFrame = new JFrame("History");

        JPanel wrapperPanel = new JPanel();
        wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));
        JList<TrieNode> list = new JList<>(dict.history.toArray(new TrieNode[0]));
//        list.setListData();

        JScrollPane scrollPane = new JScrollPane(list);
        wrapperPanel.add(scrollPane);

        historyFrame.repaint();
        historyFrame.add(wrapperPanel);
        historyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        historyFrame.pack();
        historyFrame.setVisible(true);
    }

    private static JFrame searchPanel() {
        JFrame frame = new JFrame("BoxLayoutDemo");

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        JTextField textField = new JTextField();
        JButton searchButton = new JButton("Search");
        topPanel.add(textField);
        topPanel.add(searchButton);
        topPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        frame.add(topPanel, BorderLayout.PAGE_START);

        // todo refactor this
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showHistory();
            }
        });

        JPanel centrePanel = new JPanel();
        centrePanel.setLayout(new BoxLayout(centrePanel, BoxLayout.Y_AXIS));
        JList<TrieNode> list = new JList<>(dict.getFromPrefix("").toArray(new TrieNode[0]));
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

        textField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                System.out.println(textField.getText());
//                System.out.println(dict.getFromPrefix(textField.getText()));
                list.setListData(dict.getFromPrefix(textField.getText()).toArray(new TrieNode[0]));
            }
        });
        list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    TrieNode node = list.getSelectedValue();
                    dict.history.add(node);
                    System.out.println(dict.history);
                    editorFrame = editorFrame(node);
                    editorFrame.repaint();
                    editorFrame.pack();
                    editorFrame.setVisible(true);
                }
            }
        });

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowActivated(java.awt.event.WindowEvent windowEvent) {
                list.setListData(dict.getFromPrefix(textField.getText()).toArray(new TrieNode[0]));
            }
        });

        frame.add(buttonPane, BorderLayout.PAGE_END);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        return frame;
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

        scrollPane.setViewportView(definitionField);

        wordField.setText(node.word);
        for(String definition : node.definitions) {
            model.addRow(new Object[]{definition});
        }

        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String word = wordField.getText();
                if(word.isBlank()) {
                    JOptionPane.showMessageDialog(null, "Word cannot be blank");
                    return;
                }
                int merge = 0;
                TrieNode dictNode = dict.get(word);
                if(dictNode != null && dictNode.isEndOfWord) {
                    Object[] options = {"Append", "Overwrite",  "Cancel"};
                    merge = JOptionPane.showOptionDialog(null, "Word already exists, do you want to overwrite or append the definition?", "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                    if(merge == 2) {
                        return;
                    }
                }
                ArrayList<String> definitions = new ArrayList<>();
                for (int i = 0; i < definitionField.getRowCount(); i++) {
                    definitions.add(definitionField.getValueAt(i, 0).toString());
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

        wrapperPanel.add(wordField);
        wrapperPanel.add(scrollPane);
        wrapperPanel.add(saveButton);
        wrapperPanel.add(deleteButton);
        wrapperPanel.add(cancelButton2);
        wrapperPanel.setAlignmentX(Component.CENTER_ALIGNMENT);


        frame.add(wrapperPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();

        return frame;
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            dict = new Dictionary("slang.txt");
            createAndShowGUI();
        });
    }
}
