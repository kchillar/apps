package com.yoo.etol.mapper;

import com.yoo.etol.config.CharSequenceToCodePointMapping;

public interface LanguageToEnglishCharSequenceMapper 
{
	public int getVisarga();
	public CharSequenceToCodePointMapping getVisargaSymbol();
	public CharSequenceToCodePointMapping getAsciSequence(int codepoint);
}
