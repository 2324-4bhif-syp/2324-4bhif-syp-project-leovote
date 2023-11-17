package at.htlleonding.entity;

import at.htlleonding.control.HashService;

public class Candidate {
    //<editor-fold desc="Fields">
    private final HashService hashService = new HashService();
    private String id;
    private String schoolId;
    private String firstName;
    private String lastName;
    private String grade;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Candidate(String schoolId, String firstName, String lastName, String grade) {
        setSchoolId(schoolId);
        setFirstName(firstName);
        setLastName(lastName);
        setGrade(grade);
        setId(this.toString());
    }
    //</editor-fold>

    //<editor-fold desc="Getter and Setter">
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = hashService.calculateSHA256Hash(id);
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId.toLowerCase();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade.toUpperCase();
    }

    public String toCSVString() {
        return schoolId + ';' + firstName + ';' + lastName + ';' + grade + "\n";
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    @Override
    public String toString() {
        return "Candidate{" +
                "schoolId='" + schoolId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", grade='" + grade + '\'' +
                '}';
    }
    //</editor-fold>
}
