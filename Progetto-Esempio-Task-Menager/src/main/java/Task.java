public class Task {

    private String title;
    private String description;
    private String deadline;
    private boolean completed;

    public Task(String title, String description, String deadline) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.completed = false;
    }

    public String getTitle() {
        return title;

    }

    public String getDescription() {
        return description;

    }

    public String getDeadline() {
        return deadline;

    }

    public boolean isCompleted() {
        return completed;

    }
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public void completeTask() {
        this.completed = true;
    }
}
