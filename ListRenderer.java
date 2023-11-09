import javax.swing.*;
import java.awt.*;

public class ListRenderer extends JLabel implements ListCellRenderer<TrieNode> {
    public Component getListCellRendererComponent(JList<? extends TrieNode> list, TrieNode value, int index, boolean isSelected, boolean cellHasFocus) {
        setText(value.word);
        if (isSelected) {
            setBackground(Color.blue);
            setForeground(Color.white);
        } else {
            setBackground(Color.white);
            setForeground(Color.black);
        }
        setOpaque(true);
        return this;
    }
}
