package com.ajoy.etol.mapper;

import com.ajoy.etol.config.CharSequenceToCodePointMapping;

public interface TransliterationMapper 
{
	public boolean isTransliterationMarkedup();
	public String getMarkupStart();
	public String getMarkupEnd();

	public boolean isAsciiCharUsedForLanguageVowel(int asciChar);
	public boolean isAsciiCharUsedForLanguageConsonant(int asciChar);
	public boolean isAsciiCharPassThrough(int asciChar);
	public boolean isAsciiCharUsedInMarkup(int asciChar);
	
	public int getModifierCodepoint();			
	
	public CharSequenceToCodePointMapping getLanguageAcchuUnicode(String asciiCharSequence);
	public CharSequenceToCodePointMapping getLanguageHalluUnicode(String asciiCharSequence) ;
		
	//Given a language unicode code point, get the correspoinding ascii char sequence.
	public CharSequenceToCodePointMapping getAsciSequence(int langCharCodePoint);
}
