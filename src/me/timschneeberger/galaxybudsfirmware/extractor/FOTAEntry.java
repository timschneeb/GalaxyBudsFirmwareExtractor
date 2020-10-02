package me.timschneeberger.galaxybudsfirmware.extractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FOTAEntry {
    private static final String TAG = "[FW Entry] ";
    private long mCrc32;
    private long mEntry_pos;
    private long mEntry_size;
    private long mId;

    public FOTAEntry(int i, long count, File file) throws IOException {
        byte[] bArr = new byte[256];
        int i2 = (i * 16) + 12;
        if (file.exists()) {
            FileInputStream fileInputStream2 = new FileInputStream(file);
            try {
                fileInputStream2.read(bArr);
                this.mId = ((((long) bArr[i2 + 3]) & 255) << 24) | ((((long) bArr[i2 + 2]) & 255) << 16) | ((((long) bArr[i2 + 1]) & 255) << 8) | (((long) bArr[i2]) & 255);
                int i3 = i2 + 4;
                this.mCrc32 = ((((long) bArr[i3 + 3]) & 255) << 24) | ((((long) bArr[i3 + 2]) & 255) << 16) | ((((long) bArr[i3 + 1]) & 255) << 8) | (((long) bArr[i3]) & 255);
                int i4 = i3 + 4;
                this.mEntry_pos = ((((long) bArr[i4 + 3]) & 255) << 24) | ((((long) bArr[i4 + 2]) & 255) << 16) | ((((long) bArr[i4 + 1]) & 255) << 8) | (((long) bArr[i4]) & 255);
                int i5 = i4 + 4;
                this.mEntry_size = ((((long) bArr[i5 + 1]) & 255) << 8) | ((((long) bArr[i5 + 3]) & 255) << 24) | ((((long) bArr[i5 + 2]) & 255) << 16) | (((long) bArr[i5]) & 255);

                String box = i < (count - 1) ? "   ├─ " : "   └─ ";
                System.out.println(TAG + box + "ID=" + this.mId + " CRC32=0x" + String.format("%x", this.mCrc32) + " Position=" + this.mEntry_pos + " Size=" + this.mEntry_size);
                fileInputStream2.close();
            } catch (Exception e) {
                e.printStackTrace();
                fileInputStream2.close();
            }
        }
    }

    public long getCrc32() {
        return mCrc32;
    }

    public long getPosition() {
        return mEntry_pos;
    }

    public long getSize() {
        return mEntry_size;
    }

    public long getId() {
        return mId;
    }
}