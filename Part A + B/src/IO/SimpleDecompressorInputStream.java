package IO;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SimpleDecompressorInputStream extends InputStream {
    private InputStream in;
    /*
    This function reads a simple compressed maze and decompresses it.
    e.g. [5,3,1,1] to [0,0,0,0,0,1,1,1,0,1]
     */
    public SimpleDecompressorInputStream(InputStream in){
        this.in = in;
    }

    public int decompress(byte[] b, byte[] answerArray) {
        // Define needed variables
        ArrayList<Byte> tempPlace = new ArrayList<>();
        byte currentByte = (byte) 0;
        // Decompress basic maze's data
        for (int i = 0; i < 12; i++){
            tempPlace.add(b[i]);
        }
        // Decompress cell's data
        for (int i = 12; i < b.length; i++){
            int currentInt = unsignedToByteInt(b[i]);
            for (int j = 0; j < currentInt; j++){
                tempPlace.add(currentByte);
            }
            // if '0' change to '1' if '1' change to '0'
            if (currentByte == (byte) 0){
                currentByte = (byte) 1;
            }
            else {
                currentByte = (byte) 0;
            }
        }
        // Convert array to byte[]
        for (int i = 0; i < 12 + (tempPlace.get(8) + tempPlace.get(9) * 127) * (tempPlace.get(10) + tempPlace.get(11) * 127); i++){
            answerArray[i] = tempPlace.get(i);
        }
        return 0;
    }

    public int unsignedToByteInt(byte num) {
        return Byte.toUnsignedInt(num);
    }

    @Override
    public int read() throws IOException{
        return this.in.read();
    }
    @Override
    public int read(byte[] b) throws IOException, IllegalArgumentException{
        return decompress(in.readAllBytes(), b);
    }
    @Override
    public void close() throws IOException{
        this.in.close();
        super.close();
    }

}
