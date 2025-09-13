import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class NotepadApp extends JFrame implements ActionListener {

    private JTextArea editorArea;
    private JMenuBar topMenuBar;
    private JMenu fileMenu, editMenu, formatMenu, helpMenu;
    private JMenuItem openMenuItem, saveMenuItem, exitMenuItem;
    private JMenuItem cutMenuItem, copyMenuItem, pasteMenuItem;
    private JMenuItem fontMenuItem, colorMenuItem;
    private JMenuItem aboutMenuItem;

    public NotepadApp() {
        super("My Simple Notepad");
        setSize(750, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Setting up the text editing area inside a scroll pane
        editorArea = new JTextArea();
        JScrollPane scroll = new JScrollPane(editorArea);
        add(scroll, BorderLayout.CENTER);

        // Construct the Menu Bar and its items
        topMenuBar = new JMenuBar();

        // File Menu with Open, Save, Exit
        fileMenu = new JMenu("File");
        openMenuItem = new JMenuItem("Open");
        saveMenuItem = new JMenuItem("Save");
        exitMenuItem = new JMenuItem("Exit");
        openMenuItem.addActionListener(this);
        saveMenuItem.addActionListener(this);
        exitMenuItem.addActionListener(this);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);

        // Edit Menu with Cut, Copy, Paste
        editMenu = new JMenu("Edit");
        cutMenuItem = new JMenuItem("Cut");
        copyMenuItem = new JMenuItem("Copy");
        pasteMenuItem = new JMenuItem("Paste");
        cutMenuItem.addActionListener(this);
        copyMenuItem.addActionListener(this);
        pasteMenuItem.addActionListener(this);
        editMenu.add(cutMenuItem);
        editMenu.add(copyMenuItem);
        editMenu.add(pasteMenuItem);

        // Format Menu with Font and Color
        formatMenu = new JMenu("Format");
        fontMenuItem = new JMenuItem("Font");
        colorMenuItem = new JMenuItem("Color");
        fontMenuItem.addActionListener(this);
        colorMenuItem.addActionListener(this);
        formatMenu.add(fontMenuItem);
        formatMenu.add(colorMenuItem);

        // Help Menu with About info
        helpMenu = new JMenu("Help");
        aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(this);
        helpMenu.add(aboutMenuItem);

        // Add all the menus to the menu bar
        topMenuBar.add(fileMenu);
        topMenuBar.add(editMenu);
        topMenuBar.add(formatMenu);
        topMenuBar.add(helpMenu);

        setJMenuBar(topMenuBar);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String cmd = event.getActionCommand();

        switch (cmd) {
            case "Open" -> openFile();
            case "Save" -> saveFile();
            case "Exit" -> System.exit(0);
            case "Cut" -> editorArea.cut();
            case "Copy" -> editorArea.copy();
            case "Paste" -> editorArea.paste();
            case "Font" -> chooseFont();
            case "Color" -> chooseColor();
            case "About" -> showAboutDialog();
        }
    }

    private void openFile() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                editorArea.read(br, null);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Could not open file: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveFile() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                editorArea.write(bw);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Could not save file: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void chooseColor() {
        Color selectedColor = JColorChooser.showDialog(this,
                "Pick Text Color", editorArea.getForeground());
        if (selectedColor != null) {
            editorArea.setForeground(selectedColor);
        }
    }

    private void chooseFont() {
        JDialog fontDialog = new JDialog(this, "Choose Font", true);
        fontDialog.setLayout(new FlowLayout());
        fontDialog.setSize(320, 220);

        String[] fontFamilies = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getAvailableFontFamilyNames();
        JComboBox<String> fontBox = new JComboBox<>(fontFamilies);
        fontBox.setSelectedItem(editorArea.getFont().getFamily());

        Integer[] fontSizes = {8, 10, 12, 14, 16, 18, 20, 24, 28, 32, 36, 48, 72};
        JComboBox<Integer> sizeBox = new JComboBox<>(fontSizes);
        sizeBox.setSelectedItem(editorArea.getFont().getSize());

        JButton applyBtn = new JButton("Apply");
        applyBtn.addActionListener(e -> {
            String chosenFont = (String) fontBox.getSelectedItem();
            int chosenSize = (Integer) sizeBox.getSelectedItem();
            editorArea.setFont(new Font(chosenFont, Font.PLAIN, chosenSize));
            fontDialog.dispose();
        });

        fontDialog.add(new JLabel("Font:"));
        fontDialog.add(fontBox);
        fontDialog.add(new JLabel("Size:"));
        fontDialog.add(sizeBox);
        fontDialog.add(applyBtn);

        fontDialog.setLocationRelativeTo(this);
        fontDialog.setVisible(true);
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
                "Simple Notepad Application\nCreated by Ruchinthaka Ishan\nStudent ID: 2022s19215",
                "About", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NotepadApp notepad = new NotepadApp();
            notepad.setVisible(true);
        });
    }
}
