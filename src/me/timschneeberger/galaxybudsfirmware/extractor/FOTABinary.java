package me.timschneeberger.galaxybudsfirmware.extractor;

import java.io.*;
import java.nio.file.FileSystemNotFoundException;
import java.util.ArrayList;

public class FOTABinary {
    private static final long FOTA_BIN_MAGIC = 3405695742L;
    private static final String TAG = "[FW Binary] ";
    private File source;

    private long entry_cnt;
    private FOTAEntry[] fota_entry;
    private long magic;
    private long total_size;

    private Mp3Segment[] audio_segments;

    public FOTABinary(File file){
        if (file.exists()) {
            source = file;
        }
        else {
            System.out.println(TAG + "ERROR: File does not exist");
            throw new FileSystemNotFoundException();
        }
    }

    public boolean readAudioSegments() throws IOException {

        System.out.println("│");
        System.out.println("├─┐  [Audio segments]");

        audio_segments = Utils.toPrimitiveMp3Segment(Mp3Detection.analyse(source));
        return true;
    }

    public boolean writeAudioSegments(String directory) throws IOException {
        boolean success = true;
        for(Mp3Segment segment : audio_segments){
            boolean result = segment.writeFile(directory, source.getName().split("[.]")[0]);
            if(!result)
                success = false;
        }
        return success;
    }

    public boolean readFirmware() throws IOException {
        byte[] bArr = new byte[4];
        FileInputStream fileInputStream = new FileInputStream(source);
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            if (bufferedInputStream.read(bArr) != -1) {
                this.magic = ((((long) bArr[2]) & 255) << 16) | ((((long) bArr[3]) & 255) << 24) | ((((long) bArr[1]) & 255) << 8) | (((long) bArr[0]) & 255);
                if (this.magic != FOTA_BIN_MAGIC) {
                    fileInputStream.close();
                    System.out.println(TAG + "ERROR: Invalid magic number. This is not a valid firmware file.");
                    return false;
                }
            }
            if (bufferedInputStream.read(bArr) != -1) {
                this.total_size = ((((long) bArr[2]) & 255) << 16) | ((((long) bArr[3]) & 255) << 24) | ((((long) bArr[1]) & 255) << 8) | (((long) bArr[0]) & 255);
                if (this.total_size == 0) {
                    fileInputStream.close();
                    System.out.println(TAG + "ERROR: Total firmware size is zero");
                    return false;
                }
            }
            if (bufferedInputStream.read(bArr) != -1) {
                this.entry_cnt = ((((long) bArr[1]) & 255) << 8) | ((((long) bArr[3]) & 255) << 24) | ((((long) bArr[2]) & 255) << 16) | (((long) bArr[0]) & 255);
                if (this.entry_cnt == 0) {
                    fileInputStream.close();
                    System.out.println(TAG + "ERROR: Firmware has no binary segments");
                    return false;
                }
            }

            System.out.println("Firmware archive \"" + source.getName() + "\" Magic=" + String.format("%x", this.magic) + " TotalSize=" + this.total_size);
            System.out.println("│");
            System.out.println("├─┐  [Binary segments] SegmentCount=" + this.entry_cnt);

            this.fota_entry = new FOTAEntry[((int) this.entry_cnt)];
            for (int i = 0; ((long) i) < this.entry_cnt; i++) {
                this.fota_entry[i] = new FOTAEntry(i, this.entry_cnt, source);
            }

            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            fileInputStream.close();
            return false;
        }

        return true;
    }

    public boolean writeRawBinary(File file) throws IOException {
        if (this.fota_entry.length < 1) {
            System.out.println();
            System.out.println(TAG + "ERROR: Empty firmware. Unable to extract data.");
            return false;
        }

        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println();
            System.out.println(TAG + "ERROR: Cannot write output file. There exists already a directory with the same name.");
            return false;
        }

        DataOutputStream os = new DataOutputStream(fileOutputStream);
        FileInputStream sourceInputStream = new FileInputStream(source);
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(sourceInputStream);
            int currentOffset = 0;
            for (FOTAEntry entry : this.fota_entry) {

                int bytesToBeSkipped = (int)(entry.getPosition() - currentOffset);
                bufferedInputStream.readNBytes(bytesToBeSkipped);
                currentOffset += bytesToBeSkipped;

                os.write(bufferedInputStream.readNBytes((int) entry.getSize()));
                currentOffset += entry.getSize();
            }
        } catch (Exception e) {
            e.printStackTrace();
            sourceInputStream.close();
            os.close();
            return false;
        }

        sourceInputStream.close();
        os.close();

        return true;
    }
}