package me.timschneeberger.galaxybudsfirmware.extractor;

import java.util.ArrayList;

public class Utils {
    static void addBytes(ArrayList<Byte> array, byte[] bytes){
        for(byte b : bytes){
            array.add(b);
        }
    }
    static byte[] toPrimitiveBytes(ArrayList<Byte> array){
        byte[] bytes = new byte[array.size()];
        for(int i = 0; i < array.size(); i++){
            bytes[i] = array.get(i);
        }
        return bytes;
    }
    static Mp3Segment[] toPrimitiveMp3Segment(ArrayList<Mp3Segment> array){
        Mp3Segment[] bytes = new Mp3Segment[array.size()];
        for(int i = 0; i < array.size(); i++){
            bytes[i] = array.get(i);
        }
        return bytes;
    }
}
