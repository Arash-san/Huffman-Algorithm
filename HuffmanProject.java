import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.awt.*;
class Huffman {
    char chr;
    int freq;
    Huffman left, right;
}

class HuffmanComparator implements Comparator<Huffman> {
    public int compare(Huffman a, Huffman b) {
        return a.freq - b.freq;
    }
}

public class HuffmanProject {
    static String InputFile;
    static String HuffmanTableFile;
    static String HuffmanCodeFile;
    static char[] finalChar;
    static String[] finalCode;
    static int num = 0;

    public static void printCode(Huffman root, String s) {
        if (root.left == null && root.right == null && root.chr != '#') {
            finalChar[num] = root.chr;
            finalCode[num++] = s;
            // System.out.println(root.chr+": "+s);
            return;
        }
        printCode(root.left, s + "0");
        try {
            printCode(root.right, s + "1");
        } catch (Exception e) {

        }

    }

    public static void main(String[] args) throws Exception {
        while (true) {
            System.out.println("");
            System.out.println("Select a task:");
            System.out.println("\t1. Compressing a file with huffman algorithm ");
            System.out.println("\t2. Decompressing huffman files");
            Scanner input = new Scanner(System.in);
            int selection = input.nextInt();
            if (selection == 1)
                Compress();
            if (selection == 2)
                Decompress();
        }

    }
    static void Compress() throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the address of the input text:");
        InputFile = input.nextLine();
        File file = new File(InputFile);
        HuffmanTableFile = file.getParent() + "\\"+file.getName().substring(0, file.getName().length()-4)+ ".table";
        HuffmanCodeFile = file.getParent() + "\\"+file.getName().substring(0, file.getName().length()-4)+ ".huffman";
        // System.out.println(HuffmanTableFile+"\n"+HuffmanCodeFile);
        int[] tempCount = new int[200];
        // BufferedReader br = new BufferedReader(new FileReader(file));
        // String st = br.readLine();
        // br.close();
        String st = "";
        try (BufferedReader br
             = new BufferedReader(new FileReader(file))) {
            String str;
            while ((str = br.readLine()) != null) {
                // System.out.println(br);
                st+=str+"\n";
            }
        }
        catch (IOException e) {
            System.out.println(
               "Error while reading a file.");
               return;
        }
       
        
        for (int i = 0; i < st.length(); i++) {
            tempCount[getIndex(st.charAt(i))]++;
        }
        int count = countNonZero(tempCount), k = 0;
        int[] numberOf = new int[count];
        char[] numberOfChar = new char[count];
        finalChar = new char[count];
        finalCode = new String[count];
        for (int i = 0; i < tempCount.length; i++) {
            if (tempCount[i] != 0) {
                numberOf[k] = tempCount[i];
                numberOfChar[k] = getChar(i);
                k++;
            }
        }
        PriorityQueue<Huffman> queue = new PriorityQueue<Huffman>(numberOf.length, new HuffmanComparator());
        for (int i = 0; i < numberOf.length; i++) {
            Huffman node = new Huffman();
            node.freq = numberOf[i];
            node.chr = numberOfChar[i];
            node.left = null;
            node.right = null;

            queue.add(node);
        }
        Huffman root = null;
        if (queue.size() == 1) {
            Huffman a = queue.poll();
            Huffman newHuffman = new Huffman();
            newHuffman.freq = a.freq;
            newHuffman.chr = '#';
            newHuffman.left = a;
            newHuffman.right = null;
            root = newHuffman;
            queue.add(newHuffman);
        } else {
            while (queue.size() > 1) {
                Huffman a = queue.poll();
                Huffman b = queue.poll();
                Huffman newHuffman = new Huffman();
                newHuffman.freq = a.freq + b.freq;
                newHuffman.chr = '#';
                newHuffman.left = a;
                newHuffman.right = b;

                root = newHuffman;
                queue.add(newHuffman);
            }
        }
        printCode(root, "");
        String temp = "";
        RandomAccessFile raf3 = new RandomAccessFile(HuffmanTableFile, "rw");
        raf3.writeInt(finalChar.length);
        for (int i = 0; i < finalChar.length; i++) {
            // System.out.println(finalChar[i] + "= " + finalCode[i]);
            raf3.writeChar(finalChar[i]);
            raf3.writeUTF(finalCode[i]);
        }
        for (int i = 0; i < st.length(); i++) {
            for (int j = 0; j < finalChar.length; j++) {
                if (finalChar[j] == st.charAt(i)) {
                    temp += finalCode[j];
                    break;
                }
            }
        }
        raf3.close();
        
        int numm = temp.length();
        int start = 0;
        String finalByte = "";
        File filee = new File(HuffmanCodeFile);
        if (filee.exists() && filee.isFile()) {
            filee.delete();
        }
        filee.createNewFile();
        RandomAccessFile raf = new RandomAccessFile(HuffmanCodeFile, "rw");
        String tmp = "";
        while (numm > start) {
            Byte tmpByte = 0;
            if (numm - start >= 7) {
                tmp = temp.substring(start, start + 7);
            } else {
                tmp = temp.substring(start, numm);
            }
            tmpByte = Byte.parseByte(tmp, 2);
            // System.out.println("=====" + tmp);
            raf.writeByte(tmpByte);
            start += 7;
        }
        raf.writeByte(tmp.length());
        raf.close();
        System.out.println("Huffman code and table created successfully. Now oppening the location of these files");
        Desktop.getDesktop().open(file.getParentFile());
    }

    static void Decompress() throws Exception {
        Scanner input = new Scanner(System.in);
        
        System.out.println("Make sure the table and the huffman code is in the same folder.");
        System.out.println("Enter the huffman file's address without the extension: (for example C:\\text)");
        String in = input.nextLine();
        HuffmanTableFile = in+".table";
        HuffmanCodeFile = in+".huffman";
        ///////////////////////////////////////////////////
        System.out.println("Recovering the huffman table from the file...");
        RandomAccessFile raf4 = new RandomAccessFile(HuffmanTableFile, "r");
        int newCount = raf4.readInt();
        char[] newfinalChar = new char[newCount];
        String[] newfinalCode = new String[newCount];
        int l = 0;
        while (raf4.getFilePointer() < raf4.length()) {
            newfinalChar[l] = raf4.readChar();
            newfinalCode[l] = raf4.readUTF();
            // System.out.println(newfinalChar[l] + ": " + newfinalCode[l]);
            l++;
        }
        raf4.close();
        System.out.println("Reading the huffman code from the huffman file...");
        RandomAccessFile raf1 = new RandomAccessFile(HuffmanCodeFile, "r");
        String tempy = "";
        String s1 = "";
        while (raf1.getFilePointer() < raf1.length() - 1) {
            byte te = raf1.readByte();
            if (raf1.getFilePointer() == raf1.length() - 1) {
                s1 = String.format("%s", Integer.toBinaryString(te & 0xFF)).replace(' ', '0');
                byte te2 = raf1.readByte();
                // System.out.println("length == " + te2);
                for (int i = 0; s1.length() < te2; i++) {
                    s1 = "0" + s1;
                }
            } else
                s1 = String.format("%7s", Integer.toBinaryString(te & 0xFF)).replace(' ', '0');
            // System.out.println(te + ": " + s1);
            tempy += s1;
        }
        raf1.close();
        // System.out.println(tempy);
        // System.out.println("------------------------------");
        System.out.println("Decrypting the huffman code by its table...");
        String finalTemp = "";
        String finalOutput = "";
        for (int i = 0; i < tempy.length(); i++) {
            finalTemp += tempy.charAt(i);
            for (int j = 0; j < newfinalCode.length; j++) {
                if (newfinalCode[j].equals(finalTemp)) {
                    finalOutput += newfinalChar[j];
                    finalTemp = "";
                }
            }

        }
        // System.out.println(finalOutput);
        String saved = in+" (Decompressed).txt";
        
        PrintWriter out = new PrintWriter(saved);
        out.println(finalOutput);
        out.close();
        
        Desktop.getDesktop().open(new File(saved));
    }

    static int countNonZero(int[] array) {
        int count = 0;
        for (int i = 0; i < array.length; i++)
            if (array[i] != 0)
                count++;
        return count;
    }

    static char getChar(int a) {
        return (char) a;
    }

    static int getIndex(char a) {
        return (int) a;
    }
}
