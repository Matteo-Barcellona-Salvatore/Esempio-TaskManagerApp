import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TaskManagerApp extends JFrame {

    private JTable taskTable;
    private TaskTableModel taskTableModel;
    private List<Task> tasks = new ArrayList<>();
    private final String FILE_NAME = "tasks.txt";
    private boolean isEditing = false;
    private int editingRowIndex = -1;

    private JTextField titleField = new JTextField();
    private JTextArea descriptionField = new JTextArea();
    private JTextField deadlineField = new JTextField();

    public TaskManagerApp() {

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        loadTasksFromFile();

        setTitle("Task Manager");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(700, 800));
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        inputPanel.setBorder(BorderFactory.createTitledBorder("Nuova Attività"));

        inputPanel.add(new JLabel("Titolo:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        inputPanel.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        inputPanel.add(new JLabel("Descrizione:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descriptionField);
        descScroll.setPreferredSize(new Dimension(200, 60));
        inputPanel.add(descScroll, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        inputPanel.add(new JLabel("Scadenza:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        inputPanel.add(deadlineField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("➕ Aggiungi");
        JButton completeButton = new JButton("✅ Completa");
        JButton deleteButton = new JButton("🗑️ Elimina");
        JButton editButton = new JButton("✏️ Modifica");
        JButton saveEditButton = new JButton("✔️ Salva Modifiche");

        buttonPanel.add(addButton);
        buttonPanel.add(completeButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        buttonPanel.add(saveEditButton);

        taskTableModel = new TaskTableModel(tasks);
        taskTable = new JTable(taskTableModel);
        taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskTable.setShowGrid(true);
        taskTable.setGridColor(UIManager.getColor("Table.gridColor")); // usa colore coerente col tema
        taskTable.setRowHeight(25);

        JScrollPane tableScrollPane = new JScrollPane(taskTable);

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createTitledBorder("Attività"));
        listPanel.add(tableScrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> addTask());
        completeButton.addActionListener(e -> completeSelectedTask());
        deleteButton.addActionListener(e -> deleteSelectedTask());
        editButton.addActionListener(e -> startEditingTask());
        saveEditButton.addActionListener(e -> saveEditedTask());

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(listPanel, BorderLayout.SOUTH);

        add(mainPanel);
        pack();
        setVisible(true);

        JMenuBar menuBar = new JMenuBar();
        JMenu settingsMenu = new JMenu("Impostazioni");
        JMenuItem lightModeItem = new JMenuItem("Tema Chiaro");
        JMenuItem darkModeItem = new JMenuItem("Tema Scuro");

        lightModeItem.addActionListener(e -> switchTheme("light"));
        darkModeItem.addActionListener(e -> switchTheme("dark"));

        settingsMenu.add(lightModeItem);
        settingsMenu.add(darkModeItem);
        menuBar.add(settingsMenu);
        setJMenuBar(menuBar);
    }

    private void switchTheme(String theme) {
        try {
            if (theme.equals("dark")) {
                UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }

            SwingUtilities.updateComponentTreeUI(this);
            if (taskTable != null) {
                taskTable.setShowGrid(true);
                taskTable.setGridColor(UIManager.getColor("Table.gridColor"));
            }

        } catch (UnsupportedLookAndFeelException e) {
            showMessage("Errore", "Impossibile cambiare tema.");
            e.printStackTrace();
        }
    }


    private void startEditingTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < tasks.size()) {
            Task task = tasks.get(selectedRow);
            titleField.setText(task.getTitle());
            descriptionField.setText(task.getDescription());
            deadlineField.setText(task.getDeadline());
            isEditing = true;
            editingRowIndex = selectedRow;
        } else {
            showMessage("Attenzione", "Seleziona un'attività da modificare.");
        }
    }

    private void saveEditedTask() {
        if (isEditing && editingRowIndex >= 0 && editingRowIndex < tasks.size()) {
            String title = titleField.getText().trim();
            String description = descriptionField.getText().trim();
            String deadline = deadlineField.getText().trim();

            if (!title.isEmpty() && !description.isEmpty() && !deadline.isEmpty()) {
                Task updatedTask = tasks.get(editingRowIndex);
                updatedTask.setTitle(title);
                updatedTask.setDescription(description);
                updatedTask.setDeadline(deadline);

                taskTableModel.fireTableDataChanged();
                saveTasksToFile();

                isEditing = false;
                editingRowIndex = -1;

                titleField.setText("");
                descriptionField.setText("");
                deadlineField.setText("");

            } else {
                showMessage("Errore", "Tutti i campi devono essere compilati.");
            }
        } else {
            showMessage("Errore", "Nessuna attività in modifica.");
        }
    }


    private void deleteSelectedTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < tasks.size()) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Sei sicuro di voler eliminare questa attività?",
                    "Conferma eliminazione",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                tasks.remove(selectedRow);
                taskTableModel.fireTableDataChanged();
                saveTasksToFile();
            }
        } else {
            showMessage("Attenzione", "Seleziona un'attività da eliminare.");
        }
    }


    private void addTask() {
        String title = titleField.getText().trim();
        String description = descriptionField.getText().trim();
        String deadline = deadlineField.getText().trim();

        if (!title.isEmpty() && !description.isEmpty() && !deadline.isEmpty()) {
            Task task = new Task(title, description, deadline);
            tasks.add(task);
            updateTaskList();
            titleField.setText("");
            descriptionField.setText("");
            deadlineField.setText("");
            saveTasksToFile();
        } else {
            showMessage("Errore", "Tutti i campi devono essere compilati.");
        }
    }

    private void completeSelectedTask() {
        int index = taskTable.getSelectedRow();
        if (index >= 0 && index < tasks.size()) {
            tasks.get(index).completeTask();
            updateTaskList();
            saveTasksToFile();
        } else {
            showMessage("Attenzione", "Seleziona un'attività da completare.");
        }
    }

    private void updateTaskList() {
        taskTableModel.fireTableDataChanged();
    }

    private void showMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void saveTasksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Task task : tasks) {
                String taskString = task.getTitle() + "," + task.getDescription() + "," + task.getDeadline() + "," + (task.isCompleted() ? "Completed" : "Pending");
                writer.write(taskString);
                writer.newLine();
            }
        } catch (IOException e) {
            showMessage("Errore", "Impossibile salvare le attività.");
            e.printStackTrace();
        }
    }

    private void loadTasksFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String title = parts[0];
                    String description = parts[1];
                    String deadline = parts[2];
                    boolean completed = "Completed".equals(parts[3]);
                    Task task = new Task(title, description, deadline);
                    if (completed) {
                        task.completeTask();
                    }
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TaskManagerApp::new);
    }


    static class TaskTableModel extends AbstractTableModel {
        private final String[] columnNames = {"Titolo", "Descrizione", "Scadenza", "Stato"};
        private final List<Task> tasks;

        public TaskTableModel(List<Task> tasks) {
            this.tasks = tasks;
        }

        @Override
        public int getRowCount() {
            return tasks.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Task task = tasks.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> task.getTitle();
                case 1 -> task.getDescription();
                case 2 -> task.getDeadline();
                case 3 -> task.isCompleted() ? "✅ Completata" : "🕓 In sospeso";
                default -> null;
            };
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }
    }
}