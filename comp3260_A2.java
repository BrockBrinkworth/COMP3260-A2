/*
    Authors: Hamish Broadhurst-Tynan (3238465) & Brock Brinkworth (3331952)
    Purpose: This class is main and the entry point for the program 
*/

import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
public class comp3260_A2
{
    public static void main(String args[])
    {
        String p = "";
        String nP = "";
        String k = "";
        String nK = "";
      
        //Attempts to read the Encryption input file, and exits out if it doesn't.
        //Reads each line as P, P', K, K' respectively
        try
        {
            File inFile = new File("Encrypt-Input.txt");
            Scanner fReader = new Scanner(inFile);

            while(fReader.hasNextLine()){
                p = fReader.nextLine();
                nP = fReader.nextLine();
                k = fReader.nextLine();
                nK = fReader.nextLine();
            }

            fReader.close();
        }

        catch (FileNotFoundException e)
        {
            System.out.println("File not found...\nExiting program...");
            return;
        }

        //Begins making the header for the output string
        String header = "Avalanche Demonstration\n";
        header += "Plaintext P:  " + p + "\n";
        header += "Plaintext P': " + nP + "\n";
        header += "Key K:  " + k + "\n";
        header += "Key K': " + nK + "\n";

        //Removes all whitespaces in the strings for easier manipulation going forward
        p = p.replace(" ", "");
        nP = nP.replace(" ", "");
        k = k.replace(" ", "");
        nK = nK.replace(" ", "");

        //Creates the P & P' under K and P under K & K' avalanche tables
        String table1 = "\nP and P' under K\n";
        String table2 = "\nP under K and K'\n";

        //Starts timer on DES for analysis
        long start = System.currentTimeMillis();

        //Creates a DES0 obj called DES0, DES0 is the default version of DES
        //Takes a name, p, p', k,k' as parameters
        DES0 des0 = new DES0("DES0", p, nP, k, nK);

        //Adds details to the relevant tables
        table1 += "Ciphertext C:  " + des0.getCPK() + "\n";
        table1 += "Ciphertext C': " + des0.getCNPK() + "\n";
        table2 += "Ciphertext C:  " + des0.getCPK() + "\n";
        table2 += "Ciphertext C': " + des0.getCPNK() + "\n";

        //Same as DES0 constructors, modified DES algorithims as outlined in the assginment spec
        DES1 des1 = new DES1("DES1", p, nP, k, nK);
        DES2 des2 = new DES2("DES2", p, nP, k, nK);
        DES3 des3 = new DES3("DES3", p, nP, k, nK);

        //Formats and adds details about the avalanche effect to relevant tables
        table1 += "Round    " + des0.getName() + "  " + des1.getName() + "  " + des2.getName() + "  " + des3.getName() + "\n";
        for(int i = 0; i < 17; i++){
            table1 += String.format("%5d    %4d  %4d  %4d  %4d", i, des0.compareP().get(i), des1.compareP().get(i), des2.compareP().get(i), des3.compareP().get(i)) + "\n";
        }
        table2 += "Round    " + des0.getName() + "  " + des1.getName() + "  " + des2.getName() + "  " + des3.getName() + "\n";
        for(int i = 0; i < 17; i++){
            table2 += String.format("%5d    %4d  %4d  %4d  %4d", i, des0.compareK().get(i), des1.compareK().get(i), des2.compareK().get(i), des3.compareK().get(i)) + "\n";
        }

        //End timer for avalanche effect analysis
        long end = System.currentTimeMillis();

        //Finishes formatting and combining output string to write to file
        header += "Total Running Time: " + (end - start) + "(ms)\n";
        String output = header + table1 + table2;

        //Creates an Encrypt output file called "Encrypt-Output.txt" in the same folder as the project, if one doesn't exist
        try{
            File enOutFile = new File("Encrypt-Output.txt");
            if(enOutFile.createNewFile()){
                System.out.println(enOutFile.getName() + " created");
            }else{
                System.out.println(enOutFile.getName() + " already exists");
            }
        } catch(IOException e){
            System.out.println("Error: ");
            e.printStackTrace();
        }

        //Writes the output string to the file called "Encrypt-Output.txt"
        try{
            FileWriter outW = new FileWriter("Encrypt-Output.txt");
            outW.write(output);
            outW.close();
            System.out.println("Successfully wrote to Encrypt-Output.txt");
        } catch(IOException e){
            System.out.println("Error: ");
            e.printStackTrace();
        }

        String cipherText = "";
        String key = "";
        
        //Reads in data from the Decrypt-Input file, with the 1st line being the cipherText and the 2nd line being the original Key
        //Exits program if unsuccesful
        try
        {
            File inFile = new File("Decrypt-Input.txt");
            Scanner fReader = new Scanner(inFile);

            while(fReader.hasNextLine()){
                cipherText = fReader.nextLine();
                key = fReader.nextLine();
            }

            fReader.close();
        }

        catch (FileNotFoundException e)
        {
            System.out.println("File not found...\nExiting program...");
            return;
        }

        //Begins formatting output string for decryption demonstration
        String decryptOut = "DECRYPTION\n";
        decryptOut += "CipherText C: " + cipherText + "\n";
        decryptOut += "Key K: " + key + "\n";

        //Removes all whitespaces from cipherText and key for easy of use later
        cipherText = cipherText.replace(" ", "");
        key = key.replace(" ", "");

        //Creates another DES0 object with a modified constructor, only requiring the cipherText and the key
        DES0 decryptMod = new DES0(cipherText, key);

        //Adds the decrypted cipherText to the output file
        decryptOut += "Plaintext P: " + decryptMod.getCPK();
        
        //Attempts to create a file called "Decrypt-Output.txt" in the same folder as the project, if one doesn't already exist
        try{
            File deOutFile = new File("Decrypt-Output.txt");
            if(deOutFile.createNewFile()){
                System.out.println(deOutFile.getName() + " created");
            }else{
                System.out.println(deOutFile.getName() + " already exists");
            }
        } catch(IOException e){
            System.out.println("Error: ");
            e.printStackTrace();
        }

        //Attempts to write the decryption output to the "Decrypt-Output.txt" file
        try{
            FileWriter outW = new FileWriter("Decrypt-Output.txt");
            outW.write(decryptOut);
            outW.close();
            System.out.println("Successfully wrote to Decrypt-Output.txt");
        } catch(IOException e){
            System.out.println("Error: ");
            e.printStackTrace();
        }
    }
}