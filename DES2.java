import java.util.Vector;
public class DES2 extends DES_Steps
{
    //Same as in DES0
    private String name;
    private String p;
    private String nP;
    private String k;
    private String nK;
    private String cPK;
    private Vector<String> rCPK;
    private String cNPK;
    private Vector<String> rCNPK;
    private String cPNK;
    private Vector<String> rCPNK;

    //Same as in DES0
    public DES2(String aName, String p, String nP, String k, String nK)
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

    public String getName(){
        return name;
    }
    
    //Same as in DES0
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
        count = 0;
        for(int i = 0; i < cPK.length(); i++){
            if(cPK.charAt(i) != cNPK.charAt(i)){
                count++;
            }
        }
        output.add(count);
        return output;
    }

    //Same as in DES0
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

    //Same as in DES0, except fiestal function modified as outlined in assignment spec
    private String encrypt(String plainText, String key, Vector<String> rState)
    {
        String modKey = keyBitDrop(key);
        Vector<String>bigKeys = new Vector<>(keyRotate(modKey));
        Vector<String>smallKeys = new Vector<>(keyCompress(bigKeys));
        String output = doIP(plainText);
        output = roundsAndSwap(output, smallKeys, rState);
        output = doFP(output);
        return output;
    }

    //Same as in DES0, except fiestal function modified as outlined in assignment spec
    private String roundsAndSwap(String input, Vector<String> keyList, Vector<String> rState){
        String output = "";

        String left = input.substring(0, (input.length()/2));
        String right = input.substring((input.length()/2));

        for(int i = 0; i < 16; i++){
            String tempR = expandTxt(right);
            String tempL = right;
            tempR = xor(tempR, keyList.get(i));
            //Modification to Fiestal Function, S-Boxs stage is replaced with performing an inverse of the Expansion function, using the IEP permutation outlined in DES_Steps
            tempR = inverseEP(tempR);
            tempR = fPerm(tempR);
            right = xor(left, tempR);
            left = tempL;
            rState.add(left + right);
        }
        output = right + left;

        return output;
    }
}