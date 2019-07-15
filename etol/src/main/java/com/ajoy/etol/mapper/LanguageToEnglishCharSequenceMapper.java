package com.ajoy.etol.mapper;

import com.ajoy.etol.config.CharSequenceToCodePointMapping;

public interface LanguageToEnglishCharSequenceMapper 
{
	public int getVisarga();
	public CharSequenceToCodePointMapping getVisargaSymbol();
	public CharSequenceToCodePointMapping getAsciSequence(int codepoint);
}
