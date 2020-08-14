package prj.corey.jobscheduler.models;

public class ScheduledJob {
    private ScheduledJobType type;
    private String content;

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
