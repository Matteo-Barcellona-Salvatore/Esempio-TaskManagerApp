import javax.swing.table.AbstractTableModel;
import java.util.List;

class TaskTableModel extends AbstractTableModel {
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
