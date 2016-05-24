package com.mohammad.cryptography.AES;

import java.util.Scanner;

import javax.annotation.processing.SupportedSourceVersion;

public class Tools {
	public static final String[][] conversion_table =
		{
				//Decimal     Hex     Binary
				{"0",		  "0",	  "0000"	},
				{"1",		  "1",	  "0001"	},
				{"2",		  "2",	  "0010"	},
				{"3",		  "3",	  "0011"	},
				{"4",		  "4",	  "0100"	},
				{"5",		  "5",	  "0101"	},
				{"6",		  "6",	  "0110"	},
				{"7",		  "7",	  "0111"	},
				{"8",		  "8",	  "1000"	},
				{"9",		  "9",	  "1001"	},
				{"10",		  "A",	  "1010"	},
				{"11",		  "B",	  "1011"	},
				{"12",		  "C",	  "1100"	},
				{"13",		  "D",	  "1101"	},
				{"14",		  "E",	  "1110"	},
				{"15",		  "F",	  "1111"	}
		};
	public String C_LeftShift(String st_bits, int nb_rot) {
		String out = st_bits.substring(nb_rot);
		out += st_bits.substring(0, nb_rot);
		return out;
	}

	public String C_RightShift(String st_bits, int nb_rot) {
		int len = st_bits.length();
		String out = st_bits.substring(0, len - nb_rot);
		out += st_bits.substring(len - nb_rot);
		return out;
	}



	public int binary2decimal(String bit_str) {
		int result = 0;
		int ctr = 0;
		for (int i = bit_str.length() - 1; i >= 0; i--) {
			if (bit_str.charAt(ctr) != '0' && bit_str.charAt(ctr) != '1')
				System.err.println("ERROR: Tools.binary2decimal input value is not binary!");
			result += Integer.parseInt("" + bit_str.charAt(ctr++)) * (int) Math.pow(2, i);
		}

		// System.out.println("bin_to_dec:: "+bit_str+"\t"+result);
		return result;
	}

	public String ascii2binary(char c) {
		int ascii_val = (int) c;
		String binary = "";
		while (ascii_val > 0) {
			int rem = ascii_val % 2;
			ascii_val = ascii_val / 2;
			binary = rem + binary;
		}
		while (binary.length() < 8) {
			binary = "0" + binary;
		}
		return binary;
	}
	
	public String dec2binary(int c) {
		int ascii_val = c;
		String binary = "";
		while (ascii_val > 0) {
			int rem = ascii_val % 2;
			ascii_val = ascii_val / 2;
			binary = rem + binary;
		}
		while (binary.length() < 8) {
			binary = "0" + binary;
		}
		return binary;
	}

	public String decimal2hex(int dec) {
		String hex = "";
		int val = dec;
		while (val > 0) {
			int rem = val % 16;
			val = val / 16;
			hex = conversion_table[rem][1] + hex;
		}
		while (hex.length() < 2) {
			hex = "0" + hex;
		}
		return hex;
	}

	public String hex2binary(char hex){
		char val = Character.toUpperCase(hex);
		for(int i=0;i<conversion_table.length;i++){
			if(conversion_table[i][1].equals(""+val))
			{
				return conversion_table[i][2];
			}
		}
		
		return null;
	}
	
	public String hex2binary(String hexStr){
		String out="";
		char[] str = hexStr.toCharArray();
		for(char c : str)
			out = out + hex2binary(c) ;
		
		return out;
	}
	
	public String binary2hex(String bitStr) {
		int decVal = this.binary2decimal(bitStr);
		return this.decimal2hex(decVal);
	}

	public void TextConversion(StringBuilder inputText, StringBuilder binary_out, StringBuilder hex_out,
			StringBuilder ascii_out) {
		if (inputText.length() % 8 != 0) {
			for (int i = inputText.length() % 8; i < 8; i++)
				inputText.append(' ');
		}

		char c;
		int i;
		String binary_tmp, hex_tmp, ascii_tmp;
		for (i = 0; i < inputText.length(); i++) {
			c = inputText.charAt(i);
			binary_tmp = this.ascii2binary(c);
			hex_tmp = this.binary2hex(binary_tmp);
			ascii_tmp = (int) c + "";
			binary_out.append(binary_tmp + "  ");
			hex_out.append(hex_tmp + "  ");

			while (ascii_tmp.length() < 3) {
				ascii_tmp = "0" + ascii_tmp;
			}

			ascii_out.append(ascii_tmp + "  ");

		}
	}
	public void HexConversion(StringBuilder input, StringBuilder binary_out, StringBuilder text_out,
			StringBuilder ascii_out) {
		
		String hex_in = input.toString().replaceAll(" ", "").replaceAll("\n", "");
		
		int ctr = 0;
		for(char c : hex_in.toCharArray()){
			binary_out.append(this.hex2binary(c));
			ctr++;
			if(ctr == 2){
				binary_out.append(' ');
				ctr = 0;
			}
		}

		this.BinaryConversion(binary_out, text_out, new StringBuilder(), ascii_out);
	}
	
	public void BinaryConversion(StringBuilder inputBits, StringBuilder text_out, StringBuilder hex_out,
			StringBuilder ascii_out) {
		String rem_bits = inputBits.toString().replaceAll(" ", "").replaceAll("\n", "");;
		
		
		while(rem_bits.length()>0){
			String nextByte = rem_bits.substring(0, 8);
			rem_bits = rem_bits.substring(8);
			int ascii = this.binary2decimal(nextByte);
			String hex = this.decimal2hex(ascii);
			
			String ascii_st = ascii+"";
			while (ascii_st.length() < 3) {
				ascii_st = "0" + ascii_st;
			}
			
			text_out.append(Character.toChars(ascii));
			hex_out.append(hex+" ");
			ascii_out.append(ascii_st+" ");
					
		}
		
	}

	public String[][] _128bitStringTo4x4Matrix(String _128bitStr){
		String nextByte = "", _bitKey = _128bitStr;
		if (_bitKey.length() != AES_CONSTANTS.key_length) {
			System.err.println("ERROR: KEY LENGTH MUST BE " + AES_CONSTANTS.key_length);
			return null;
		}
		String[][] key = new String[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				nextByte = _bitKey.substring(0, 8);
				_bitKey = _bitKey.substring(8);
				
				key[j][i]= nextByte;
			}
		}
		
		return key;
	}
	
	public String _4x4bitMatrix2String(String[][] in){
		String out = "";
		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				out = out + in[j][i];
			}
		}
		return out;
	}
	public String _64bitdistriputeString(String st) {
		// assume that the string st contain multiple of 8 words
		Scanner sc = new Scanner(st);
		String out = "";

		while (sc.hasNext()) {
			for (int i = 0; i < 8; i++) {
				try {
					out += sc.next() + " ";

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			out += "\n";
		}

		sc.close();
		return out;
	}

	public int getDegree(String base){
		int baseDeg = -1, ctr = 0;
		for(int i = base.length()-1 ; i>=0;i--){
			if(base.charAt(i) == '1')
				baseDeg = ctr;
			ctr++;
		}
		return baseDeg;
	}
	/*
	// return array contain  mod and  q so nbr = q base + mod
	public String[] mod(String nbr, String base){
		
		String result = nbr, N = base, zero = "";
		for(int c=0;c<base.length();c++)
			zero += '0';
		
		int baseDeg = getDegree(base), resDeg = getDegree(result);
		
		if(baseDeg == 0)
			return new String[]{zero, nbr};

		if(baseDeg > resDeg){
			return new String[]{nbr, zero};
		}
		
		result = result.substring(result.length() - resDeg - 1);
		N = N.substring(N.length() - baseDeg - 1);
		String baseTMP = N, Q = "";
		int divisor = binary2decimal(baseTMP), nbr_part =binary2decimal( result.substring(0, baseTMP.length()));

		if(nbr_part >= divisor)
			Q = Q + '1';
		else
			Q =Q + '0';
		
		while(result.length() >= baseTMP.length()){
			
			
			while(baseTMP.length() < result.length())
				baseTMP = baseTMP + "0";
			
			//System.out.println(result+"\t"+baseTMP);
			result = Xor(result, baseTMP);
			resDeg = getDegree(result);
			divisor = binary2decimal(N); nbr_part =binary2decimal( result.substring(0, baseTMP.length()));

			//remove the left zeros
			result = result.substring(result.length() - resDeg - 1);
			
			
			 baseTMP = N;
			 
			 if(nbr_part >= divisor)
					Q = Q + '1';
				else
					Q =Q + '0';
		}
		
		while(result.length() < 8)
			result = "0"+result;

		while(Q.length() < 8)
			Q = "0"+Q;
		return new String[]{result ,Q};
	}
	
	*/
	
	private String  removeLeftZeros(String s){
		int ctr = 0;
		while(s.length()>ctr&& s.charAt(ctr) == '0')
			ctr++;
		return s.substring(ctr);
	}
	/*
	public String[] longDiv(String a, String b){
		
		
		String out_rem = "", divisor = b, devidend = a;// a/b
		 String out_Q = "";
		String  zero = "";
		for(int c=0;c<b.length();c++)
			zero += '0';
		
		int baseDeg = getDegree(b), resDeg = getDegree(a);
		
		if(baseDeg == 0){
			System.out.println("OUT 1");
			return new String[]{zero, a};
		}
		if(baseDeg > resDeg){
			System.out.println("OUT 2");

			out_Q = zero;
			return new String[]{ a,zero};
		}
		
		int divisor_int = binary2decimal(divisor), dividend_int = binary2decimal(devidend);
		
		out_Q = "";
		divisor = removeLeftZeros(divisor);
		devidend = removeLeftZeros(devidend);

		while(divisor_int < dividend_int){
			
			String devidend_alin = devidend.substring(0, divisor.length()), devidend_rem = devidend.substring(divisor.length());

			
			if(divisor_int < binary2decimal(devidend_alin)){
				String sub = Xor(devidend_alin, divisor);
				devidend = sub.substring(1) + devidend_rem;
				out_Q += '1';
			}else{
				devidend = devidend_alin.substring(1) + devidend_rem;
				out_Q += '0';
			}
			
			
			divisor_int = binary2decimal(divisor); dividend_int = binary2decimal(devidend);
		}
		out_rem = devidend;

		while(out_rem.length() < 8)
			out_rem = "0"+out_rem;

		while(out_Q.length() < 8)
			out_Q = "0"+out_Q;
		return new String[]{out_rem,out_Q};
	}
	*/
	
	public String Xor(String st1, String st2) {
		String out = "";

		if (st1.length() != st2.length())
			{System.err.println("ERROR: Xor strings must be with the same length!");
				return null;
			}
		for (int i = 0; i < st1.length(); i++) {
			if (st1.charAt(i) == st2.charAt(i))
				out += '0';
			else
				out += '1';
		}
		return out;
	}
	
	
	//ALGORITHM IN : https://engineering.purdue.edu/kak/compsec/NewLectures/Lecture7.pdf
	public String[] polynomial_long_division(String N, String mod, int gf_deg){
		if(mod.length() > gf_deg +1)
		{
			System.err.println("Error::polynomial_long_division Modulus bit pattern too long");
			return null;
		}
		
		char[] qoutient = bitVector(N.length(), '0');
		String remainder = N;
		int i=0;
		boolean done = false;
		while(!done){
			i++;
			
			if(i == N.length())
				break;
			
			int mod_highest_power = mod.length() - next_set_bit(mod, 0) -1,
					remander_highest_power, exponent_shift;
			if(next_set_bit(remainder, 0) == -1)
				remander_highest_power = 0;
			else
				remander_highest_power = remainder.length() - next_set_bit(remainder, 0) -1;
			
			if(remander_highest_power < mod_highest_power || binary2decimal(remainder) == 0)
				break;
			else{
				exponent_shift = remander_highest_power - mod_highest_power;
				qoutient[qoutient.length - exponent_shift -1] = '1' ;
				String quotient_mod_product = pad_from_left(mod, remainder.length() - mod.length(), '0');
				quotient_mod_product = LeftShift(quotient_mod_product, exponent_shift);
			    
				remainder = Xor(remainder, quotient_mod_product);
			    
			    if(remainder.length() > gf_deg)
			    	remainder = remainder.substring(remainder.length() - gf_deg);
			}
		}
		
		while(remainder.length() < 8)
			remainder = "0"+remainder;

		return new String[]{remainder, new String(qoutient)};
	}

	public String ShiftRight(String s, int n){
		
		String out = s.substring(0, s.length()-1-n);
		while(out.length() < s.length())
			out = "0"+out;
		
		return out;
	}
	public String LeftShift(String s, int n){
		String out = s.substring(n);

		return pad_from_right(out, n, '0');
		
	}
	public char[] bitVector(int length, char pat){
		char[] out = new char[length];
		for(int i=0;i<length;i++)
			out[i] =pat;
		return out;
	}
	
	public int next_set_bit(String s,int after){
		int res , out = -1;
		for(res = after; res<s.length();res++){
			if(s.charAt(res) == '1')
			{
				out = res;
				break;
			}
		}
		
		return out;
	}
	
	public String pad_from_left(String s, int n, char c){
		String out = s;
		for(int i=0;i<n;i++){
			out = c + out;
		}
	return out;
	}
	
	public String pad_from_right(String s, int n, char c){
		String out = s;
		for(int i=0;i<n;i++){
			out = out+ c;
		}
	return out;
	}
	public String zero(int l){
		String out = "";
		for(int i=0;i<l;i++)
			out +='0';
		return out;
	}
}
