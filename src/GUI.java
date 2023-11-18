package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class GUI {

    public static Dictionary dict;
    static JFrame dictionaryFrame;
    static JSplitPane searchDetailsSplitPane;
    static JSplitPane searchHistorySplitPane;

    public static JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JToggleButton dictionaryBtn = new JToggleButton("Dictionary");
        dictionaryBtn.setModel(new DefaultButtonModel());
        dictionaryBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int lastDividerLocation = 200;
                if (searchHistorySplitPane != null && searchHistorySplitPane.getDividerLocation() != 200) {
                    lastDividerLocation = searchHistorySplitPane.getDividerLocation();
                }
                searchHistorySplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new SearchPanel(), new HistoryPanel());
                searchHistorySplitPane.setDividerLocation(lastDividerLocation);

                lastDividerLocation = 400;
                if (searchDetailsSplitPane != null && searchDetailsSplitPane.getDividerLocation() != 400) {
                    lastDividerLocation = searchDetailsSplitPane.getDividerLocation();
                }

                searchDetailsSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, searchHistorySplitPane, new DetailsPanel());
                searchDetailsSplitPane.setDividerLocation(lastDividerLocation);
                searchDetailsSplitPane.setResizeWeight(0.5);
                updateMainFrame(searchDetailsSplitPane);
            }
        });
        menuBar.add(dictionaryBtn);

        JToggleButton quizBtn = new JToggleButton("Quiz");
        quizBtn.setModel(new DefaultButtonModel());
        quizBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMainFrame(new QuizPanel(true));
            }
        });
        menuBar.add(quizBtn);

        JToggleButton manageBtn = new JToggleButton("Manage");
        manageBtn.setModel(new DefaultButtonModel());
        manageBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMainFrame(new DbManagementPanel());
            }
        });
        menuBar.add(manageBtn);

        menuBar.add(Box.createHorizontalGlue());

        JToggleButton homeBtn = new JToggleButton("Home");
        homeBtn.setModel(new DefaultButtonModel());
        homeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMainFrame(new Motd());
            }
        });
        homeBtn.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        menuBar.add(homeBtn);

        return menuBar;
    }

    public static void updateMainFrame(JComponent panel) {
        panel.setOpaque(true);
        dictionaryFrame.setContentPane(panel);
        dictionaryFrame.revalidate();
        dictionaryFrame.repaint();

    }

    public static void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);

        dictionaryFrame = new JFrame("SlangDict");
        dictionaryFrame.setJMenuBar(createMenuBar());
        dictionaryFrame.setContentPane(new Motd());
        dictionaryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dictionaryFrame.setMinimumSize(new Dimension(640, 360));
        dictionaryFrame.setVisible(true);


        dictionaryFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                dict.save();
            }

            @Override
            public void windowActivated(WindowEvent e) {
                super.windowActivated(e);
                SearchPanel.update();
                HistoryPanel.update();
                DetailsPanel.clear();
            }
        });
    }

    public static void reload() {
        dict = new Dictionary("slang.txt", true);
        dict.save();
        dictionaryFrame.dispose();
        createAndShowGUI();
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                dict = new Dictionary("slang.txt", false);
            } catch (Exception e) {
                System.out.println("Error loading dictionary, reloading...");
                dict = new Dictionary("slang.txt", true);
            }
            dict.save();
            createAndShowGUI();
        });
    }
}
