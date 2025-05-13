import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    void testTaskCreation() {
        Task task = new Task("Titolo", "Descrizione", "2025-12-31");
        assertEquals("Titolo", task.getTitle());
        assertEquals("Descrizione", task.getDescription());
        assertEquals("2025-12-31", task.getDeadline());
        assertFalse(task.isCompleted());
    }

    @Test
    void testTaskCompletion() {
        Task task = new Task("Titolo", "Descrizione", "2025-12-31");
        task.completeTask();
        assertTrue(task.isCompleted());
    }

    @Test
    void testSetters() {
        Task task = new Task("Old", "OldDesc", "2020");
        task.setTitle("New");
        task.setDescription("NewDesc");
        task.setDeadline("2030");

        assertEquals("New", task.getTitle());
        assertEquals("NewDesc", task.getDescription());
        assertEquals("2030", task.getDeadline());
    }
}
