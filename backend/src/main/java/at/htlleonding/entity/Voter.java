package at.htlleonding.entity;

public class Voter {
    //<editor-fold desc="Fields">
    private String studentId; // The ifxxxxxx ids every student has
    private String password; // Hash of the password (should be similar to the one in the school db)
    private boolean hasVoted;
    //</editor-fold>

    //<editor-fold desc="Constructor">
    public Voter(String studentId, String password, boolean hasVoted) {
        setStudentId(studentId);
        setPassword(password);
        setHasVoted(hasVoted);
    }
    //</editor-fold>

    //<editor-fold desc="Getter and Setter">
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
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
