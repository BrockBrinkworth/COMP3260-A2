import java.util.Vector;
abstract class DES_Steps {
    // initial permutation table
    private static final int [] IP = 
    { 
        58, 50, 42, 34, 26, 18,
        10, 2, 60, 52, 44, 36, 28,
        20,12, 4, 62, 54, 46, 38,
        30, 22, 14, 6, 64, 56,
        48, 40, 32, 24, 16, 8,
        57, 49, 41, 33, 25, 17,
        9, 1, 59, 51, 43, 35, 27,
        19, 11, 3, 61, 53, 45,
        37, 29, 21, 13, 5, 63, 55,
        47, 39, 31, 23, 15, 7 
    };

    // Returns data from private initial permutation table
    public static int[] getIP()
    {
        return IP;
    }

    // expansion table
    private static final int[] EP = 
    {
        32, 1, 2, 3, 4, 5, 4,
        5, 6, 7, 8, 9, 8, 9, 10,
        11, 12, 13, 12, 13, 14, 15,
        16, 17, 16, 17, 18, 19, 20,
        21, 20, 21, 22, 23, 24, 25,
        24, 25, 26, 27, 28, 29, 28,
        29, 30, 31, 32, 1 
    };

    // Returns data from private expansion table
    public static int[] getEP()
    {
        return EP;
    }

     // inverse expansion permutation
     private static final int[] IEP = 
     {
        2, 3, 4, 7, 8, 9, 10, 13, 14, 
        15, 16, 19, 20, 21, 22, 25, 
        26, 27, 28, 31, 32, 33, 34, 
        37, 38, 39, 40, 43, 44, 45, 
        46, 47
     };
 
     // Returns data from private inverse expansion permutation table
     public static int[] getIEP()
     {
         return IEP;
     }

    // first key-permutation table
	private static final int[] PC1 = 
    {  
        57, 49, 41, 33, 25,
        17, 9, 1, 58, 50, 42, 34, 26,
        18, 10, 2, 59, 51, 43, 35, 27,
        19, 11, 3, 60, 52, 44, 36, 63,
        55, 47, 39, 31, 23, 15, 7, 62,
        54, 46, 38, 30, 22, 14, 6, 61,
        53, 45, 37, 29, 21, 13, 5, 28,
        20, 12, 4 
    };

    // Returns data from private first key permutation table
    public static int[] getPC1()
    {
        return PC1;
    }

    // second key-permutation table
    private static final int[] PC2 = 
    {  
        14, 17, 11, 24, 1, 5, 3,
        28, 15, 6, 21, 10, 23, 19, 12,
        4, 26, 8, 16, 7, 27, 20, 13, 2,
        41, 52, 31, 37, 47, 55, 30, 40,
        51, 45, 33, 48, 44, 49, 39, 56,
        34, 53, 46, 42, 50, 36, 29, 32 
    };

    // Returns data from private second key permutation table
    public static int[] getPC2()
    {
        return PC2;
    }

    // S-box table
    private static final int[][][] sbox =
    {
        { { 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 },
        { 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 },
        { 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 },
        { 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 } },

        { { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10 },
        { 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 },
        { 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 },
        { 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 } },

        { { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8 },
        { 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1 },
        { 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7 },
        { 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 } },

        { { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15 },
        { 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9 },
        { 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4 },
        { 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 } },

        { { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9 },
        { 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6 },
        { 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14 },
        { 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 } },

        { { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11 },
        { 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8 },
        { 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6 },
        { 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 } },

        { { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1 },
        { 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6 },
        { 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 },
        { 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 } },

        { { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7 },
        { 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2 },
        { 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8 },
        { 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11 } } 
    };

    // Returns data from private s-box table
    public static int[][][] getSbox()
    {
        return sbox;
    }

    // straight permutation table
	private static final int[] P = 
    {
        16, 7, 20, 21, 29, 12, 28,
        17, 1, 15, 23, 26, 5, 18,
        31, 10, 2, 8, 24, 14, 32,
        27, 3, 9, 19, 13, 30, 6,
        22, 11, 4, 25 
    };

    // Returns data from private straight permutation table
    public static int[] getP()
    {
        return P;
    }

    // inverse initial permutation table
    private static final int[] FP =
    { 
        40, 8, 48, 16, 56, 24, 64,
        32, 39, 7, 47, 15, 55,
        23, 63, 31, 38, 6, 46,
        14, 54, 22, 62, 30, 37,
        5, 45, 13, 53, 21, 61,
        29, 36, 4, 44, 12, 52,
        20, 60, 28, 35, 3, 43,
        11, 51, 19, 59, 27, 34,
        2, 42, 10, 50, 18, 58,
        26, 33, 1, 41, 9, 49,
        17, 57, 25 
    };

    // Returns data from private inverse initial permutation table
    public static int[] getFP()
    {
        return FP;
    }

    // shifting bits
    private static final int[] shiftBits = 
    { 
        1, 1, 2, 2, 2, 2, 2, 2,
        1, 2, 2, 2, 2, 2, 2, 1 
    };

    // Returns data from private shiftBits table
    public static int[] shiftBits()
    {
        return shiftBits;
    }

    // Initial Permutation - Begins before round 1. 
    // Permutes the bits of the plaintext as outline in the initial permutation table
    // IE, the bit at position IP[i] in the Plaintext will become the bit at position 'i'
    public static String doIP(String plainText){
        String output = "";
        
        for(int i = 0; i < IP.length; i++){
            output += plainText.charAt(IP[i] - 1);
        }
        
        return output;
    }
    
    // Final permutation table method - Inverse of the initial permutation table
    public static String doFP(String text){
        String output = "";
        for(int i = 0; i < FP.length; i++){
            output += text.charAt(FP[i] - 1);
        }
        return output;
    }

    // Expansion permutation table method - expansion of the right half of the data from 32 bits to 48 bits.
    // This has 2 purposes, one is to make the bits longer for the XOR method, and the other is to provide longer results
    // that can be compressed during the substitution operation.
    public static String expandTxt(String text){
        String output = "";

        for(int i = 0; i < EP.length; i++){
            output += text.charAt(EP[i] - 1);
        }

        return output;
    }

    //Method to perform the inverse expansion permutation. Used to shrink a 48 bit string to 32
    public static String inverseEP(String text){
        String output = "";

        for(int i = 0; i < IEP.length; i++){
            output += text.charAt(IEP[i] - 1);
        }

        return output;
    }

    // Exclusive or method - Binary comparison of bits 0 & 1, 
    // if bits are the same result is 0, if bits are different result is 1.
    public static String xor(String text, String key){
        String output = "";

        for(int i = 0; i < text.length(); i++){
            if(text.charAt(i) == key.charAt(i)){
                output += "0";
            }
            else{
                output += "1";
            }
        }
        return output;
    }

    // Substitution box method - takes 6 bit binary input and uses 2 outside bits for the row and inside 4 bits for coloumn
    // The value in the table located with these is now converted to binary of only 4 bits. From 48 bits to 32 bits.
    public static String sBox(String text){
        String output = "";
        Vector<String> blocks = new Vector<>();

        for(int i = 6; i <= text.length(); i += 6){
            blocks.add(text.substring(i - 6, i));
        }

        for(int i = 0; i < blocks.size(); i++){
            String temp = blocks.get(i);
            String sRow = temp.substring(0, 1) + "" + temp.substring(temp.length() - 1);
            int row = Integer.parseInt(sRow, 2);
            String sCol = temp.substring(1, (temp.length() - 1));
            int col = Integer.parseInt(sCol, 2);
            temp = Integer.toBinaryString(sbox[i][row][col]);
            temp = String.format("%4s", temp).replaceAll(" ", "0");
            output += temp;
        }
        return output;
    }

    //Method to perform the final permutation in the Fiestal Function
    //Param is the text in that stage of the Fiestal Function 
    public static String fPerm(String text){
        String output = "";

        for(int i = 0; i < P.length; i++){
            output += text.charAt(P[i] - 1);
        }

        return output;
    }

    // Converts the 64 bit key to a 56 bit key using the PC1 table
    // Drops or reorganises bits at places according to the PC1 permutation table
    // Param is the original key passed to the program 
    public static String keyBitDrop(String key){
        String modKey = "";

        for(int i = 0; i < PC1.length; i++){
            modKey += key.charAt(PC1[i] - 1);
        }

        return modKey;
    }

    // Generates a list of 56 bit keys to form the 48 bit keys in each round by performing a leftward bit rotation
    //The param is the inital 56 bit key formed from the PC1 permutation table in keyBitDrop
    public static Vector<String> keyRotate(String oldKey){
        Vector<String> keyList = new Vector<>();
        String temp = oldKey;
        for(int i = 0; i < 16; i++){
            String a = temp.substring(0, (temp.length()/2));
            String b = temp.substring((temp.length()/2));
            String tempA = a.substring(0, shiftBits[i]);
            String tempB = b.substring(0, shiftBits[i]);
            a = a.substring(shiftBits[i]);
            b = b.substring(shiftBits[i]);
            a += tempA;
            b += tempB;
            temp = a + b;
            keyList.add(temp);
        }
        return keyList;
    }

    //Shrinks each rounds 56 bit key to a 48 bit key using the PC2 Permutation table 
    //Param is a string vector of 56 bit keys and returns a string vector of 48 bit keys
    public static Vector<String> keyCompress(Vector<String> bigKey){
        Vector<String> smallKey = new Vector<>();

        for(int i = 0; i < bigKey.size(); i++){
            String bigTemp = bigKey.get(i);
            String smlTemp = "";
            for(int j = 0; j < PC2.length; j++){
                smlTemp += bigTemp.charAt(PC2[j] - 1);
            }
            smallKey.add(smlTemp);
        }

        return smallKey;
    }

    //Reverses the list of 48 bit keys for decryption purposes
    //Param is a String vector of 48 bit keys and simply returns a vector with the elements reversed
    public static Vector<String> keyInverse(Vector<String> keys){
        Vector<String> output = new Vector<>();

        for(int i = keys.size(); i > 0; i --){
            output.add(keys.get(i - 1));
        }
        return output;
    }
}