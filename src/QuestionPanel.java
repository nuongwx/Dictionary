package src;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class QuestionPanel extends JPanel {
    public QuestionPanel(boolean word) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        List<TrieNode> nodes = GUI.dict.quiz();
        if (nodes.isEmpty()) {
            add(new JLabel("No words in dictionary"));
            return;
        }
        TrieNode ans = nodes.get(new Random().nextInt(nodes.size()));

        JLabel question;
        if (word) {
            question = new JLabel(ans.definitions.get(0));
        } else {
            question = new JLabel(ans.word);
        }
        question.setAlignmentX(CENTER_ALIGNMENT);
        add(question);

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
            button.setAlignmentX(CENTER_ALIGNMENT);
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

//        JPanel wrapperPanel = new JPanel();
//        wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));
//        wrapperPanel.add(this);
//        wrapperPanel.add(buttonPanel);

        add(buttonPanel);
    }
}
