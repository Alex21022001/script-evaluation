package com.alexsitiy.script.evaluation.model;


import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * This class is an implementation of {@link OutputStream} that limits
 * the max capacity of the lines. It also has initial capacity that will be increased
 * if needed until the max capacity is reached. If the buffer is full - it starts to
 * overwrite the oldest lines.
 */
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

    /**
     * Writes byte to inner byteBuffer that collects them until \n
     * is received, in this case flushByteBuffer() is invoked and the collected bytes
     * are turned into a line and stores in the buffer.
     *
     * @param b byte to written
     */
    @Override
    public synchronized void write(int b) {
        if (b == '\n') {
            flushByteBuffer();
        } else {
            byteBuffer.write(b);
        }
    }

    /**
     * Writes the array of bytes according to the given {@code off} and {@code len}.
     * Turns the byte array into {@link String} and split it into lines in accordance
     * with \n character. Afterwards stores the given lines in the buffer.
     *
     * @param b   the data.
     * @param off the start offset in the data.
     * @param len the number of bytes to write.
     */
    @Override
    public synchronized void write(byte[] b, int off, int len) {
        String data = new String(b, off, len, StandardCharsets.UTF_8);
        String[] lines = data.split("\n");

        for (String line : lines) {
            writeLine(line);
        }
    }

    /**
     * Delegates the method to {@code write(byte[],int,int)}.
     */
    @Override
    public void write(byte[] b) {
        write(b, 0, b.length);
    }

    /**
     * Flushes the byteBuffer.
     * */
    @Override
    public void close() {
        flushByteBuffer();
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
        if (byteBuffer.size() != 0) {
            String line = byteBuffer.toString();
            writeLine(line);
            byteBuffer.reset();
        }
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
