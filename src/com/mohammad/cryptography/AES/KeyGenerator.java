package com.mohammad.cryptography.AES;

import java.util.ArrayList;
import java.util.List;

public class KeyGenerator {

	private List<String[][]> AllKeys;
	private String _128bitKey;

	public KeyGenerator(String _128bitKey) {
		this._128bitKey = _128bitKey;
		AllKeys = new ArrayList<>();

		generatKeys();
	}
	
	public List<String[][]> getAllKeys() {
		return AllKeys;
	}

	public static void main(String[] args){
		String hexKey = "0f1571c947d9e8590cb7add6af7f6798";
		Tools tool = new Tools();
		String binaryKey = tool.hex2binary(hexKey);
		System.out.println("KEY : "+binaryKey+"\nSIZE = "+binaryKey.length());
		KeyGenerator kg = new KeyGenerator(binaryKey);
		
		for(String[][] key : kg.getAllKeys()){
			kg.print(key);
		}
	}

	public void generatKeys() {

		String nextByte = "", _bitKey = this._128bitKey;
		if (_bitKey.length() != AES_CONSTANTS.key_length) {
			System.err.println("ERROR: KEY LENGTH MUST BE " + AES_CONSTANTS.key_length);
			return;
		}
		String[][] key = new String[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				nextByte = _bitKey.substring(0, 8);
				_bitKey = _bitKey.substring(8);
				
				key[j][i]= nextByte;
			}
		}
		String[][] expKeys = keyExpansion(key);
		for(int i=0;i<11;i++){
			String[][] key_i = new String[4][4];
			for(int c = 0;c<4;c++){
				for(int r = 0;r<4;r++){
					key_i[r][c] = expKeys[r][i*4 + c];
				}
			}
			
			AllKeys.add(key_i);
		}
	}
	
	public String[][] keyExpansion(String[][] key){
		String[][] outKeys = new String[4][44];
		String[] word_tmp = new String[4];
		Tools tool  = new Tools();
		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				outKeys[j][i] = key[j][i];
			}
		}
		
		for(int i=4;i<44;i++){
			
			for(int wti=0;wti<4;wti++){
				word_tmp[wti] = outKeys[wti][i-1];
			}
			
			if(i%4 == 0){
				String[] rot = RotWord(word_tmp);
				
				String[] Rcon = new String[4];
				Rcon[0] = tool.hex2binary(AES_CONSTANTS.RC[i/4 - 1]);
				for(int wti = 0;wti<4;wti++){
					if(wti > 0)
						Rcon[wti] = "00000000";
					word_tmp[wti] = tool.Xor(AES_CONSTANTS.Substitution(rot[wti]), Rcon[wti]);
				}
				
				
			}
			
			for(int wti=0;wti<4;wti++){
				 outKeys[wti][i] = tool.Xor(outKeys[wti][i-4], word_tmp[wti]);
			}
		}
		
		return outKeys;
	}
	
	public String[] RotWord(String[] word){
		String tmp = word[0];
		String[] out = new String[word.length];
		for(int i=1;i<word.length;i++){
			out[i-1] = word[i];
		}
		out[word.length -1 ]= tmp;
		
		return out;
	}
	
	public String print(String[][] key){
		String out="\n";
		Tools tool = new Tools();
		for(int i=0;i<key.length;i++){
			for(int j=0;j<key[0].length ; j++){
				out = out + tool.binary2hex(key[i][j])+",\t";
			}
			out += "\n";
		}
		
		System.out.println(out);
		return out;
	}
	
	public void print(String[] word){
		Tools tool = new Tools();
		String out = "";
		for(int j=0;j<word.length ; j++){
			out = out + tool.binary2hex(word[j])+",\t";
		}
		
		System.out.println("WORD: "+out);
	}
}
