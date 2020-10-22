package me.timschneeberger.galaxybudsfirmware.extractor;

import java.io.*;

public class Mp3Segment {
    private final String TAG = "[Mp3Segment] ";

    private final byte[] data;
    private final long offset;
    private final long index;
    private final int samplerate;
    private final int bitrate;

    public Mp3Segment(byte[] data, long offset, long index, int samplerate, int bitrate){
        this.data = data;
        this.offset = offset;
        this.index = index;
        this.samplerate = samplerate;
        this.bitrate = bitrate;
    }

    public byte[] getData() {
        return data;
    }

    public long getIndex() {
        return index;
    }

    public long getOffset() {
        return offset;
    }

    public int getBitrate() {
        return bitrate;
    }

    public int getSamplerate() {
        return samplerate;
    }

    public boolean writeFile(String directory, String prefix) throws IOException{
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(directory + "/" + prefix + "_" + String.format("0x%x", getOffset()) + ".mp3");
        } catch (FileNotFoundException e) {
            System.out.println();
            System.out.println(TAG + "ERROR: Cannot write output file. There exists already a directory with the same name.");
            return false;
        }

        DataOutputStream os = new DataOutputStream(fileOutputStream);
        try {
            os.write(data);
        } catch (Exception e) {
            e.printStackTrace();
            os.close();
            return false;
        }
        os.close();
        return true;
    }
}
