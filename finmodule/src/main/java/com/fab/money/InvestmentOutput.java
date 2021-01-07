package com.fab.money;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 
 * @author kalyanc
 *
 */
public class InvestmentOutput 
{
	private long paymentAmount = 100;
	private int totalTerm = 12;
	private int investTerm = 10;
	private double interestRatePerYear = 12;

	double[] interestRateInYear = new double[totalTerm];
	double[][] investAmount = new double[totalTerm][investTerm];
	double[] outAmount = new double[totalTerm];

	public InvestmentOutput()
	{		
		setupInterestRates();
	}

	public InvestmentOutput(long termAmount, int terms, int paymentTerms, double interestRate)
	{		
                paymentAmount = termAmount;
                totalTerm = terms;
		investTerm = paymentTerms;
		interestRatePerYear = interestRate;
		setupInterestRates();
	}


	public void computeAmounts() 
	{
		for (int i = 0; i < totalTerm; i++)
			for (int j = 0; j < investTerm; j++) 					
				if (i == j)
					investAmount[i][j] = paymentAmount;		

		for (int i = 1; i < totalTerm; i++) 	
			for (int j = 0; j < investTerm; j++) 							
				if (i > j) 				
					investAmount[i][j] = Math.round(investAmount[i-1][j] * (1 + getRateForYear(i)));				

		for (int i = 0; i < totalTerm; i++) 
		{
			if (i > 0) 
			{
				int end = investTerm;
				if (i < end)
					end = i;
				for (int j = 0; j < end; j++)
					outAmount[i] = outAmount[i] + investAmount[i][j];
			}
		}		 
	}

	public void displayInterestTable()
	{
		sop("Year,");
		for(int j=0; j< investTerm; j++)		
			sop("Rate["+j+"], ");
		sopln("");

		for(int i=0; i< totalTerm; i++)
		{
			if(i < totalTerm -1)
			{
				sop("Year-"+i+",");			
				for(int j=0; j< investTerm; j++)	
					if(j<investTerm-1)
						sop(getRateForYear(i)+",");
					else
						sop(getRateForYear(i)+"");
			}
			sopln("");
		}
		sopln("");
	}

	public void displayAmountTable()
	{	
		sopln("At interest rate: "+interestRatePerYear);
		sop("Year,");
		for(int j=0; j< investTerm; j++)		
			sop("Invest["+j+"],");		
		sopln(" Exit Amount");

		for(int i=0; i< totalTerm; i++)
		{
			sop("Year-"+i+",");
			for(int j=0; j< investTerm; j++)
				if(j<investTerm-1)
					sop(investAmount[i][j]+",");
				else
					sop(investAmount[i][j]+"");

			sopln(","+outAmount[i]);
		}
		sopln("");				
	}

	public void displayAmountTable2()
	{	
		sopln("At interest rate: "+interestRatePerYear);

		sop("Time --> ");		
		for(int i=0; i< totalTerm; i++)		
			sop("Year-"+i+",");		
		sopln("");

		for(int j=0; j< investTerm; j++)
		{
			sop("Amount-"+j+",");

			for(int i=0; i< totalTerm; i++)
			{
				if(j<investTerm-1)
					sop(investAmount[i][j]+",");
				else
					sop(investAmount[i][j]+",");
			}
			sopln("");
		}

		sop("Exit, ");
		for(int i=0; i< totalTerm; i++)		
			sop(outAmount[i]+",");		
		sopln("");				
	}


	public void generateAmountsFile() throws IOException
	{
		PrintWriter pr = new PrintWriter(new File("amounts-at-rate-"+interestRatePerYear+".csv"));		
		pr.println("Interest Rate,"+interestRatePerYear);

		pr.print("Year,");
		pr.print("Exit Amount,");
		for(int j=0; j< investTerm; j++)		
			pr.print("Amount-"+(j+1)+",");		

		pr.println();

		for(int i=0; i< totalTerm; i++)
		{						
			pr.print("Year-"+i+",");
			pr.print(outAmount[i]+",");
			for(int j=0; j< investTerm; j++)
				if(j<investTerm-1)
					pr.print(investAmount[i][j]+",");
				else
					pr.print(investAmount[i][j]+"");

			pr.println();
		}

		pr.flush();
		pr.close();
	}

	private double getRateForYear(int year)
	{
		return interestRateInYear[year];
	}

	private void setupInterestRates()
	{
		for (int i = 0; i < totalTerm; i++)		
			interestRateInYear[i] = interestRatePerYear/100.0;		
	}

	public static void sop(String line) 
	{
		System.out.print(line);
	}

	public static void sopln(String line) 
	{
		System.out.println(line);
	}


	public static void main(String[] args) throws IOException
	{
	        System.out.println("Need four arguments: AmountPerTerm TotalTerms NumberOfPaymentTerms ratePerYear");	
		long perTermAmount = Long.parseLong(args[0]);
                int totalTerms = Integer.parseInt(args[1]);
		int noOfPaymentTerms = Integer.parseInt(args[2]);
                double ratePerYear = Double.parseDouble(args[3]);

		InvestmentOutput io = new InvestmentOutput(perTermAmount, totalTerms, noOfPaymentTerms, ratePerYear);
		io.computeAmounts();
		io.displayAmountTable2();
	}
}

