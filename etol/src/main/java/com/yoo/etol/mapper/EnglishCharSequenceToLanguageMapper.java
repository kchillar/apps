package com.yoo.etol.mapper;

import com.yoo.etol.config.CharSequenceToCodePointMapping;

public interface EnglishCharSequenceToLanguageMapper 
{
	
	public boolean isTransliterationMarkedup();
	public String getMarkupStart();
	public String getMarkupEnd();

	public int getVisarga();			
	public CharSequenceToCodePointMapping getVowelSymbol(String asciiCharSequence);
	public CharSequenceToCodePointMapping getConsonantSymbol(String asciiCharSequence) ;
	
	
	public boolean isAsciiCharUsedForLanguageVowel(int c);
	public boolean isAsciiCharUsedForLanguageConsonant(int c);
	public boolean isAsciiCharPassThrough(int c);
	public boolean isAsciiCharUsedInMarkup(int c);
}
