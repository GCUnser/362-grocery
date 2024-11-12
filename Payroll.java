/**
 * @author Gabriel_Unser
 */
public class Payroll {
    public String employeeID;
    public String employeeName;
    private int hoursWorked;
    private double hourlyRate;

    public Payroll(String employeeID, String employeeName, String position, boolean willHire, double hourlyRate) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.hoursWorked = hoursWorked;
        this.hourlyRate = hourlyRate;
    }

    public double calculateWage() {
        return this.hoursWorked * this.hourlyRate;
    }
}
