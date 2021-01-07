package executor;

import decoder.Decoder;
import decoder.Instruction;
import decoder.Opcodes;
import decoder.SyntaxException;

import java.math.BigInteger;
import java.util.*;

public class Executor {
    private final Scanner scanner;
    private final Deque<BigInteger> cpuStack;
    private final List<Instruction> instructionList;
    private int instructionPointer;
    private final int radix;

    public Executor(List<Instruction> instructionList, int radix) throws RuntimeException {
        this.scanner = new Scanner(System.in);
        this.cpuStack = new LinkedList<>();
        this.instructionList = instructionList;
        this.instructionPointer = 0;
        this.radix = radix;

        run();
    }

    private void run() throws RuntimeException {
        while (instructionPointer < instructionList.size()) {
            Instruction instruction = instructionList.get(instructionPointer);

            try {
                executeSingleInstruction(instruction, false);
            } catch (InputMismatchException e) {
                throw new RuntimeException("Can't parse input number", instructionPointer);
            } catch (NoSuchElementException e) {
                throw new RuntimeException("Not enough stack elements", instructionPointer);
            }
        }
    }

    private void executeSingleInstruction(Instruction instruction, boolean dynamicExecution) throws RuntimeException {
        int nextInstruction = instructionPointer + 1;

        switch (instruction.getOpcode()) {
            case NOP:
                break;
            case INPUT:
                cpuStack.push(scanner.nextBigInteger(radix));
                break;
            case ROT:
                cpuStack.addLast(cpuStack.pop());
                break;
            case SWAP:
                BigInteger last = cpuStack.pop();
                BigInteger secondLast = cpuStack.pop();

                cpuStack.push(last);
                cpuStack.push(secondLast);
                break;
            case PUSH:
                cpuStack.push(BigInteger.ONE);
                break;
            case RROT:
                cpuStack.push(cpuStack.removeLast());
                break;
            case DUP:
                cpuStack.push(cpuStack.getFirst());
                break;
            case ADD:
                cpuStack.push(cpuStack.pop().add(cpuStack.pop()));
                break;
            case LBRACE:
                if (cpuStack.getFirst().equals(BigInteger.ZERO)) {
                    nextInstruction = instruction.getJumpToIndex();
                }
                break;
            case OUTPUT:
                System.out.println(cpuStack.pop().toString(radix).toUpperCase());
                break;
            case MULTIPLY:
                cpuStack.push(cpuStack.pop().multiply(cpuStack.pop()));
                break;
            case EXECUTE:
                BigInteger[] symbols = new BigInteger[Decoder.GLYPHS_GROUP_LENGTH];
                for (int i = 0; i < Decoder.GLYPHS_GROUP_LENGTH; ++i) {
                    symbols[i] = cpuStack.pop();
                }

                try {
                    Opcodes opcode = Decoder.translateGlyphsGroupToOpcode(symbols);
                    Instruction executeInstruction = new Instruction(opcode);

                    if (opcode == Opcodes.LBRACE || opcode == Opcodes.RBRACE) {
                        throw new RuntimeException("EXECUTE *BRACE instruction", instructionPointer);
                    }

                    executeSingleInstruction(executeInstruction, true);
                } catch (SyntaxException e) {
                    throw new RuntimeException("EXECUTE unknown instruction", instructionPointer);
                }
                break;
            case NEGATE:
                cpuStack.push(cpuStack.pop().negate());
                break;
            case POP:
                cpuStack.pop();
                break;
            case RBRACE:
                nextInstruction = instruction.getJumpToIndex();
                break;
            default:
                throw new RuntimeException("Unknown instruction", instructionPointer);
        }

        if (!dynamicExecution) {
            instructionPointer = nextInstruction;
        }
    }
}
