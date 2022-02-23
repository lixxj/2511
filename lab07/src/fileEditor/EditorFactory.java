package fileEditor;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public abstract class EditorFactory {

    public JFrame createFrame() {
        JFrame frame = new JFrame("Basic Editor");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenSize.height = screenSize.height - (25 * screenSize.height / 768);
        frame.setSize(screenSize);
        return frame;
    }

    public JDesktopPane createDesktopPane() {
        return new JDesktopPane();
    }

    public ActionListener createActionListener(JDesktopPane pane) {
        return new ButtonListener(pane);
    }

    public JMenuBar createMenuBar(ActionListener listener) {
        // Create Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        String[] fileMenuItems = new String[] { "New", "Open", "Save", "Save As" };
        for (int i = 0; i < fileMenuItems.length; i++) {
            JMenuItem menuItem = new JMenuItem(fileMenuItems[i]);
            fileMenu.add(menuItem);
            menuItem.addActionListener(listener); // Listens to button press
        }
        menuBar.add(fileMenu);

        //      Edit menu
        JMenu editMenu = new JMenu("Edit");
        String[] editMenuItems = new String[] { "Copy", "Cut", "Paste", "Select All" };

        for (int i = 0; i < editMenuItems.length; i++) {
            JMenuItem menuItem = new JMenuItem(editMenuItems[i]);
            editMenu.add(menuItem);
            menuItem.addActionListener(listener); // Listens to button press
        }
        menuBar.add(editMenu);
        return menuBar;
    }

    public JToolBar createToolBar(ActionListener listener) {
        JToolBar toolBar = new JToolBar();
        String[] buttons = new String[] { "New", "Open", "Save", "Copy", "Cut", "Paste" };

        for (int i = 0; i < buttons.length; i++) {
            JButton button = new JButton(buttons[i], new ImageIcon(buttons[i] + ".jpg"));
            button.setPreferredSize(new Dimension(500, 50));
            toolBar.add(button);
            button.addActionListener(listener);
            if (i == 2)
                toolBar.addSeparator(new Dimension(10, toolBar.getHeight()));
        }
        return toolBar;
    }

    public WindowAdapter createWindowAdapter(JDesktopPane pane) {
        return new WindowCloser(pane);
    }
}
