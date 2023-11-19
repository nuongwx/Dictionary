package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class SearchPanel extends JPanel {
    public static DefaultListModel<TrieNode> listModel = new DefaultListModel<>();

    public SearchPanel() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        JTextField textField = new JTextField();
        textField.setColumns(20);
        textField.setMinimumSize(new Dimension(100, 20));
        JButton addButton = new JButton("➕");
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"Word", "Definition"});
        topPanel.add(textField);
        topPanel.add(comboBox);
        topPanel.add(addButton);
        topPanel.setAlignmentX(CENTER_ALIGNMENT);
        add(topPanel, BorderLayout.PAGE_START);

        JPanel centrePanel = new JPanel();
        centrePanel.setLayout(new BoxLayout(centrePanel, BoxLayout.Y_AXIS));

//        DefaultListModel<TrieNode> listModel = new DefaultListModel<>();
        listModel.clear();
        listModel.addAll(GUI.dict.getFromPrefix(""));
        JList<TrieNode> list = new JList<>(listModel);
        list.setCellRenderer(new ListRenderer());
        JScrollPane scrollPane = new JScrollPane(list);
        centrePanel.add(scrollPane);
        add(centrePanel, BorderLayout.CENTER);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
        buttonPane.setAlignmentX(CENTER_ALIGNMENT);

        add(buttonPane, BorderLayout.PAGE_END);
        revalidate();
        repaint();

        addButton.addActionListener(evt -> {
            TrieNode node = new TrieNode();
            new EditorFrame(node);
        });

        comboBox.addItemListener(evt -> {
            listModel.clear();
            listModel.addAll(List.of(getSearchResult(comboBox, textField)));
        });

        textField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                listModel.clear();
                listModel.addAll(List.of(getSearchResult(comboBox, textField)));
            }
        });

        list.addMouseListener(new java.awt.event.MouseAdapter() {
            int evtClickCount = 0;
            final java.util.Timer timer = new java.util.Timer();

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                evtClickCount = e.getClickCount();
                if (evtClickCount == 1) {
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (evtClickCount == 1) {
                                singleClick();
                            } else {
                                doubleClick();
                            }
                            evtClickCount = 0;
                        }
                    }, 250);
                }
            }

            public void singleClick() {
                TrieNode node = list.getSelectedValue();
                GUI.dict.history.addFirst(node);
                HistoryPanel.update();
                DetailsPanel.update(node);
            }

            public void doubleClick() {
                TrieNode node = list.getSelectedValue();
                new EditorFrame(node);
            }
        });
    }

    private TrieNode[] getSearchResult(JComboBox<String> comboBox, JTextField textField) {
        TrieNode[] nodes;
        if (Objects.requireNonNull(comboBox.getSelectedItem()).toString().equals("Word")) {
            nodes = GUI.dict.getFromPrefix((textField.getText())).toArray(new TrieNode[0]);
        } else {
            nodes = GUI.dict.getFromDefinition((textField.getText())).toArray(new TrieNode[0]);
        }
        return nodes;
    }

    public static void update() {
        listModel.clear();
        listModel.addAll(GUI.dict.getFromPrefix(""));
    }
}
