package src;

import javax.swing.*;

public class QuizPanel extends JPanel {
    public QuizPanel(boolean word) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Quiz");
        label.setAlignmentX(CENTER_ALIGNMENT);
        add(label);

        JPanel choosePanel = new JPanel();
        choosePanel.setLayout(new BoxLayout(choosePanel, BoxLayout.X_AXIS));
        JButton wordButton = new JButton("Word");
        JButton definitionButton = new JButton("Definition");
        choosePanel.add(wordButton);
        choosePanel.add(definitionButton);
        add(choosePanel);

        JPanel questionWrapperPanel = new JPanel();
        questionWrapperPanel.setLayout(new BoxLayout(questionWrapperPanel, BoxLayout.Y_AXIS));
        questionWrapperPanel.add(new QuestionPanel(word));

        add(questionWrapperPanel);

        JPanel bottomPanel = new JPanel();
        JButton nextButton = new JButton("Next");
        nextButton.setAlignmentX(CENTER_ALIGNMENT);
        bottomPanel.add(nextButton);
        add(bottomPanel);

        wordButton.addActionListener(evt -> {
            questionWrapperPanel.removeAll();
            questionWrapperPanel.add(new QuestionPanel(true));
            revalidate();
            repaint();
        });

        definitionButton.addActionListener(evt -> {
            questionWrapperPanel.removeAll();
            questionWrapperPanel.add(new QuestionPanel(false));
            revalidate();
            repaint();
        });
        nextButton.addActionListener(evt -> {
            questionWrapperPanel.removeAll();
            questionWrapperPanel.add(new QuestionPanel(word));
            questionWrapperPanel.revalidate();
            questionWrapperPanel.repaint();
        });

    }
}
