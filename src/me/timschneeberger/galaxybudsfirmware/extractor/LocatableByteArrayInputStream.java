package me.timschneeberger.galaxybudsfirmware.extractor;

import java.io.ByteArrayInputStream;

public class LocatableByteArrayInputStream extends ByteArrayInputStream {
    public LocatableByteArrayInputStream(byte[] buf) {
        super(buf);
    }

    public int getPosition(){
        return this.pos;
    }
}
