package at.htlleonding.entity;

public enum ElectionType {
    CROSSES("Crosses Election"),        // Custom string representation
    MULTIVALUE("Multi-Value Election"); // Custom string representation

    private final String displayName;

    // Constructor
    ElectionType(String displayName) {
        this.displayName = displayName;
    }

    // Override the toString method to return a more user-friendly name
    @Override
    public String toString() {
        return displayName;
    }

    // Static method to convert a string back to an enum value
    public static ElectionType fromString(String text) {
        for (ElectionType type : ElectionType.values()) {
            if (type.displayName.equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enum constant for " + text);
    }
}
