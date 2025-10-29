package IO;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/*
RLE (Run-length encoding) Compression
The RLE compression will compress the array with a counter, followed by following character (in our case,
either '0' for open space of '1' for wall'
e.g. [1,1,1,1,1,0,0,0] in RLE compression: [5,1,3,0]

* First 12 cells are reserved for the maze's data, such as start/goal position, number of rows and columns, etc,
  so the maze's data itself will start from cell 13 (array[12]).
 */

public class MyCompressorOutputStream extends OutputStream{
    private OutputStream out;

    public MyCompressorOutputStream(OutputStream out){
        this.out = out;
    }

    @Override
    public void write(int b) throws IOException {
        this.out.write(b);
    }

    // The compress function recieves a maze in a byte[] type and compresses it into an RLE compression.
    public byte[] RLE(byte[] b){
        ArrayList<Byte> compressedMaze = new ArrayList<>();
        int counter = 0; // Used to count the number of times a type of '0' or '1' appears
        int currentType = 0; // The current type ('0' or '1')

        // Compress the first 12 cells containing the maze's basic data (start/goal position, number of rows and columns, etc).
        for (int i = 0; i < 12; i++){
            compressedMaze.add(b[i]);
        }

        // Compress the maze's data cells themeselves.
        for (int i = 12; i < b.length; i++){
            int currentMazeBlock = (int) b[i];
            // If current block is '2' (start position) or '3' (goal position), count them as '0' since they are free.
            // The maze constructor for byte[] will handle it using the maze's basic data from the first 12 cells.
            if (currentMazeBlock == 2 || currentMazeBlock == 3){
                currentMazeBlock = 0;
            }
            // If the current block is the same as the currentType, keep counting it's amount.
            if (currentMazeBlock == currentType){
                counter++;
            }
            // If it's different, add it to the array.
            else {
                addToArrayOfBytes(compressedMaze, counter, currentType);
                // Change the current type '0' or '1' to the other type.
                if (currentType == 0) {
                    currentType = 1;
                } else {
                    currentType = 0;
                }
                counter = 1;
            }
        }
        // Add last one
        addToArrayOfBytes(compressedMaze, counter, currentType);

        // Convert the array into a byte[].
        byte[] compressedMazeReturn = new byte[compressedMaze.size()];
        for (int i = 0; i < compressedMazeReturn.length; i++){
            compressedMazeReturn[i] = compressedMaze.get(i);
        }

        // Return byte[].
        return compressedMazeReturn;
    }

    // This function recieves an array, and 2 ints: (1) indicating the amount, (2) indicating the type '0' or '1'
    // It than adds them into the array, where the amount comes before the type, according to the RLE compression.
    public void addToArrayOfBytes(ArrayList<Byte> byteArray, int currentAmount, int currentType) {
        // If the currentAmount is >= 255, it cannot be counted as such, add it, and "reset" for the counter.
        while (currentAmount >= 255) {
            byteArray.add(intToByteUnsigned(255));
            byteArray.add(intToByteUnsigned(currentType));
            byteArray.add((byte) 0);
            // Add the different type
            if (currentType == 0){
                byteArray.add((byte) 1);
            }
            else {
                byteArray.add((byte) 0);
            }
            currentAmount -= 255;
        }
        // Add to array regulary
        if (currentAmount > 0) {
            byteArray.add(intToByteUnsigned(currentAmount));
            byteArray.add(intToByteUnsigned(currentType));
        }
    }

    public byte intToByteUnsigned(int num) {
        if (num < 0 || num > 255) {
            throw new IllegalArgumentException("not in range 0-255");
        }
        return (byte) (num & 0xFF);
    }


    @Override
    public void write(byte[] b) throws IOException{
        this.out.write(RLE(b));
    }

    @Override
    public void flush() throws IOException{
        this.out.flush();
        //super.flush();
    }

    @Override
    public void close()throws IOException{
        this.out.close();
        //super.close();
    }

}
