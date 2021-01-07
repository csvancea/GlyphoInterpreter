package decoder;

public enum Opcodes {
    NOP(0, 'n'),
    INPUT(1, 'i'),
    ROT(10, '>'),
    SWAP(11, '\\'),
    PUSH(12, '1'),
    RROT(100, '<'),
    DUP(101, 'd'),
    ADD(102, '+'),
    LBRACE(110, '['),
    OUTPUT(111, 'o'),
    MULTIPLY(112, '*'),
    EXECUTE(120, 'e'),
    NEGATE(121, '-'),
    POP(122, '!'),
    RBRACE(123, ']'),
    NOT_IMPLEMENTED(999, 'X');

    private final int value;
    private final char glyph;

    Opcodes(int value, char glyph) {
        this.value = value;
        this.glyph = glyph;
    }

    public int getValue() {
        return value;
    }

    public char getGlyph() {
        return glyph;
    }

    public static Opcodes getByValue(int value) throws SyntaxException {
        for (Opcodes instr : Opcodes.values()) {
            if (instr.value == value) {
                return instr;
            }
        }

        // shouldn't get here
        return NOT_IMPLEMENTED;
    }
}
