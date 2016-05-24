package com.mohammad.cryptography;

import com.mohammad.cryptography.AES.Tools;

public class GF_Calculator {

	private int GF_degree = 8;// GF(2^8)
	private String irrPoly = "100011011";//"101100011";// X^8 + X^4 + X^3 + X + 1

	public static void main(String[] args) {

		Tools tool = new Tools();
		GF_Calculator gfc = new GF_Calculator(8);
		//System.out.println(tool.mod("00001010", "00011011")[0]);
		
		
		//System.out.println(gfc.GF8_mult("01010100", "00001101"));//(x6 + x4 + x + 1)(x7 + x6 + x3 + x) 
	
		//System.out.println(tool.polynomial_long_division("100011011", "010000011",8)[0]);
	//1759     550
		//String[] eca = gfc.ExtendedEculedAlgorithm_GF8("100011011", "010000011");
		//System.out.println(eca[0]+"\n"+eca[1]);
		
		System.out.println(gfc.GF8_mult("00001110", "00000010"));
	
	}

	public GF_Calculator(int gF_degree) {
		GF_degree = gF_degree;
	}

	public int getGF_degree() {
		return GF_degree;
	}

	public void setGF_degree(int gF_degree) {
		GF_degree = gF_degree;
	}

	public String getIrrPoly() {
		return irrPoly;
	}

	public void setIrrPoly(String irrPoly) {
		this.irrPoly = irrPoly;
	}

	public String add(String a, String b) {
		Tools tool = new Tools();
		if (tool.getDegree(a) >= GF_degree) {
			a = tool.polynomial_long_division(a, irrPoly, 8)[0];
		}
		if (tool.getDegree(b) >= GF_degree) {
			b = tool.polynomial_long_division(b, irrPoly, 8)[0];
		}
		while (a.length() < GF_degree) {
			a = "0" + a;
		}
		while (b.length() < GF_degree) {
			b = "0" + b;
		}

		return tool.Xor(a, b);
	}

	public String subtract(String a, String b) {
		return add(a,b);
	}

	
	/*
	 * 
	 * Run the following loop eight times (once per bit). It is OK to stop when a or b are zero before an iteration:
		1-If the rightmost bit of b is set, exclusive OR the product p by the value of a. This is polynomial addition.
		2-Shift b one bit to the right, discarding the rightmost bit, and making the leftmost bit have a value of zero. This divides the polynomial by x, discarding the x0 term.
		3-Keep track of whether the leftmost bit of a is set to one and call this value carry.
		4-Shift a one bit to the left, discarding the leftmost bit, and making the new rightmost bit zero. This multiplies the polynomial by x, but we still need to take account of carry which represented the coefficient of x7.
		5-If carry had a value of one, exclusive or a with the hexadecimal number 0x1b (00011011 in binary). 0x1b corresponds to the irreducible polynomial with the high term eliminated. Conceptually, the high term of the irreducible polynomial and carry add modulo 2 to 0.
			p now has the product
	 * 
	 * */
	public String GF8_mult(String A, String B) {
		String a = A,  b = B;
		String p = "00000000", zero = "00000000";
		Tools tool = new Tools();

		if (tool.getDegree(a) >= GF_degree) {
			a = tool.polynomial_long_division(a, irrPoly, 8)[0];
		}
		if (tool.getDegree(b) >= GF_degree) {
			b = tool.polynomial_long_division(b, irrPoly, 8)[0];
		}

		while (a.length() < 8) {
			a = "0" + a;
		}
		while (b.length() < 8) {
			b = "0" + b;
		}

		char carry ;
		while (!a.equals(zero) && !b.equals(zero)) {
			//System.out.println("\tP="+p+"\ta="+a+"\tb="+b+"\tirr="+ irrPoly.substring(1));

			if (b.charAt(b.length() - 1) == '1') {
				p = tool.Xor(p, a);
			}
			
			b = "0"+b.substring(0, b.length()-1);// right shift
			carry = a.charAt(0) ;
			
			a = a.substring(1) + "0";

			if(carry == '1'){
			//	System.out.println("carry= 1 ==> "+a+"\t"+ irrPoly.substring(1));
				a = tool.Xor(a, irrPoly.substring(1));
			}
		}
//System.out.println("RESULT = "+p);
		return p;
	}

	///return [ gcd, inverse ] as String array
 	public String[] ExtendedEculedAlgorithm_GF8(String a, String b){
		
		
		
		String r_tmp_prev = a, r_tmp_curr = b, v_tmp_prev = "00000001", w_tmp_prev = "00000000",v_tmp_curr = "00000000",w_tmp_curr = "00000001"; 
		boolean done = false;
		Tools tool = new Tools();
		String[] mod_tmp;
		while(!done){
			mod_tmp = tool.polynomial_long_division(r_tmp_prev, r_tmp_curr, 8);//tool.longDiv(r_tmp_prev, r_tmp_curr);
			//System.out.println("Q = "+mod_tmp[1]+"\tr= "+mod_tmp[1]);
			String q=mod_tmp[1].length() > 8 ? mod_tmp[1].substring(mod_tmp[1].length() - 8):mod_tmp[1] ,r = mod_tmp[0].length() > 8 ? mod_tmp[0].substring(mod_tmp[0].length() - 8):mod_tmp[0],
					v = tool.Xor(v_tmp_prev, GF8_mult(q, v_tmp_curr)),
					w = tool.Xor(w_tmp_prev, GF8_mult(q, w_tmp_curr))
					;
			r_tmp_prev = r_tmp_curr;
			r_tmp_curr = r;
			v_tmp_prev = v_tmp_curr;
			v_tmp_curr = v;
			w_tmp_prev = w_tmp_curr;
			w_tmp_curr = w;
			
			if(r.equals("00000000"))
				done = true;
			
			//System.out.println("ITR:\nr= "+r+"\nv= "+v+"\nw= "+w+"\nq= "+q+"\n----------------\n");

		}
		//System.out.println("FINAL :\ngcd= "+r_tmp_prev+"\ninvers= "+w_tmp_prev+"\n----------------\n");

		return new String[]{r_tmp_prev,  w_tmp_prev};
	}
}
