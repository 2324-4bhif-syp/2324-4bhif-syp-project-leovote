package at.htlleonding.entity;

public class Voter {
    //<editor-fold desc="Fields">
    private String schoolId; // The ifxxxxxx ids every student has
    private String password; // Hash of the password (should be similar to the one in the school db)
    private boolean hasVoted;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Voter(String studentId, String password, boolean hasVoted) {
        setSchoolId(studentId);
        setPassword(password);
        setHasVoted(hasVoted);
    }
    //</editor-fold>

    //<editor-fold desc="Getter and Setter">
    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }
    //</editor-fold>
}
