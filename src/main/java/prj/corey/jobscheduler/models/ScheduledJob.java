package prj.corey.jobscheduler.models;

public class ScheduledJob {
    private String name;
    private ScheduledJobType type;
    private String content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ScheduledJobType getType() {
        return type;
    }

    public void setType(ScheduledJobType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
