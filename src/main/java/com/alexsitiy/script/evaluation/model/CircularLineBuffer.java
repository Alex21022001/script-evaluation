package com.alexsitiy.script.evaluation.model;


import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class CircularLineBuffer extends OutputStream {
    private String[] buffer;
    private final ByteArrayOutputStream byteBuffer;
    private final int maxCapacity;
    private int writeIndex;
    private boolean flipped;

    public CircularLineBuffer(int initialCapacity, int maxCapacity) {
        if (initialCapacity > maxCapacity)
            throw new IllegalArgumentException();

        this.byteBuffer = new ByteArrayOutputStream();
        this.buffer = new String[initialCapacity];
        this.maxCapacity = maxCapacity;
        this.writeIndex = 0;
        this.flipped = false;
    }

    @Override
    public synchronized void write(int b) {
        if (b == '\n') {
            flushByteBuffer();
        } else {
            byteBuffer.write(b);
        }
    }

    @Override
    public synchronized void write(byte[] b, int off, int len) {
        String data = new String(b, off, len, StandardCharsets.UTF_8);
        String[] lines = data.split("\n");

        for (String line : lines) {
            writeLine(line);
        }
    }

    @Override
    public void write(byte[] b) {
        write(b, 0, b.length);
    }

    private void writeLine(String line) {
        ensureCapacity();
        buffer[writeIndex] = line;
        writeIndex = (writeIndex + 1) % buffer.length;

        if (writeIndex == 0) {
            flipped = true;
        }
    }

    private void flushByteBuffer() {
        String line = byteBuffer.toString();
        writeLine(line);
        byteBuffer.reset();
    }

    private void ensureCapacity() {
        if (flipped || writeIndex < buffer.length - 1) {
            return;
        }

        int newMaxLines = Math.min(buffer.length * 2, maxCapacity);
        buffer = Arrays.copyOf(buffer, newMaxLines);
    }

    public synchronized String toString() {
        StringBuilder builder = new StringBuilder();
        int index = 0;
        int length = writeIndex;

        if (flipped) {
            index = writeIndex;
            length = buffer.length;
        }

        for (int i = 0; i < length; i++) {
            builder.append(buffer[index]);
            builder.append('\n');
            index = (index + 1) % length;
        }

        return builder.toString();
    }

}
