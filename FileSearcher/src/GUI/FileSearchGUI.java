package MultiThreadedFileSearch;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;

import CliManager.CliManager;

public class FileSearchGUI {

    private JFrame frame;
    private JTextField pathField;
    private JTextField searchField;
    private JTextField contentField;
    private JTextField ignoreFileField;
    private JTextField ignoreExtensionField;
    private JTextArea outputArea;
    private JButton searchButton;
    private JButton contentSearchButton;
    private JButton selectButton;
    private JButton ignoreFilesButton;
    private JButton ignoreExtensionsButton;

    // Sets to hold ignored files and extensions
    private HashSet<String> ignoredFilesSet = new HashSet<>();
    private HashSet<String> ignoredExtensionsSet = new HashSet<>();
    public FileSearchGUI() {
        frame = new JFrame("File Search Application");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);

        pathField = new JTextField(25);
        searchField = new JTextField(25);
        contentField = new JTextField(25);
        ignoreFileField = new JTextField(25);
        ignoreExtensionField = new JTextField(25);

        searchButton = new JButton("Search File");
        contentSearchButton = new JButton("Search Content");
        selectButton = new JButton("Select Directory");
        ignoreFilesButton = new JButton("Ignore Files");
        ignoreExtensionsButton = new JButton("Ignore Extensions");

        outputArea = new JTextArea(15, 40);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Directory Path: "), gbc);
        gbc.gridx = 1;
        panel.add(pathField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(selectButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Search File: "), gbc);
        gbc.gridx = 1;
        panel.add(searchField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(searchButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Search Content: "), gbc);
        gbc.gridx = 1;
        panel.add(contentField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(contentSearchButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Ignore File: "), gbc);
        gbc.gridx = 1;
        panel.add(ignoreFileField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(ignoreFilesButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        panel.add(new JLabel("Ignore Extension: "), gbc);
        gbc.gridx = 1;
        panel.add(ignoreExtensionField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        panel.add(ignoreExtensionsButton, gbc);

        frame.getContentPane().add(panel, BorderLayout.NORTH);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectDirectory();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performFileSearch();
            }
        });

        contentSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performContentSearch();
            }
        });

        ignoreFilesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addIgnoredFiles();
            }
        });

        ignoreExtensionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addIgnoredExtensions();
            }
        });

        frame.setVisible(true);
    }

    private void selectDirectory() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            pathField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void performFileSearch() {
        String directoryPath = pathField.getText();
        String searchTerm = searchField.getText();

        try {
            File directory = new File(directoryPath);
            if (!directory.isDirectory()) {
                outputArea.append("Error: Not a valid directory\n");
                return;
            }

            // Filter files based on ignored extensions and ignored file names
            File[] filesToIndex = directory.listFiles((dir, name) -> {
                String extension = getFileExtension(name);
                return !ignoredExtensionsSet.contains(extension) && !ignoredFilesSet.contains(name);
            });

            int indexedFileCount = 0; // Counter for indexed files

            if (filesToIndex == null || filesToIndex.length == 0) {
                outputArea.append("No files found for indexing after applying ignore rules.\n");
                return;
            }

            // Output the files that will be indexed
            for (File file : filesToIndex) {
                outputArea.append("Indexing file: " + file.getName() + "\n");
                indexedFileCount++; // Increment the count for each valid indexed file
                // Here, you might want to call your indexing logic or method
            }

            // Call the CLI manager with the necessary arguments
            String[] args = buildArgs(directoryPath, "-s", searchTerm);
            CliManager.redirectSystemOutput(outputArea);
            CliManager.parseArgs(args);
            outputArea.append("\nSearch for file '" + searchTerm + "' completed.\n");

            // Output the final indexed file count
            outputArea.append("Files indexed: " + indexedFileCount + "\n");
            outputArea.append("Indexing complete for directory, excluding ignored items.\n");

        } catch (Exception ex) {
            outputArea.append("Error: " + ex.getMessage() + "\n");
        }
    }

    private void performContentSearch() {
        String directoryPath = pathField.getText();
        String contentSearchTerm = contentField.getText();

        try {
            String[] args = buildArgs(directoryPath, "-f", contentSearchTerm);
            CliManager.redirectSystemOutput(outputArea);
            CliManager.parseArgs(args);
            outputArea.append("\nSearch for content '" + contentSearchTerm + "' completed.\n");
        } catch (Exception ex) {
            outputArea.append("Error: " + ex.getMessage() + "\n");
        }
    }

    private void addIgnoredFiles() {
        String ignoredFile = ignoreFileField.getText();
        ignoredFilesSet.add(ignoredFile);
        outputArea.append("Ignoring file: " + ignoredFile + "\n");
    }

    private void addIgnoredExtensions() {
        String ignoredExtension = ignoreExtensionField.getText();
        ignoredExtensionsSet.add(ignoredExtension);
        outputArea.append("Ignoring extension: " + ignoredExtension + "\n");
    }

    private String[] buildArgs(String directoryPath, String option, String term) {
        // Start with basic args
        String[] args = new String[]{"-r", directoryPath, option, term};

        // Add ignored files and extensions dynamically
        if (!ignoredFilesSet.isEmpty()) {
            args = concatenateArrays(args, new String[]{"-igf", String.join(",", ignoredFilesSet)});
        }

        if (!ignoredExtensionsSet.isEmpty()) {
            args = concatenateArrays(args, new String[]{"-ige", String.join(",", ignoredExtensionsSet)});
        }

        return args;
    }

    private String[] concatenateArrays(String[] original, String[] toAdd) {
        String[] result = Arrays.copyOf(original, original.length + toAdd.length);
        System.arraycopy(toAdd, 0, result, original.length, toAdd.length);
        return result;
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FileSearchGUI::new);
    }
}