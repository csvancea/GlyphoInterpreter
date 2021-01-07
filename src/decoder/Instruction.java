package decoder;

public class Instruction {
    private final Opcodes opcode;
    private final int param; // atm used only by the RBRACE opcode (points to the corresponding LBRACE)

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

    public int getJumpToIndex() {
        assert(opcode == Opcodes.RBRACE);
        return param;
    }
}
