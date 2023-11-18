package src;

import javax.swing.*;

public class Motd extends JPanel {
    public Motd() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel welcomeText = new JLabel("Welcome to SlangDict");
        welcomeText.setAlignmentX(CENTER_ALIGNMENT);
        welcomeText.setFont(welcomeText.getFont().deriveFont(20f));
        add(welcomeText);
        TrieNode node = GUI.dict.motd();
        if (node != null) {
            JLabel wordLabel = new JLabel(node.word);
            wordLabel.setAlignmentX(CENTER_ALIGNMENT);
            wordLabel.setFont(wordLabel.getFont().deriveFont(20f));
            add(wordLabel);

            JPanel definitionPanel = new JPanel();
            definitionPanel.setLayout(new BoxLayout(definitionPanel, BoxLayout.Y_AXIS));
            for (String definition : node.definitions) {
                JLabel definitionLabel = new JLabel(definition);
                definitionLabel.setAlignmentX(CENTER_ALIGNMENT);
                definitionPanel.add(definitionLabel);
            }
            add(definitionPanel);
        }
    }
}
