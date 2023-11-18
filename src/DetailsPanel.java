package src;

import javax.swing.*;
import java.awt.*;

public class DetailsPanel extends JPanel {
    public static JLabel wordLabel = new JLabel("");
    public static JPanel definitionPanel = new JPanel();

    public DetailsPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        wordLabel.setText("Select a word to view its details.");
        wordLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(wordLabel);
        definitionPanel.removeAll();
        definitionPanel.validate();
        definitionPanel.repaint();
        add(definitionPanel);
    }

    public DetailsPanel(TrieNode node) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        if (node == null) {
            return;
        }

        wordLabel.setText(node.word);
        wordLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(wordLabel);

        definitionPanel.removeAll();
        definitionPanel.setLayout(new BoxLayout(definitionPanel, BoxLayout.Y_AXIS));
        for (String definition : node.definitions) {
            JLabel definitionLabel = new JLabel(definition);
            definitionLabel.setAlignmentX(CENTER_ALIGNMENT);
            definitionPanel.add(definitionLabel);
        }
        definitionPanel.revalidate();
        definitionPanel.repaint();
        add(definitionPanel);
    }

    public static void update(TrieNode node) {
        wordLabel.setText(node.word);

        definitionPanel.removeAll();
        definitionPanel.setLayout(new BoxLayout(definitionPanel, BoxLayout.Y_AXIS));
        for (String definition : node.definitions) {
            JLabel definitionLabel = new JLabel(definition);
            definitionLabel.setAlignmentX(CENTER_ALIGNMENT);
            definitionPanel.add(definitionLabel);
        }
        definitionPanel.revalidate();
        definitionPanel.repaint();
    }

    public static void clear() {
        wordLabel.setText("Select a word to view its details.");
        definitionPanel.removeAll();
        definitionPanel.validate();
        definitionPanel.repaint();
    }

}
