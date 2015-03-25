import java.util.Comparator;

/**
 * The Node to form the Huffman List/Tree
 * @author Frank Hucek
 */
public class HuffmanNode implements Comparator<HuffmanNode>
{
    public Character inChar;
    
    public HuffmanNode left;
    public HuffmanNode right;

    public int frequency;
    public String encoding;
    
    public HuffmanNode(HuffmanNode left, HuffmanNode right)
    {
        this.inChar = null;
        this.left = left;
        this.right = right;
        this.frequency = left.frequency + right.frequency;
        this.encoding = "";
    }
    
    public HuffmanNode(Character inChar, HuffmanNode left, HuffmanNode right)
    {
        this.inChar = inChar;
        this.left = left;
        this.right = right;
        this.frequency = 0;
        this.encoding = "";
    }
    
    // used to build array list
    public HuffmanNode(Character inChar)
    {
        this.inChar = inChar;
        this.left = null;
        this.right = null;
        this.frequency = 0;
        this.encoding = "";
    }
         
    //for sorting java arraylist by frequency
    @Override
    public int compare(HuffmanNode o1, HuffmanNode o2)
    {       
        return o1.frequency - o2.frequency;
    }
}
