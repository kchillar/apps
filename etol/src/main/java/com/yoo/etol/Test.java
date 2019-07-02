package com.yoo.etol;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

public class Test 
{
	//f q w x z H I M P Q U V W X Y Z
	public static void main(String[] args) throws IOException
	{				
		String str = new String("\u0C1C\u0C4D\u0C1E\u0C3E\u0C28\u0C02 \u0C1C\u0C4D\u0C1E\u0C47\u0C2F\u0C02");
		
		log("Hi: "+str);		
		//log("\u0C15\u0C33\u0C4D\u0C2F\u0C3E\u0C23\u0C30\u0C40\u0C28\u0C3F\u0C35\u0C3E\u0C38");		
		//log("\u0C15\u0C33\u0C4D\u0C2F\u0C3E\u0C23\u0C30\u0C40\u0C28\u0C3F\u0C35\u0C3E\u0C38 \u0C2D\u0C35\u0C3E\u0C28 ");		
		/*
		String str = new String("\u0C15\u0C33\u0C4D\u0C2F\u0C3E\u0C23 \u0C36\u0C4D\u0C30\u0C40\u0C28\u0C3F\u0C35\u0C3E\u0C38\u0C41 \u0C2D\u0C35\u0C3E\u0C28\u0C3F".getBytes());		
		log("\u0C15\u0C4D\u0C15 \u0C16 \u0C16\u0C4D\u0C16");		
		log("str: "+str+"\u0C15\u0C33\u0C4D\u0C2F\u0C3E\u0C23 \u0C36\u0C4D\u0C30\u0C40\u0C28\u0C3F\u0C35\u0C3E\u0C38\u0C41 \u0C2D\u0C35\u0C3E\u0C28\u0C3F");
		*/
	}
	
	public static void useStreams() throws Exception
	{
		
		byte[][] data = new byte[5][];
		String[] str = new String[5];
		
		data[0] = "k".getBytes();
		data[1] = "\u0C15".getBytes();
		data[2] = " ".getBytes();
 		data[3] = "\u0C36".getBytes();
		data[4] = "\u0C15\u0C36".getBytes();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Writer wr = new OutputStreamWriter(out, "UTF-8");
		
		for(int i=0; i<data.length; i++)
		{	
			str[i] = new String(data[i]);
			log("writing data["+i+"] = '"+str[i]+"' size: "+data[i].length);
			//out.write(data[i]);
			wr.write(str[i].charAt(0));
		}
		
		wr.flush();
		log("ByteArraySize: "+out.size());

		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		Reader rd = new InputStreamReader(in, "UTF-8");
		
		int v;
		char ch;
		
		while( (v = rd.read()) != -1)
		{
			ch = (char) v;
			log("got c: '"+ch+"' int v: "+v);
		}
		
		

	}
	
	
	public static void junk()
	{
		//log("\u0C15\u0C33\u0C4D\u0C2F\u0C06\u0C23\u0C36\u0C4D\u0C30\u0C08\u0C28\u0C07\u0C35\u0C38");
		
		//Sree
		log("\u0C36\u0C4D\u0C30\u0C40");
		//ka
		log("\u0C15");
		log("\u0C15\u0C01");
		
		//kaa
		log("\u0C15\u0C3E");
		
		//kee
		log("\u0C15\u0C40");
	
		//ka visarga na = kna
		log("\u0C15\u0C4D\u0C28");
		
		//ka visarga ka kka
		log("\u0C0E\u0C15\u0C4D\u0C15\u0C21");
		
		//ka visarga na visarga ba= knaba
		/*
		log("\u0C15\u0C4D\u0C28\u0C4D\u0C2C");
		log("\u0C15\u0C4D\u0C28\u0C4D\u0C2C\u0C40");
		log("\u0C15\u0C4D\u0C28\u0C2C\u0C40\u0C03");
		*/
		
		showCharsInt();
		
	}

	private static final void log(String msg)
	{
		System.out.println(msg);
	}

	
	private static final void sop(char c)
	{
		System.out.println(c+" = "+ ((int)c));
	}
	
	private static void showCharsInt()
	{
		sop(' ');
		sop('a');
		sop('A');
		sop('e');
		sop('E');
		sop('u');
		sop('U');
		sop('i');
		sop('I');
		sop('o');
		sop('O');
		
		sop('f');
		sop('q');
		sop('w');
		sop('z');
		sop('H');
		sop('I');
		sop('M');
		sop('Q');
		sop('V');
		sop('W');
		sop('X');
		sop('Y');
		sop('Z');
	}
	
	
	
	/*
	 * f = 102
q = 113
w = 119
z = 122
H = 72
I = 73
M = 77
Q = 81
V = 86
W = 87
X = 88
Y = 89
Z = 90
	 */
}
