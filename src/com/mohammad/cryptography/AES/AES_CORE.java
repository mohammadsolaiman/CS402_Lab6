package com.mohammad.cryptography.AES;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import com.mohammad.cryptography.GF_Calculator;

public class AES_CORE {

	private String _128bitKey;

	private List<String[][]> AllRoundKeys;
	private KeyGenerator KG ;
	public Tools tool;
	
	
	public static void main(String[] args){
		String hexKey = "0f1571c947d9e8590cb7add6af7f6798";
		Tools tool = new Tools();
		String binaryKey = tool.hex2binary(hexKey);
		System.out.println("KEY : "+binaryKey+"\nSIZE = "+binaryKey.length());
		KeyGenerator kg = new KeyGenerator(binaryKey);
		AES_CORE aes = new AES_CORE(binaryKey);
		String plain = "0123456789abcdeffedcba9876543210";
		String plainBIT = tool.hex2binary(plain);
		System.out.println("PLAIN : "+plainBIT);
		String cypher = aes.encrypt(plainBIT);
		System.out.println("\nCYPHER : "+cypher);
		System.out.println("\nDECRYPT : "+aes.decrypt(cypher));
	}
	
	public AES_CORE(String _128bitKey) {
		this._128bitKey = _128bitKey;
		KG = new KeyGenerator(_128bitKey);
		AllRoundKeys = KG.getAllKeys();
		tool = new Tools();
	}
	
	public String encrypt(String plainText){
		String[][] plain = tool._128bitStringTo4x4Matrix(plainText);
		String[][] first = addRoundKey(plain, AllRoundKeys.get(0));
		String[][] roundOut = first;
		for(int round = 1;round<=9;round++){
			roundOut = addRoundKey(MixColumns(ShiftRows(SubstituteBytes(roundOut))), AllRoundKeys.get(round));
		}
		return tool._4x4bitMatrix2String(addRoundKey(ShiftRows(SubstituteBytes(roundOut)), AllRoundKeys.get(10)));
	}
	
	public String decrypt(String cypher){
		String[][] cy = tool._128bitStringTo4x4Matrix(cypher);
		String[][] first = addRoundKey(cy, AllRoundKeys.get(10));
		String[][] roundOut = first;
		for(int round = 9;round>=1;round--){
			roundOut = InverseMixColumns(addRoundKey(InverseSubstituteBytes(InverseShiftRows(roundOut)), AllRoundKeys.get(round)));
		}
		
		
		return tool._4x4bitMatrix2String(addRoundKey(InverseSubstituteBytes(InverseShiftRows(roundOut)), AllRoundKeys.get(0)));
	}
	
	public String[][] addRoundKey(String[][] plain, String[][] key){

		String[][] out = new String[4][4];
		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				out[i][j] = tool.Xor(plain[i][j],key[i][j]);
			}
		}
		return out;
	}
	
	public String[][] SubstituteBytes(String[][] input){

		String[][] out = new String[4][4];
		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				out[i][j] = AES_CONSTANTS.Substitution(input[i][j]);
			}
		}
		return out;
	}
	
	public String[][] InverseSubstituteBytes(String[][] input){
		String[][] out = new String[4][4];
		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				out[i][j] = AES_CONSTANTS.InverseSubstitution(input[i][j]);
			}
		}
		return out;
	}
	
	public String[][] InverseShiftRows(String[][] input){
		String[][] out = new String[4][4];
		
		for(int i =0;i<4;i++){
			
			for(int j = 0;j<i;j++){
				out[i][j] = input[i][4-i+j];
				
			}
			for(int j=i;j<4;j++){
				out[i][j]= input[i][j-i];
			}
		}
		return out;
	}
	
	public String[][] ShiftRows(String[][] input){

		String[][] out = new String[4][4];
		
		for(int i =0;i<4;i++){
			for(int j = 4-i;j<4;j++){
				out[i][j] = input[i][j-4+i];
			}
			for(int j = i;j<4;j++){
				out[i][j-i] = input[i][j];
			}
		}

		return out;
	}


	public String[][] MixColumns(String[][] input){

		String[][] out = new String[4][4];
		GF_Calculator gf8 = new GF_Calculator(8);
		for(int i =0;i<4;i++){
			for(int j = 0;j<4;j++){
				out[i][j] = "00000000";
				
				for(int k=0;k<4;k++){
					out[i][j] =tool.Xor(out[i][j], gf8.GF8_mult(tool.hex2binary(AES_CONSTANTS.Mix[i][k]), input[k][j]));
				}
			}
		}
		
		return out;
	}
	public String[][] InverseMixColumns(String[][] input){
		
		String[][] out = new String[4][4];
		GF_Calculator gf8 = new GF_Calculator(8);
		for(int i =0;i<4;i++){
			for(int j = 0;j<4;j++){
				out[i][j] = "00000000";
				
				for(int k=0;k<4;k++){
					out[i][j] =tool.Xor(out[i][j], gf8.GF8_mult(tool.hex2binary(AES_CONSTANTS.InvMix[i][k]), input[k][j]));
				}
			}
		}
		
		return out;
	}
	
	
	public void printKeysToFile(String path){
		try{
		FileWriter allKeysWrite = new FileWriter(new File(path));
		String out = "";
		
		for(String[][] key : this.AllRoundKeys){
			out += this.KG.print(key)+"\n";
		}
		
		allKeysWrite.write(out);
		allKeysWrite.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
