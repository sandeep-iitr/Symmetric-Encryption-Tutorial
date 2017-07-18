import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

public class SymmetricKey {

	private SecretKeySpec secretKey;
	private Cipher cipher;
	
	public SymmetricKey(String secret, int length, String algorithm) {
		
		try{
		byte[] key = new byte[length];
		key = fixSecret(secret, length);
		this.secretKey = new SecretKeySpec(key, algorithm);
		this.cipher = Cipher.getInstance(algorithm);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}//end fxn

	
	private byte[] fixSecret(String s, int length) {
		try{
		if (s.length() < length) {
			int missingLength = length - s.length();
			for (int i = 0; i < missingLength; i++) {
				s += " ";
			}
		}
		System.out.println("Fixed S is:"+s.substring(0,length));
		return s.substring(0, length).getBytes("UTF-8");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}//end fxn
	
	
	public void encryptFile(File f){
		try{
		System.out.println("Encrypting file: " + f.getName());
		this.cipher.init(Cipher.ENCRYPT_MODE, this.secretKey);
		//The cipher is initialized for one of the following four operations: encryption, decryption, key wrapping or key unwrapping, depending on the value of opmode.
		
		this.writeToFile(f);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}//end fxn
	
	public void decryptFile(File f) {
		try{
		System.out.println("Decrypting file: " + f.getName());
		this.cipher.init(Cipher.DECRYPT_MODE, this.secretKey);
		//The cipher is initialized for one of the following four operations: encryption, decryption, key wrapping or key unwrapping, depending on the value of opmode.
		
		this.writeToFile(f);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}//end fxn
	
	public void writeToFile(File f) {
		try{
		FileInputStream in = new FileInputStream(f);
		byte[] input = new byte[(int) f.length()];
		in.read(input);

		FileOutputStream out = new FileOutputStream(f);
		//Encrypts or decrypts data in a single-part operation, or finishes a multiple-part operation. The data is encrypted or decrypted, depending on how this cipher was initialized.
		
		byte[] output = this.cipher.doFinal(input);
		out.write(output);

		out.flush();
		out.close();
		in.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}//end fxn
	
	
	
	public static void main(String[] args) {
		
		try{
		File dir = new File("src/secureFiles");
		File[] filelist = dir.listFiles();

		SymmetricKey ske;
		
		ske = new SymmetricKey("!@#$MySecr3tPassw0rd", 16, "AES");
		
		int choice = -2;
		while (choice != -1) {
			String[] options = { "Encrypt All", "Decrypt All", "Exit" };
			System.out.println("Enter Choice:");
			System.out.println("0:"+options[0]);
			System.out.println("1:"+options[1]);
			System.out.println("2:"+options[2]);
			Scanner in = new Scanner(System.in);
			choice = in.nextInt();
			
			switch (choice) {
			case 0:
				Arrays.asList(filelist).forEach(file -> {
					try {
						ske.encryptFile(file);
					} catch (Exception e) {
						System.err.println("Couldn't encrypt " + file.getName() + ": " + e.getMessage());
					}
				});
				System.out.println("Files encrypted successfully");
				break;
			case 1:
				Arrays.asList(filelist).forEach(file -> {
					try {
						ske.decryptFile(file);
					} catch (Exception e) {
						System.err.println("Couldn't decrypt " + file.getName() + ": " + e.getMessage());
					}
				});
				System.out.println("Files decrypted successfully");
				break;
			default:
				choice = -1;
				break;
			}//end switch
			
		}//end while
		
		}//try
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
	}//end main

}
