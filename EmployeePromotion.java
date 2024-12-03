public class EmployeePromotion {

    public String employeeID;
    public String employeeName;
    public String managerID;
    public String managerName;

    public EmployeePromotion(String employeeID, String employeeName, String managerID, String managerName) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.managerID = managerID;
        this.managerName = managerName;
    }

    @Override
    public String toString() {
        return employeeName + " (" + employeeID + "); approved by"
                + managerName + " (" + managerID + ")";
    }

}
