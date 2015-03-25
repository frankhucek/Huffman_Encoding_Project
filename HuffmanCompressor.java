import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * The main method to run the Huffman Compressor
 * @author Frank Hucek
 */
public class HuffmanCompressor
{
    public static void main(String[] args)
    {
        if(args.length < 2)
        {
            System.out.println("No input/output file specified.");
        }
        else
        {
            try
            (PrintWriter writer = new PrintWriter("Encodings_and_SpaceSaving.txt")) 
            {
                String s = HuffmanTree.huffmanCoder(args[0], args[1]);
                writer.print(s);
            } 
            catch (FileNotFoundException ex)
            {
		System.out.println("Cannot find file.");
            }
        }
    }
}
