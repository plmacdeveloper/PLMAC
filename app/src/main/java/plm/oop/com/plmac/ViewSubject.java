package plm.oop.com.plmac;
public class ViewSubject {
    private String name;
    private String schedule;
    private String room;
    public ViewSubject(String name, String schedule, String room) {
        this.name = name;
        this.schedule = schedule;
        this.room = room;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSchedule() {
        return schedule;
    }
    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
    public String getRoom() {
        return room;
    }
    public void setRoom(String room) {
        this.room = room;
    }
}