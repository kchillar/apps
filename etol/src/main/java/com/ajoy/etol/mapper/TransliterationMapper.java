package com.ajoy.etol.mapper;

import com.ajoy.etol.config.CharSequenceToCodePointMapping;

public interface TransliterationMapper 
{
	public boolean isTransliterationMarkedup();
	public String getMarkupStart();
	public String getMarkupEnd();

	public int getModifierCodepoint();			
	public CharSequenceToCodePointMapping getVowelSymbol(String asciiCharSequence);
	public CharSequenceToCodePointMapping getConsonantSymbol(String asciiCharSequence) ;
		
	public boolean isAsciiCharUsedForLanguageVowel(int asciChar);
	public boolean isAsciiCharUsedForLanguageConsonant(int asciChar);
	public boolean isAsciiCharPassThrough(int asciChar);
	public boolean isAsciiCharUsedInMarkup(int asciChar);
	
	public CharSequenceToCodePointMapping getAsciSequence(int langCharCodePoint);
}
