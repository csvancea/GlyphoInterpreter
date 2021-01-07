package decoder;

import java.util.*;

public class Decoder {
    private static final int GLYPHS_GROUP_LENGTH = 4;

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
            Opcodes currentOpcode = translateGlyphsGroupToOpcode(currentGlyphsGroup);

            switch (currentOpcode) {
                case LBRACE:
                    braces.push(currentInstruction);
                    break;
                case RBRACE:
                    try {
                        jumpToInstruction = braces.pop();
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

    static private Opcodes translateGlyphsGroupToOpcode(String glyphsGroup) throws SyntaxException {
        int value = 0;
        int lastUsedValue = 0;
        int[] powers = {1000, 100, 10, 1};
        HashMap<Character, Integer> symbolToDigit = new HashMap<>();

        for (int i = 0; i < GLYPHS_GROUP_LENGTH; ++i) {
            char ch = glyphsGroup.charAt(i);

            if (!symbolToDigit.containsKey(ch)) {
                symbolToDigit.put(ch, lastUsedValue++);
            }
        }

        for (int i = 0; i < GLYPHS_GROUP_LENGTH; ++i) {
            char ch = glyphsGroup.charAt(i);

            value += symbolToDigit.get(ch) * powers[i];
        }

        return Opcodes.getByValue(value);
    }
}
