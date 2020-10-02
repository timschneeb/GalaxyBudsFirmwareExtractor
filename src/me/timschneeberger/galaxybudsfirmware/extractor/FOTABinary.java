package me.timschneeberger.galaxybudsfirmware.extractor;

import java.io.*;

public class FOTABinary {
    private static final long FOTA_BIN_MAGIC = 3405695742L;
    private static final String TAG = "[FW Binary] ";
    private File source;
    private long entry_cnt;
    private FOTAEntry[] fota_entry;
    private long magic;
    private long total_size;

    public boolean readFirmware(File file) throws IOException {
        byte[] bArr = new byte[4];
        source = file;
        if (file.exists()) {
            FileInputStream fileInputStream = new FileInputStream(file);
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
                        System.out.println(TAG + "ERROR: Firmware has no data segments");
                        return false;
                    }
                }
                System.out.println(TAG + "└─┐ \"" + file.getName() + "\" Magic=" + String.format("%x", this.magic) + " TotalSize=" + this.total_size + " EntryCount=" + this.entry_cnt);

                this.fota_entry = new FOTAEntry[((int) this.entry_cnt)];
                for (int i = 0; ((long) i) < this.entry_cnt; i++) {
                    this.fota_entry[i] = new FOTAEntry(i, this.entry_cnt, file);
                }
                fileInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                fileInputStream.close();
                return false;
            }
        } else {
            System.out.println(TAG + "ERROR: File does not exist");
            return false;
        }
        return true;
    }

    public boolean writeRawBinary(File file) throws IOException {
        if (this.fota_entry.length < 1) {
            System.out.println(TAG + "ERROR: Empty firmware. Unable to extract data.");
            return false;
        }

        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println(TAG + "ERROR: Cannot write output file. There exists already a directory with the same name.");
            return false;
        }

        DataOutputStream os = new DataOutputStream(fileOutputStream);
        FileInputStream sourceInputStream = new FileInputStream(source);
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(sourceInputStream);
            int currentOffset = 0;
            for (FOTAEntry entry : this.fota_entry) {
                System.out.println(TAG + "Extracting segment #" + entry.getId() +
                        " from offset 0x" + String.format("%x", entry.getPosition()) +
                        " to 0x" + String.format("%x", entry.getPosition() + entry.getSize()));

                int bytesToBeSkipped = (int)(entry.getPosition() - currentOffset);
                bufferedInputStream.readNBytes(bytesToBeSkipped);
                currentOffset += bytesToBeSkipped;

                os.write(bufferedInputStream.readNBytes((int) entry.getSize()));
                currentOffset += entry.getSize();
            }
        } catch (Exception e) {
            e.printStackTrace();
            sourceInputStream.close();
            return false;
        }

        sourceInputStream.close();
        os.close();

        return true;
    }
}