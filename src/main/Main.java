package main;

import decoder.Decoder;
import decoder.SyntaxException;

import executor.Executor;
import executor.RuntimeException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    private static final int EXIT_CODE_SYNTAX = -1;
    private static final int EXIT_CODE_RUNTIME = -2;

    public static void main(String[] args) {
        Decoder decoder;
        Executor executor;
        int radix = 10;

        if (args.length < 1) {
            System.err.println("No gly file given.");
            return;
        }

        if (args.length >= 2) {
            radix = Integer.parseInt(args[1]);
        }

        try {
            String fileContent = Files.readString(Paths.get(args[0]), StandardCharsets.US_ASCII);
            decoder = new Decoder(fileContent);
            executor = new Executor(decoder.getInstructionList(), radix);
        } catch (SyntaxException e) {
            System.err.println("Error:" + e.getInstructionIndex());
            System.exit(EXIT_CODE_SYNTAX);
        } catch (RuntimeException e) {
            System.err.println("Exception:" + e.getInstructionIndex());
            System.exit(EXIT_CODE_RUNTIME);
        }
        catch (IOException e) {
            System.err.println("Can't open gly file: " + args[0]);
        }
    }
}
