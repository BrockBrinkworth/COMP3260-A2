import java.util.Vector;
public class DES0 extends DES_Steps
{
    //DES object name, used for formatting results later
    private String name;

    //vairable for P
    private String p;

    //variable for P'
    private String nP;
    
    //Variable for K
    private String k;

    //Variable for K'
    private String nK;

    //Cipher text using P & K
    private String cPK;
    //State of the text in each stage of DES using P & K
    private Vector<String> rCPK;

    //Cipher text using P' & K
    private String cNPK;
    //State of the text in each stage of DES using P' & K
    private Vector<String> rCNPK;

    //Cipher text using P & K'
    private String cPNK;
    //State of the text in each stage of DES using P' & K
    private Vector<String> rCPNK;
    
    //Constructor requiring name, p, p', k' & k'
    public DES0(String aName, String p, String nP, String k, String nK)
    {
        name = aName;
        this.p = p;
        this.nP = nP;
        this.k = k;
        this.nK = nK;
        rCPK = new Vector<>();
        rCNPK = new Vector<>();
        rCPNK = new Vector<>();
        cPK = encrypt(this.p, this.k, rCPK);
        cNPK = encrypt(this.nP, this.k, rCNPK);
        cPNK = encrypt(this.p, this.nK, rCPNK);
    }

    //Alternate constructor only requiring p & k
    public DES0(String p, String k){
        name = null;
        this.p = p;
        this.k = k;
        nK = null;
        nP = null;
        rCPK = null;
        rCNPK = null;
        rCPNK = null;
        cPK = decrypt(this.p, this.k);
        cNPK = null;
        cPNK = null;
    }
    
    //Accessor method for cPK
    public String getCPK(){
        return addSpaces(cPK);
    }

    //Accessor method for cNPK
    public String getCNPK(){
        return addSpaces(cNPK);
    }

    //Accessor method for cPNK
    public String getCPNK(){
        return addSpaces(cPNK);
    }

    //Accesor method for DES obj name
    public String getName(){
        return name;
    }

    //Method to return and Integer Vector which contains the bit differences of both P & P' being encrypted under K
    public Vector<Integer> compareP(){
        Vector<Integer> output = new Vector<>();
        Integer count = 0;
        for(int i = 0; i < rCPK.size(); i++){
            String a = rCPK.get(i);
            String b = rCNPK.get(i);
            count = 0;
            for(int j = 0; j < a.length(); j++){
                if(a.charAt(j) != b.charAt(j)){
                    count++;
                }
            }
            output.add(count);
        }
        //Includes the final ciphertext after the 32 bit swap
        count = 0;
        for(int i = 0; i < cPK.length(); i++){
            if(cPK.charAt(i) != cNPK.charAt(i)){
                count++;
            }
        }
        output.add(count);
        return output;
    }

    //Same as compareP except for comparing P being ecnrypted under K & K'.
    public Vector<Integer> compareK(){
        Vector<Integer> output = new Vector<>();
        Integer count = 0;
        for(int i = 0; i < rCPK.size(); i++){
            String a = rCPK.get(i);
            String b = rCPNK.get(i);
            count = 0;
            for(int j = 0; j < a.length(); j++){
                if(a.charAt(j) != b.charAt(j)){
                    count ++;
                }
            }
            output.add(count);
        }
        count = 0;
        for(int i = 0; i < cPK.length(); i++){
            if(cPK.charAt(i) != cPNK.charAt(i)){
                count++;
            }
        }
        output.add(count);
        return output;
    }

    //Method for encrypting something using DES
    private String encrypt(String plainText, String key, Vector<String> rState)
    {
        //56 bit key after permuting against PC1
        String modKey = keyBitDrop(key);
        //Generates key lists for seach rounf of DES
        Vector<String>bigKeys = new Vector<>(keyRotate(modKey));
        Vector<String>smallKeys = new Vector<>(keyCompress(bigKeys));
        //Initial Permutation
        String output = doIP(plainText);
        //Performs the 16 internal rounds of DES as well as the final 32 bit swap
        output = roundsAndSwap(output, smallKeys, rState);
        //Final Permutation
        output = doFP(output);
        return output;
    }

    //Same as encrypt() except generates a reverse key schedule, as required for DES
    private String decrypt(String cipherText, String key){
        String modKey = keyBitDrop(key);
        Vector<String>bigKeys = new Vector<>(keyRotate(modKey));
        Vector<String>smallKeys = new Vector<>(keyCompress(bigKeys));
        Vector<String>iSmlKeys = new Vector<>(keyInverse(smallKeys));
        String output = doIP(cipherText);
        output = roundsAndSwap(output, iSmlKeys);
        output = doFP(output);
        return output; 
    }
    
    //This method is the Fiestal function, as well as splitting the plain text in 2 after the IP and the 32 bit swap at the end of the function
    private String roundsAndSwap(String input, Vector<String> keyList){
        String output = "";
        //Splits plaintext in halves
        String left = input.substring(0, (input.length()/2));
        String right = input.substring((input.length()/2));

        for(int i = 0; i < 16; i++){
            //Expansion Permutation Step, assigns it to a temp value
            String tempR = expandTxt(right);
            //Saves the 32 bit right half in a temp value for later
            String tempL = right;
            //XOR with round key step
            tempR = xor(tempR, keyList.get(i));
            //S-BOX step
            tempR = sBox(tempR);
            //Permutation P
            tempR = fPerm(tempR);
            //New right becomes LHS XOR RHS
            right = xor(left, tempR);
            //New left becomes old right pre modification
            left = tempL;
        }
        //Final 32 bit swap
        output = right + left;

        return output;
    }

    //Same as the method with the same name, this method just also takes a string vector so it can save the state of the cipher text after each round of DES
    private String roundsAndSwap(String input, Vector<String> keyList, Vector<String> rState){
        String output = "";

        String left = input.substring(0, (input.length()/2));
        String right = input.substring((input.length()/2));
        //output += "\nAfter split: L= " + binToHex(left) + " R= " + binToHex(right) +"\n"; 

        for(int i = 0; i < 16; i++){
            String tempR = expandTxt(right);
            String tempL = right;
            tempR = xor(tempR, keyList.get(i));
            tempR = sBox(tempR);
            tempR = fPerm(tempR);
            right = xor(left, tempR);
            left = tempL;
            rState.add(left + right);
        }
        output = right + left;

        return output;
    }

    //Method to simply add a whitespace after every 8 characters for ease of reading
    private String addSpaces(String input){
        String output = "";
        for(int i = 8; i <= input.length(); i += 8){
            output += input.substring(i - 8, i) + " ";
        }
        return output;
    }    private String binToHex(String input){
        String output = "";
        for(int i = 8; i <= input.length(); i += 8){
            int temp = Integer.parseInt(input.substring(i-8, i), 2);
            String tempS = Integer.toHexString(temp);
            if(tempS.length() < 2){
                tempS = "0" + tempS;
            }
            output += tempS; 
        }
        return output.toUpperCase();
    }
}