package com.ajoy.etol.tmp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedList;

public class CodePointTester 
{
	public static void main(String[] args) throws Exception
	{
		char[] list = {'<', '>', '/', '%' };
		
		for(char c: list)				
		 System.out.println("char: '"+c+"' int = "+((int)c)+" hex: "+ (Integer.toHexString(c)));
	}
	
	public static void main1(String[] args) throws Exception
	{
		System.out.println(" java -cp CodePointTester ./inputCodePoints.txt ./output.txt");
		
		String inputFile = args[0];
		String outputFile = args[1];
		
		BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)));
		
		LinkedList<Integer> list = new LinkedList<Integer>();
		
		String line;
		
		while((line = br.readLine()) != null)
		{
			int codePoint;
			
			if(line.trim().length() != 0)
				codePoint = Integer.parseInt(line.trim());
			else							
				codePoint = " ".codePointAt(0);			
			
			System.out.println("Adding code point: "+codePoint+" for: '"+line+"'");
			
			list.add(codePoint);
		}
		
		Writer wr = new OutputStreamWriter(new FileOutputStream(new File(outputFile)), "UTF-8"); 
		
		for(Integer codepoint: list)
			wr.write(codepoint);
		
		wr.close();
		br.close();
	}
}
