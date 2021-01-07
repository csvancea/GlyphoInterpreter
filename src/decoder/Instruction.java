package decoder;

public class Instruction {
    private final Opcodes opcode;
    private int param; // atm used only by the BRACE opcodes (points to the next instruction that has to be executed)

    public Instruction(Opcodes opcode) {
        this(opcode, -1);
    }

    public Instruction(Opcodes opcode, int param) {
        this.opcode = opcode;
        this.param = param;
    }

    public Opcodes getOpcode() {
        return opcode;
    }

    public void setJumpToIndex(int index) {
        this.param = index;
    }

    public int getJumpToIndex() {
        return param;
    }
}
