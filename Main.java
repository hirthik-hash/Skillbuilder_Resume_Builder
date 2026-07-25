import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

public class Main extends JFrame {

    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextArea educationArea;
    private JTextArea experienceArea;
    private JTextArea skillsArea;

    private JTextArea previewArea;

    public Main() {
        setTitle("Resume Builder");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        add(buildFormPanel(), BorderLayout.WEST);
        add(buildPreviewPanel(), BorderLayout.CENTER);
        add(buildButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Your Details"));
        panel.setPreferredSize(new Dimension(350, 600));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        int row = 0;

        gbc.gridy = row++;
        panel.add(new JLabel("Full Name:"), gbc);
        gbc.gridy = row++;
        nameField = new JTextField();
        panel.add(nameField, gbc);

        gbc.gridy = row++;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridy = row++;
        emailField = new JTextField();
        panel.add(emailField, gbc);

        gbc.gridy = row++;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridy = row++;
        phoneField = new JTextField();
        panel.add(phoneField, gbc);

        gbc.gridy = row++;
        panel.add(new JLabel("Education:"), gbc);
        gbc.gridy = row++;
        educationArea = new JTextArea(3, 20);
        educationArea.setLineWrap(true);
        panel.add(new JScrollPane(educationArea), gbc);

        gbc.gridy = row++;
        panel.add(new JLabel("Work Experience:"), gbc);
        gbc.gridy = row++;
        experienceArea = new JTextArea(4, 20);
        experienceArea.setLineWrap(true);
        panel.add(new JScrollPane(experienceArea), gbc);


        gbc.gridy = row++;
        panel.add(new JLabel("Skills (comma-separated):"), gbc);
        gbc.gridy = row++;
        skillsArea = new JTextArea(2, 20);
        skillsArea.setLineWrap(true);
        panel.add(new JScrollPane(skillsArea), gbc);

        return panel;
    }


    private JPanel buildPreviewPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Resume Preview"));

        previewArea = new JTextArea();
        previewArea.setEditable(false);
        previewArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        panel.add(new JScrollPane(previewArea), BorderLayout.CENTER);
        return panel;
    }


    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel();

        JButton generateButton = new JButton("Generate Resume");
        JButton saveButton = new JButton("Save to File");
        JButton clearButton = new JButton("Clear All");

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateResume();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveResumeToFile();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearAllFields();
            }
        });

        panel.add(generateButton);
        panel.add(saveButton);
        panel.add(clearButton);

        return panel;
    }


    private void generateResume() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String education = educationArea.getText().trim();
        String experience = experienceArea.getText().trim();
        String skills = skillsArea.getText().trim();

        // Basic validation
        if (name.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Name and Email are required fields.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }


        StringBuilder resume = new StringBuilder();
        resume.append("=".repeat(50)).append("\n");
        resume.append(centerText(name.toUpperCase(), 50)).append("\n");
        resume.append(centerText(email + "  |  " + phone, 50)).append("\n");
        resume.append("=".repeat(50)).append("\n\n");

        resume.append("EDUCATION\n");
        resume.append("-".repeat(50)).append("\n");
        resume.append(education.isEmpty() ? "(Not provided)" : education).append("\n\n");

        resume.append("WORK EXPERIENCE\n");
        resume.append("-".repeat(50)).append("\n");
        resume.append(experience.isEmpty() ? "(Not provided)" : experience).append("\n\n");

        resume.append("SKILLS\n");
        resume.append("-".repeat(50)).append("\n");
        resume.append(skills.isEmpty() ? "(Not provided)" : formatSkills(skills)).append("\n");

        previewArea.setText(resume.toString());
    }


    private String formatSkills(String skills) {
        String[] skillArray = skills.split(",");
        StringBuilder formatted = new StringBuilder();
        for (String skill : skillArray) {
            String trimmed = skill.trim();
            if (!trimmed.isEmpty()) {
                formatted.append("  • ").append(trimmed).append("\n");
            }
        }
        return formatted.toString();
    }


    private String centerText(String text, int width) {
        if (text.length() >= width) return text;
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text;
    }


    private void saveResumeToFile() {
        if (previewArea.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please generate the resume first.",
                    "Nothing to Save",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new java.io.File("resume.txt"));
        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(fileToSave)) {
                writer.write(previewArea.getText());
                JOptionPane.showMessageDialog(this,
                        "Resume saved to:\n" + fileToSave.getAbsolutePath(),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Error saving file: " + e.getMessage(),
                        "Save Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearAllFields() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        educationArea.setText("");
        experienceArea.setText("");
        skillsArea.setText("");
        previewArea.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.setVisible(true);
        });
    }
}