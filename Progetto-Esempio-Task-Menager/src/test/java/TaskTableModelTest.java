import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTableModelTest {

    @Test
    void testTableModelRowCount() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task("A", "Desc A", "2025"));
        tasks.add(new Task("B", "Desc B", "2026"));

        TaskTableModel model = new TaskTableModel(tasks);
        assertEquals(2, model.getRowCount());
    }

    @Test
    void testGetValueAt() {
        Task task = new Task("Test", "Desc", "2025");
        List<Task> tasks = List.of(task);
        TaskTableModel model = new TaskTableModel(tasks);

        assertEquals("Test", model.getValueAt(0, 0));
        assertEquals("Desc", model.getValueAt(0, 1));
        assertEquals("2025", model.getValueAt(0, 2));
        assertEquals("🕓 In sospeso", model.getValueAt(0, 3));

        task.completeTask();
        assertEquals("✅ Completata", model.getValueAt(0, 3));
    }
}
