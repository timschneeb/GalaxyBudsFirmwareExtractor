package me.timschneeberger.galaxybudsfirmware.extractor;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

import static java.lang.Math.floor;
import static java.util.Map.entry;

public class Mp3Detection {
    static final Map<Integer, Integer> bitrates = Map.ofEntries(
            entry(0b0001,  32000),
            entry(0b0010,  40000),
            entry(0b0011,  48000),
            entry(0b0100,  56000),
            entry(0b0101,  64000),
            entry(0b0110,  80000),
            entry(0b0111,  96000),
            entry(0b1000, 112000),
            entry(0b1001, 128000),
            entry(0b1010, 160000),
            entry(0b1011, 192000),
            entry(0b1100, 224000),
            entry(0b1101, 256000),
            entry(0b1110, 320000)
    );

    static final Map<Integer, Integer> samplerates = Map.ofEntries(
            entry(0b00,  44100),
            entry(0b01,  48000),
            entry(0b10,  32000)
    );

    public static ArrayList<Mp3Segment> analyse(File file) throws IOException {
        ArrayList<Mp3Segment> segments = new ArrayList<Mp3Segment>();

        FileInputStream fileInputStream = new FileInputStream(file);
        LocatableByteArrayInputStream stream =
                new LocatableByteArrayInputStream(fileInputStream.readAllBytes());

        boolean isMp3 = false;
        int counter = 0;
        int last_bitrate = 0;
        int last_samplerate = 0;

        ArrayList<Byte> lastMp3Bytes = new ArrayList<Byte>();
        ArrayList<Byte> currentMp3Bytes = new ArrayList<Byte>();
        currentMp3Bytes.add((byte) 0);

        Utils.addBytes(currentMp3Bytes, stream.readNBytes(3));

        /* We are reading three bytes ahead! -> Set current offset to -1*/
        long offset = -1;

        while(true){
            if(!isMp3){
                /* Discard any "audio" data under 1kB */
                if(lastMp3Bytes.size() > 1000){
                    Mp3Segment segment = new Mp3Segment(Utils.toPrimitiveBytes(lastMp3Bytes),
                            offset, counter, last_samplerate, last_bitrate);
                    segments.add(segment);

                    System.out.println("│ ├─ ID=" + segment.getIndex() +
                            "\tOffset=0x" +   String.format("%x", segment.getOffset()) +
                            "\tSize=" + segment.getData().length +
                            "\tBitrate=" + segment.getBitrate() +
                            "\tSamplerate=" + segment.getSamplerate());

                    try (FileOutputStream fos = new FileOutputStream(inputFile.getAbsoluteFile().getParentFile().getPath() + "/out_" + segment.getIndex() + ".mp3")) {
                        fos.write(Utils.toPrimitiveBytes(lastMp3Bytes));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    counter++;
                }
                lastMp3Bytes.clear();
            }

            currentMp3Bytes.remove(0);
            int streamByte = stream.read();

            if(streamByte == -1){
                break;
            }

            currentMp3Bytes.add((byte)streamByte);

            int header = ((currentMp3Bytes.get(3) & 0xFF)) |
                         ((currentMp3Bytes.get(2) & 0x00FF) <<  8) |
                         ((currentMp3Bytes.get(1) & 0x00FF) << 16) |
                         ((currentMp3Bytes.get(0) & 0x00FF) << 24);

            // Frame sync: 0b11111111 111xxxxx xxxxxxxx xxxxxxxx
            if ((header & 0xFFE00000) != 0xFFE00000) {
                isMp3 = false;
                continue;
            }

            // MPEG version: 0bxxxxxxxx xxxVVxxx xxxxxxxx xxxxxxxx
            int mpeg_version = (header & 0x00180000) >>> 19;
            if (mpeg_version != 0b11) {
                // reserved value or not MPEG1
                isMp3 = false;
                continue;
            }

            // MPEG Layer: 0bxxxxxxxx xxxxxLLx xxxxxxxx xxxxxxxx
            int mpeg_layer = (header & 0x00060000) >>> 17;
            if (mpeg_layer != 0b01) {
                // reserved value or not Layer 3
                isMp3 = false;
                continue;
            }

            // Bitrate: 0bxxxxxxxx xxxxxxxx BBBBxxxx xxxxxxxx
            int bit_rate_index = (header & 0x0000F000) >>> 12;
            if (bit_rate_index == 0b0000 || bit_rate_index == 0b1111) {
                // weird bitrate or reserved value
                isMp3 = false;
                continue;
            }
            last_bitrate = bitrates.get(bit_rate_index);

            // sampling rate: 0bxxxxxxxx xxxxxxxx xxxxRRxx xxxxxxxx
            int sampling_rate_index = (header & 0x00000C00) >>> 10;
            if (sampling_rate_index == 0b11) {
                // reserved value
                isMp3 = false;
                continue;
            }
            last_samplerate = samplerates.get(sampling_rate_index);

            // padding? 0bxxxxxxxx xxxxxxxx xxxxxxPx xxxxxxxx
            boolean has_padding = (((header & 0x00000200) >>> 9) == 0b1);


            // emphasis: 0bxxxxxxxx xxxxxxxx xxxxxxxx xxxxxxEE
            int emphasis = (header & 0x00000003);
            if (emphasis == 0b10) {
                // reserved value
                isMp3 = false;
                continue;
            }

            // calculate the frame length
            int frame_length = (int) floor(144.f * last_bitrate / last_samplerate);
            if (has_padding)
                frame_length++;

            if(!isMp3){
                offset = stream.getPosition();
            }

            lastMp3Bytes.addAll(currentMp3Bytes);
            Utils.addBytes(lastMp3Bytes, stream.readNBytes(frame_length - 4));

            currentMp3Bytes.clear();
            currentMp3Bytes.add((byte) 0b0);
            Utils.addBytes(currentMp3Bytes, stream.readNBytes(3));

            isMp3 = true;
        }

        if(segments.size() < 1){
            System.out.println("│ └─ No MP3 audio segments found");
        }
        else {
            System.out.println("│ └─ [EOF] SegmentCount=" + segments.size());
        }

        stream.close();
        fileInputStream.close();

        return segments;
    }
}
