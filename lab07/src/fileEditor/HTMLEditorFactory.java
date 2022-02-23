package fileEditor;

import java.awt.event.ActionListener;
import javax.swing.JMenuBar;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JToolBar;

public class HTMLEditorFactory extends EditorFactory {
    
    @Override
    public JMenuBar createMenuBar(ActionListener listener) {
        JMenuBar menuBar = super.createMenuBar(listener);
        JTextField urlField = new JTextField();
        JButton goUrl = new JButton("Go");
        ButtonListener blistener = (ButtonListener) listener;
        blistener.setTextField(urlField);

        goUrl.addActionListener(listener);
        menuBar.add(urlField);
        menuBar.add(goUrl);

        return menuBar;
    }
}
