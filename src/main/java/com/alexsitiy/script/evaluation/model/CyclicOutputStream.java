package com.alexsitiy.script.evaluation.model;

import java.io.OutputStream;

public class CyclicOutputStream extends OutputStream {
    private int position = 0;
    private final byte[] buffer;

    public CyclicOutputStream(int initialCapacity) {
        buffer = new byte[initialCapacity];
    }


    @Override
    public synchronized void write(int b) {
        if (position >= buffer.length) {
            position = 0;
        }

        buffer[position++] = (byte) b;
    }

    @Override
    public String toString() {
        return new String(buffer, 0, position);
    }
}
