package fileEditor;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;

public class EditorApplication {

    private JFrame frame;
    private JMenuBar menuBar;
    private JToolBar toolBar;
    private WindowAdapter windowAdapter;
    private JDesktopPane pane;
    private ActionListener listener;
    
    public void setFrameVisible(boolean value) {
        frame.setVisible(value);
    }
  
    public EditorApplication(EditorFactory factory) {
        this.frame = factory.createFrame();
        this.pane = factory.createDesktopPane();
        this.listener = factory.createActionListener(pane);
        this.menuBar = factory.createMenuBar(listener);
        this.toolBar = factory.createToolBar(listener);
        this.windowAdapter = factory.createWindowAdapter(pane);

        // Add Components to Frame
        frame.setJMenuBar(menuBar);
        frame.addWindowListener(windowAdapter);
        frame.getContentPane().add(toolBar, "North");
        frame.getContentPane().add(pane);
    }

    public static void main(String[] args) {
        EditorFactory factory = new HTMLEditorFactory();
        EditorApplication app = new EditorApplication(factory);
        app.setFrameVisible(true);
    }
	
}