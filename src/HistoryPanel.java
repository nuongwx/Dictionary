package src;

import javax.swing.*;

public class HistoryPanel extends JPanel {
    public static DefaultListModel<TrieNode> histListModel = new DefaultListModel<>();

    public HistoryPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        histListModel = new DefaultListModel<>();

        JList<TrieNode> histList = new JList<>(histListModel);
        histListModel.addAll(GUI.dict.history);

        JScrollPane histScrollPane = new JScrollPane(histList);
        histScrollPane.setAlignmentX(CENTER_ALIGNMENT);
        add(histScrollPane);
    }

    public static void update() {
        histListModel.removeAllElements();
        histListModel.addAll(GUI.dict.history);
    }
}
