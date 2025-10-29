package IO;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class SimpleCompressorOutputStream extends OutputStream{
    private OutputStream out;

    public SimpleCompressorOutputStream(OutputStream out){
        this.out = out;
    }

    //TODO: seperate functions

    @Override
    public void write(int b) throws IOException {
        this.out.write(b);
    }
    /*
    This function compresses a maze by counting the '0' and '1' (starting from '0').
    It compresses a 1d array of byte[], and returns the count for each number, from '0' to '1'.
    e.g. [0,0,0,0,0,1,1,1,0,1] to: [5,3,1,1]
     */
    @Override
    public void write(byte[] b) throws IOException {
        // Define variables to use
        ArrayList<Byte> compressedMaze = new ArrayList<>();
        int counter = 0;
        int currentCount = 0;
        // First 12 cells to contain the basic maze's data
        for (int i = 0; i < 12; i++) {
            compressedMaze.add(b[i]);
        }
        // Compress the rest of the maze's data (the cells themeselves).
        // if '2' (start position) or '3' (goal position), count it as '0', a free space.
        for (int i = 12; i < b.length; i++) {
            int currentMazeBlock = (int) b[i];
            if (currentMazeBlock == 2 || currentMazeBlock == 3) {
                currentMazeBlock = 0;
            }
            // If currentCount matches the currentBlock, add 1 to counter.
            if (currentMazeBlock == currentCount) {
                counter++;
                // Else if needs to append to array
            } else {
                // If counter is greater than 255
                while (counter >= 255) {
                    compressedMaze.add(intToByteUnsigned(255));
                    compressedMaze.add((byte) 0);
                    counter -= 255;
                }
                // If counter is below 255
                if (counter > 0) {
                    compressedMaze.add(intToByteUnsigned(counter));
                }
                // Chance current type of count from '0' to '1' or from '1' to '0'
                if (currentCount == 0) {
                    currentCount = 1;
                } else {
                    currentCount = 0;
                }
                // Set current counter
                counter = 1;
            }
        }
        // If counter is greater than 255
        while (counter >= 255) {
            compressedMaze.add(intToByteUnsigned(255));
            compressedMaze.add((byte) 0);
            counter -= 255;
        }
        // If counter is below 255
        if (counter > 0) {
            compressedMaze.add(intToByteUnsigned(counter));
        }
        // Convert array to byte[]
        byte[] compressedMazeReturn = new byte[compressedMaze.size()];
        for (int i = 0; i < compressedMazeReturn.length; i++) {
            compressedMazeReturn[i] = compressedMaze.get(i);
        }
        this.out.write(compressedMazeReturn);
    }

    public byte intToByteUnsigned(int num) {
        if (num < 0 || num > 255) {
            throw new IllegalArgumentException("Not in correct range 0-255");
        }
        return (byte) (num & 0xFF);
    }


    @Override
    public void flush() throws IOException{
        this.out.flush();
        super.flush();
    }

    @Override
    public void close()throws IOException{
        this.out.close();
        super.close();
    }

}
