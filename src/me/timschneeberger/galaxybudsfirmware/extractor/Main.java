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
        FOTABinary bin = new FOTABinary(inputFile);

        if(!bin.readFirmware())
            exit(1);

        if(!bin.readAudioSegments())
            exit(1);

        String baseName = inputFile.getName().split("[.]")[0];
        String outputPath = inputFile.getAbsoluteFile().getParentFile().getPath() + "/" + baseName + "_out";
        File dir = new File(outputPath);
        if (!dir.exists()){
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
        }

        System.out.println();
        System.out.print("Extracting binary segments into raw firmware image... ");

        File outputRawBinFile = new File(outputPath + "/" + baseName+ ".raw.bin");
        if(!bin.writeRawBinary(outputRawBinFile))
            exit(1);

        System.out.println("Done");

        System.out.print("Extracting audio segments as MP3 files... ");
        if(!bin.writeAudioSegments(outputPath))
            exit(1);

        System.out.println("Done");

        System.out.println();
        System.out.println("Data has been written to '" + outputPath + "'");
    }
}
