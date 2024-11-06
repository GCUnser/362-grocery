/**
 * @author Sagnik_Dey
 */
public class Firing {
    private String employeeID;
    public String employeeName;
    public boolean warning;
    public boolean meetingWithManager;
    public boolean isFired;
    public Firing(String employeeID, String employeeName, boolean warning, boolean meetingWithManager, boolean isFired){
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.warning = warning;
        this.meetingWithManager = meetingWithManager;
        this.isFired = isFired;

    }
}
