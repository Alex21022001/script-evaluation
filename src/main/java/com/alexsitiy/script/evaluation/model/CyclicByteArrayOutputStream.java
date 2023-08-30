package com.alexsitiy.script.evaluation.model;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

public class CyclicByteArrayOutputStream extends OutputStream {
    private final ByteArrayOutputStream outputStream;
    private final int maxCapacity;

    public CyclicByteArrayOutputStream(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.outputStream = new ByteArrayOutputStream();
    }


    @Override
    public void write(int b) {
        if (outputStream.size() < maxCapacity) {
            outputStream.write(b);
        } else {
            outputStream.reset();
            outputStream.write(b);
        }
    }

    @Override
    public void write(byte[] b, int off, int len) {
        if (outputStream.size() + len < maxCapacity) {
            outputStream.write(b, off, len);
        } else if (len < maxCapacity) {
            outputStream.reset();
            outputStream.write(b, off, len);
        } else {
            outputStream.reset();
            outputStream.write(b, off, maxCapacity);
        }
    }

    @Override
    public void write(byte[] b) {
        write(b, 0, b.length);
    }

    @Override
    public String toString() {
        return outputStream.toString();
    }
}
