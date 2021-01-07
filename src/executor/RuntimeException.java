package executor;

public class RuntimeException extends Exception {
    private final int instructionIndex;

    public RuntimeException(String message, int instructionIndex) {
        super(message);
        this.instructionIndex = instructionIndex;
    }

    public int getInstructionIndex() {
        return instructionIndex;
    }
}
