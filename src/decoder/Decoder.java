package decoder;

import java.util.*;

public class Decoder {
    public static final int GLYPHS_GROUP_LENGTH = 4;

    private int currentGlyphsGroupIndex;
    private final String glyphs;
    private final List<Instruction> instructionList;

    public Decoder(String glyphs) throws SyntaxException {
        this.currentGlyphsGroupIndex = 0;
        this.glyphs = glyphs;
        this.instructionList = new ArrayList<>();

        decode();
    }

    public List<Instruction> getInstructionList() {
        return instructionList;
    }

    private void decode() throws SyntaxException {
        Stack<Integer> braces = new Stack<>();

        while (currentGlyphsGroupIndex < glyphs.length()) {
            int jumpToInstruction = -1;
            int currentInstruction = getCurrentInstructionIndex();
            String currentGlyphsGroup = readCurrentGlyphsGroup();
            Opcodes currentOpcode = translateGlyphsGroupToOpcode(currentGlyphsGroup.chars().mapToObj(c -> (char)c).toArray(Character[]::new));

            switch (currentOpcode) {
                case LBRACE:
                    braces.push(currentInstruction);
                    break;
                case RBRACE:
                    try {
                        jumpToInstruction = braces.pop();
                        instructionList.get(jumpToInstruction).setJumpToIndex(currentInstruction + 1);
                    }
                    catch (EmptyStackException e) {
                        throw new SyntaxException("RBRACE with no match", currentInstruction);
                    }
                    break;
            }

            instructionList.add(new Instruction(currentOpcode, jumpToInstruction));
            currentGlyphsGroupIndex += GLYPHS_GROUP_LENGTH;
        }

        if (!braces.isEmpty()) {
            throw new SyntaxException("LBRACE with no match", braces.peek());
        }
    }

    private int getCurrentInstructionIndex() {
        return currentGlyphsGroupIndex / GLYPHS_GROUP_LENGTH;
    }

    private String readCurrentGlyphsGroup() throws SyntaxException {
        if (glyphs.length() - currentGlyphsGroupIndex < GLYPHS_GROUP_LENGTH) {
            throw new SyntaxException("Invalid instruction length", getCurrentInstructionIndex());
        }

        return glyphs.substring(currentGlyphsGroupIndex, currentGlyphsGroupIndex + GLYPHS_GROUP_LENGTH);
    }

    public static <T> Opcodes translateGlyphsGroupToOpcode(T[] glyphsGroup) throws SyntaxException {
        int value = 0;
        int lastUsedValue = 0;
        int[] powers = {1000, 100, 10, 1};
        HashMap<T, Integer> symbolToDigit = new HashMap<>();

        for (int i = 0; i < GLYPHS_GROUP_LENGTH; ++i) {
            T symbol = glyphsGroup[i];

            if (!symbolToDigit.containsKey(symbol)) {
                symbolToDigit.put(symbol, lastUsedValue++);
            }
        }

        for (int i = 0; i < GLYPHS_GROUP_LENGTH; ++i) {
            T symbol = glyphsGroup[i];

            value += symbolToDigit.get(symbol) * powers[i];
        }

        return Opcodes.getByValue(value);
    }
}
