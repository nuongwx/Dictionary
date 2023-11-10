import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

public class GuiDictionaryPanel extends JPanel implements ActionListener {
    static Dictionary dict = GUI.dict;
    static JTextField textField = new JTextField();

    static DefaultListModel<TrieNode> listModel = new DefaultListModel<>();
    static JList<TrieNode> list = new JList<>(listModel);

    static JSplitPane gSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JSplitPane(), new JPanel());
    static boolean searchWord = true;

    public GuiDictionaryPanel() {
        listModel.addAll(dict.getFromPrefix(""));

        setLayout(new BorderLayout());
        add(createAndShowGUI());
    }

    public static void updateList() {
        listModel.clear();
        String query = textField.getText();
        if (query.isBlank()) {
            query = "";
        }
        if (searchWord) {
            listModel.addAll(dict.getFromPrefix(query));
        } else {
            listModel.addAll(dict.getFromDefinition(query));
        }
//        setDetailsFrame(null);
    }

    public JPanel createAndShowGUI() {
        gSplitPane.setDividerLocation(400);
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));

        textField.setColumns(20);
        textField.setMinimumSize(new Dimension(100, 20));
        JButton addButton = new JButton("➕");
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"Word", "Definition"});
        topPanel.add(textField);
        topPanel.add(comboBox);
        topPanel.add(addButton);
        topPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        searchPanel.add(topPanel, BorderLayout.PAGE_START);

        JPanel centrePanel = new JPanel();
        centrePanel.setLayout(new BoxLayout(centrePanel, BoxLayout.Y_AXIS));
        list = new JList<>(listModel);
        list.setCellRenderer(new ListRenderer());
        JScrollPane scrollPane = new JScrollPane(list);
        centrePanel.add(scrollPane);
        searchPanel.add(centrePanel, BorderLayout.CENTER);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
        buttonPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        searchPanel.add(buttonPane, BorderLayout.PAGE_END);

        JPanel historyPane = new JPanel();
        historyPane.setLayout(new BoxLayout(historyPane, BoxLayout.Y_AXIS));

        JSplitPane searchHistSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, searchPanel, historyPane);
        searchHistSplitPane.setDividerLocation(200);

        JPanel detailsPane = setDetailsFrame(null);
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TrieNode node = new TrieNode();
                GuiEditorFrame.show(node);
            }
        });
        comboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                searchWord = comboBox.getSelectedIndex() == 0;
                updateList();
            }
        });

        textField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                updateList();

            }
        });
        list.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                TrieNode node = list.getSelectedValue();
                if (node == null) {
                    return;
                } else if (!node.isEndOfWord) {
                    ((DefaultListModel<TrieNode>) list.getModel()).removeElement(node);
                    return;
                }
                setDetailsFrame(node);
                if (!dict.history.isEmpty() && dict.history.getFirst() == node) {
                    return;
                } else {
                    dict.history.addFirst(node);
                }

                historyPane.removeAll();
                JList<TrieNode> histList = new JList<>(dict.history.toArray(new TrieNode[0]));
                JScrollPane histScrollPane = new JScrollPane(histList);
                historyPane.add(histScrollPane);
                historyPane.revalidate();
                historyPane.repaint();

                searchHistSplitPane.setDividerLocation(searchHistSplitPane.getDividerLocation());
                gSplitPane.setDividerLocation(gSplitPane.getDividerLocation());

            }

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    TrieNode node = list.getSelectedValue();
                    if (!node.isEndOfWord) {
                        return;
                    }
                    GuiEditorFrame.show(node);
                }
            }
        });
        gSplitPane.setLeftComponent(searchHistSplitPane);
        gSplitPane.setDividerLocation(gSplitPane.getDividerLocation());
        gSplitPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JPanel wrapper = new JPanel();
        wrapper.setBorder(BorderFactory.createDashedBorder(Color.BLACK));
        wrapper.setLayout(new BorderLayout());
        wrapper.add(gSplitPane);
        return wrapper;
    }

    public static JPanel setDetailsFrame(TrieNode node) {
        JPanel wrapperPanel = new JPanel();
        if (node != null) {
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

//        detailsFrame.add(wrapperPanel);
//        detailsFrame.repaint();
//        detailsFrame.revalidate();
        } else {
            wrapperPanel.add(new JLabel("No word selected"));
        }

        gSplitPane.setRightComponent(wrapperPanel);
        gSplitPane.setDividerLocation(gSplitPane.getDividerLocation());
        gSplitPane.repaint();
        gSplitPane.revalidate();

        return wrapperPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

}
