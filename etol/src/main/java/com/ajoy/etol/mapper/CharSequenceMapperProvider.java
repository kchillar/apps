package com.ajoy.etol.mapper;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ajoy.etol.config.CharSequenceMapperProviderConfig;
import com.ajoy.etol.config.LanguageInfo;
import com.ajoy.etol.config.Settings;
import com.ajoy.etol.config.TransliteratorConfig;
import com.ajoy.etol.util.XMLHelper;

public class CharSequenceMapperProvider 
{
	private static Logger log = LogManager.getLogger(CharSequenceMapperProvider.class);
	private static CharSequenceMapperProvider singleton = new CharSequenceMapperProvider();	
	private Map<String, TransliterationMapper> map = new HashMap<String, TransliterationMapper>();	
	private CharSequenceMapperProviderConfig config;
	
	private CharSequenceMapperProvider()
	{		
	}
	
	public static void init()
	{
		try
		{
			//File file = new File("/Users/kalyanc/CodeRepos/apps/etol/conf/english-to-telugu-transliterator-config.xml");
			InputStream in = CharSequenceMapperProvider.class.getResourceAsStream("/char-sequence-mapper-provider-config.xml");
			CharSequenceMapperProviderConfig config = (CharSequenceMapperProviderConfig) XMLHelper.getObjectFromInputStream(CharSequenceMapperProviderConfig.class, in);
			in.close();
			singleton.init(config);
			
			if(Settings.EnableLogs)
			{			
			}
		}
		catch(Exception exp)
		{
			exp.printStackTrace();
		}
	}
	
	private void init(CharSequenceMapperProviderConfig aConfig)
	{
		config = aConfig;
		try
		{
			if(config.getLanguageInfoList() != null)
			{
				for(LanguageInfo lInfo: config.getLanguageInfoList())
				{
					log.info("initing mapper for language: "+lInfo.getCode());
					InputStream in = CharSequenceMapperProvider.class.getResourceAsStream("/"+lInfo.getCode()+".xml");
					TransliteratorConfig mapperConfig = (TransliteratorConfig) XMLHelper.getObjectFromInputStream(TransliteratorConfig.class, in);
					in.close();					
					TransliterationMapper mapper = new TransliterationMapperImpl(mapperConfig);	
					System.out.println("Check here for markup hardcoding");
					//((TransliterationMapperImpl)mapper).setTransliterationMarkedup(false);
					map.put(lInfo.getCode(), mapper);
				}
			}
		}
		catch(Exception exp)
		{
			exp.printStackTrace();
		}
	}
	
	public static TransliterationMapper getCharSequenceMapper(String languageCode)
	{
		return singleton.getMap().get(languageCode);
	}

	public Map<String, TransliterationMapper> getMap() {
		return map;
	}

	public void setMap(Map<String, TransliterationMapper> map) {
		this.map = map;
	}
	
	
}
