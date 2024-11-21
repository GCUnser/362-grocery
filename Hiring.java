/**
 * @author Sagnik_Dey
 */
public class Hiring {
    public String employeeID;
    public String employeeName;
    private String position;
    private double hourlyRate;
    public boolean willHire;
    public Hiring(String employeeID, String employeeName, String position, boolean willHire, double hourlyRate) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.position = position;
        this.willHire = willHire;
        this.hourlyRate = hourlyRate;
    }
}
