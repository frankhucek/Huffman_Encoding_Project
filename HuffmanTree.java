import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * The Huffman Tree
 * Compress a file based on a generated Huffman Tree
 * @author Frank Hucek
 */
public class HuffmanTree
{
    /**
     * The array list containing the HuffmanNodes
     * I used an array list here so that I could easily
     * associate characters with their index in the array
     * I also have immediate access to any element in the list
     * based on the index.
     */
    private final ArrayList<HuffmanNode> alist;
    
    /**
     * The array list sorted by frequencies of characters
     * I used an array list here so that I have immediate access to any
     * element within the list at a specific index.
     */
    private ArrayList<HuffmanNode> frequencyList;
    
    /**
     * Initializes the ArrayList and 
     * creates a new instance of HuffmanNode at each index with 
     * corresponding inChar to the index
     * @param inputFile
     */
    public HuffmanTree(String inputFile)
    {
        // ascii character range 0-255
        this.alist = new ArrayList<>(256);
        this.frequencyList = new ArrayList<>(256);
        
        // ASCii characters 0-32 are unprintable, but whatever
        for(int i = 0; i < 256; i++)
        {
            this.alist.add(i,new HuffmanNode((char)i));
            //this.frequencyList.add(i,alist.get(i));
        }
        try
        {
            this.makeList(new File(inputFile));
        }
        catch(IOException e)
        {   
            System.out.println("Failed to create a list. IOException.");
        }
    }
    
    /**
     * Reads each char in file and adds that chars frequency to the ArrayList
     * Copies elements from alist to frequencyList and sorts 
     * frequencyList based on character frequency
     * @param fileToRead
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void makeList(File fileToRead) throws FileNotFoundException, IOException
    {   
        FileReader file = new FileReader(fileToRead); // throws FileNotFoundException
        int i;
        while((i = file.read()) >= 0) // read() throws IOException
        {
            if(i < 256)
            {
                this.alist.get(i).frequency++;
            }
        }
        
        for(HuffmanNode node : alist)
        {
            this.frequencyList.add(node);
        }
        frequencyList.sort((HuffmanNode o1, HuffmanNode o2) -> 
                o1.frequency - o2.frequency); 
    }
    
    /**
     * Builds a Huffman Tree and returns the root of the tree
     * Only leaves of the tree will have a non-null Character value
     * @param list The array list of HuffmanNodes to build the tree from
     * @return The root of the Huffman Tree
     */
    public HuffmanNode buildTree(ArrayList<HuffmanNode> list) 
    {
        while(list.size() > 1)
        {
            if(list.get(0).frequency == 0)
            {
                list.remove(0);
            }
            else
            {
                HuffmanNode left = list.remove(0);
                HuffmanNode right = list.remove(0);
                HuffmanNode root = new HuffmanNode(left,right);
                list.add(list.size(),root);
            }
        }
        return list.remove(0);
    }
    
    /**
     * Traverses the Huffman Tree from the specified root 
     * and encodes each of its nodes.
     * @param root The root to start the encoding
     */
    public void traverseAndEncode(HuffmanNode root)
    {
        if(root.left == null && root.right == null)
            return;
        if(root.left != null)
        {
            root.left.encoding = root.encoding + "0";
            traverseAndEncode(root.left);
        }
        if(root.right != null)
        {
            root.right.encoding = root.encoding + "1";
            traverseAndEncode(root.right);
        }            
    }
    
    /**
     * This method will compress the input file using a generated Huffman tree
     * and place the encoded data in the output file
     * @param inputFileName The input file name
     * @param outputFileName The output file name
     * @return The string containing the table of encodings and space savings
     */
    public static String huffmanCoder(String inputFileName, String outputFileName)
    {
        HuffmanTree huffTree = new HuffmanTree(inputFileName);
        HuffmanNode root = huffTree.buildTree(huffTree.frequencyList);
        huffTree.traverseAndEncode(root); 
        // all nodes now have their proper encoding, 
        // so can refer to alist.get((int)character).encoding
        
        String str = null;
        
        try
        {
            PrintWriter writer = new PrintWriter(outputFileName);
            FileReader reader = new FileReader(inputFileName);
            
            StringBuilder input = new StringBuilder();
            StringBuilder output = new StringBuilder();
            
            int i;
            while((i = reader.read()) != -1)
            {
                if(i < 256)
                {
                    writer.print(huffTree.alist.get(i).encoding);
                    input.append((char)i);
                    output.append(huffTree.alist.get(i).encoding);
                }
            }
            for(int j = 0; j < huffTree.alist.size(); j++)
            {
                if(huffTree.alist.get(j).frequency != 0)
                {
                    str = str + "Letter: " + huffTree.alist.get(j).inChar
                            + " -> " + huffTree.alist.get(j).frequency
                            + " -> " + huffTree.alist.get(j).encoding + "\n";
                }
            }
            int inBits, outBits;
            inBits = input.length() * 16;
            outBits = output.length();
            double ratio = (double)(inBits)/(double)(outBits);
            str = str + "\nThe input file contained " + inBits + " bits.\n" 
                    + "The output file contained " + outBits + " bits*.\n"
                    + "The encoded output file is " + ratio 
                    + " times smaller than the original.\n"
                    + "* This would be the amount of bits the output file "
                    + "would be if it were represented in true binary.\n";
            return str;
        } 
        catch (IOException ex)
        {
            System.out.println("IOException. Cannot create/read file.");
        }
        
        return str;
    }
}
