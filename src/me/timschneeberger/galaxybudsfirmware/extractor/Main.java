package me.timschneeberger.galaxybudsfirmware.extractor;

import java.io.File;
import java.io.IOException;

import static java.lang.System.exit;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println();

        if(args.length < 1){
            System.out.println("Missing argument. Please specify a firmware binary file.");
            return;
        }

        System.out.println("Analysing binary...");
        System.out.println();

        File inputFile = new File(args[0]);
        FOTABinary bin = new FOTABinary();
        boolean read_success = bin.readFirmware(inputFile);
        if(!read_success)
            exit(1);

        System.out.println();
        System.out.println("Extracting into raw firmware image...");
        System.out.println();

        File outputFile = new File(inputFile.getParentFile().getPath() + "/" + inputFile.getName().split("[.]")[0] + ".raw.bin");
        boolean write_success = bin.writeRawBinary(outputFile);
        if(!write_success)
            exit(1);

        System.out.println();
        System.out.println("Raw firmware image has been extracted to '" + outputFile.toString() + "'.\n" +
                "You can view it in a disassembler.");
    }
}
