package IO;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/*
RLE (Run-length encoding) Compression
The RLE compression will compress the array with a counter, followed by following character (in our case,
either '0' for open space of '1' for wall'
e.g. [1,1,1,1,1,0,0,0] in RLE compression: [5,1,3,0]

* First 12 cells are reserved for the maze's data, such as start/goal position, number of rows and columns, etc,
  so the maze's data itself will start from cell 13 (array[12]).
 */

public class MyDecompressorInputStream extends InputStream {
    private InputStream in;

    public MyDecompressorInputStream(InputStream in){
        this.in = in;
    }

    // The function recieves a compressed RLE maze and decompresses it into the needed byte[].
    public int deRLE(byte[] b, byte[] target) {
        ArrayList<Byte> tempPlace = new ArrayList<>();

        // Insert maze's basic data into the array (start/goal position, number of rows and columns, etc).
        for (int i = 0; i < 12; i++){
            tempPlace.add(b[i]);
        }

        // Calculate the needed space for the array (12 cells for basic data + (rows * column)).
        // e.g. for a 10 * 10 maze, with need 112 cells.
        int neededLength = 12 + (tempPlace.get(8) + tempPlace.get(9) * 127) * (tempPlace.get(10) + tempPlace.get(11) * 127);

        // The loop will fill the maze itself. Starting for the 13th cell, it will use the [i] cell as the count for
        // the amount, and the [i+1] cell for the type (either '0' or '1').
        for (int i = 12; i < b.length; i+=2){
            int currentloop = unsignedToByteInt(b[i]);
            int currentType = unsignedToByteInt(b[i+1]);
            // Add to the temporary array.
            for (int j = 0; j < currentloop; j++){
                tempPlace.add((byte) currentType);
            }
        }

        // Insert into the correct type byte[].
        for (int i = 0; i < neededLength; i++){
            target[i] = tempPlace.get(i);
        }

        return 0;
    }


    public int unsignedToByteInt(byte number) {
        return Byte.toUnsignedInt(number);
    }

    @Override
    public int read() throws IOException{
        return this.in.read();
    }
    @Override
    public int read(byte[] b) throws IOException, IllegalArgumentException{
        return deRLE(in.readAllBytes(), b);
    }
    @Override
    public void close() throws IOException{
        this.in.close();
        //super.close();
    }

}
