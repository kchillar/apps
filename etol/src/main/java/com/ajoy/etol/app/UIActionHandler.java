package com.ajoy.etol.app;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.Document;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ajoy.etol.Transliterator;

/**
 * 
 * @author Kalyana Chillara<br>
 *
 */
public class UIActionHandler implements KeyListener
{	
	private static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd aa hh:mm:ss");
	
	private static Logger log = LogManager.getLogger(UIActionHandler.class);
	private JFrame parent;	
	private JTextArea textArea;
	private JTextField textField;
	private Transliterator transliterator;

	public UIActionHandler(JFrame frame, JTextArea textArea, JTextField textField, Transliterator transliterator)
	{	
		this.parent = frame;		
		this.textArea = textArea; 
		textArea.setEditable(true);
		textArea.addKeyListener(this);
		this.textField = textField;
		this.textField.setEditable(false);
		this.transliterator = transliterator;
	}

	public boolean handleFileOpen(File file)
	{
		try 
		{  
			if(file == null)
				return false;		

			textArea.setText("");			
			
			if(!file.exists())
			{
				file.createNewFile();
				displayMsg("file: "+file.getPath()+" does not exist, created new");
				textArea.setCaretPosition(0);
			}
			else
			{
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line;					
				while((line = br.readLine())!=null)			
				textArea.append(line+"\n");			
				br.close();			
				displayMsg("Opened file: "+file.getPath()+" size: "+file.length()+" lines: "+textArea.getLineCount());
			}
			
			return true;
		} 
		catch (Exception evt) 
		{ 
			JOptionPane.showMessageDialog(parent, evt.getMessage()); 
			evt.printStackTrace();
			return false;
		} 
		finally
		{
			cntrlPressed = false;
		}
	}

	public void handleSaveFile(File file)
	{
		try 
		{ 			
			BufferedWriter w = new BufferedWriter( new FileWriter(file, false)); 				
			Document document = textArea.getDocument();		
			w.write(document.getText(document.getStartPosition().getOffset(), document.getEndPosition().getOffset()));				
			w.flush(); 
			w.close();
			displayMsg("Saved file: "+file.getPath()+" size: "+file.length()); 			
		} 
		catch (Exception evt) 
		{ 
			JOptionPane.showMessageDialog(parent, evt.getMessage()); 
		} 
		finally
		{
			cntrlPressed = false;
		}
	}

	public void handleMenuEvent(String eventName)
	{
		try
		{
			if (eventName.equals("cut")) 
			{ 
				textArea.cut(); 
			} 
			else if (eventName.equals("copy")) 
			{ 
				textArea.copy(); 
			} 
			else if (eventName.equals("paste")) 
			{ 
				textArea.paste(); 
			} 
			else if (eventName.equals("Print")) 
			{ 
				try 
				{ 
					textArea.print(); 
				} 
				catch (Exception evt) 
				{ 
					JOptionPane.showMessageDialog(parent, evt.getMessage()); 
				} 
			} 
		}
		finally
		{
			cntrlPressed = false;
		}
	}

	public void keyTyped(KeyEvent e) 
	{
	}

	boolean altPressed = false;
	boolean cntrlPressed = false;

	public void keyPressed(KeyEvent e) 
	{		
		if((e.getKeyCode() == KeyEvent.VK_SPACE))
		{
			if(altPressed)
			{
				//textField.setText("1) cntrlPressed: "+cntrlPressed+", altPressed: "+altPressed+", SPACE");				
				transliterate(false, '\0');
			}			
			else //if(cntrlPressed)
			{
				//textField.setText("0) cntrlPressed: "+cntrlPressed+", altPressed: "+altPressed+", SPACE");			
				transliterate(true, '\0');
				//transliterate(true, ' ');
			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_ALT)
		{
			altPressed = true;
			//textField.setText("2) altPressed:"+altPressed+", cntrlPressed:"+cntrlPressed);
		}	
		else if(e.getKeyCode() == KeyEvent.VK_CONTROL)		
		{
			cntrlPressed = true;
			//textField.setText("3) cntrlPressed:"+cntrlPressed+", altPressed:"+altPressed);			
		}					
	}

	public void keyReleased(KeyEvent e) 
	{
		if(e.getKeyCode() == KeyEvent.VK_CONTROL)
		{
			cntrlPressed = false;
			//textField.setText("4) cntrlPressed:"+cntrlPressed);
		}		
		else if(e.getKeyCode() == KeyEvent.VK_ALT)
		{
			altPressed = false;
			//textField.setText("5) altPressed:"+altPressed);
		}
	}


	private void transliterate(boolean fromAsciToLanguage, char appendChar)
	{		
		try
		{			
			int cp = textArea.getCaretPosition();	
			int lno = textArea.getLineOfOffset(cp);
			int sp = textArea.getLineStartOffset(lno);
			int ep = textArea.getLineEndOffset(lno);
			String line = textArea.getSelectedText(); 	
			
			System.out.println("cp: "+cp+", lno: "+lno+", sp: "+sp+", ep: "+ep+", line: "+line);
			
			char[] arr = null;
			int st = 0;
			int ln = 0;
			String oldStr = null;
			String newStr = null;

			if(line == null)
			{				
				line = textArea.getText(sp, ep-sp);										
				arr = line.toCharArray();
				int ci = cp - sp;			
				if(ci > 0 && ci<arr.length)
				{
					while(Character.isWhitespace(arr[ci]))				
						ci--;
					int i=ci;
					for(i=ci; i>=0; i--)								
						if(Character.isWhitespace(arr[i]))											
							break;														
					st = i+1;
					ln = ci-st+1;				
					oldStr = new String(arr,st, ln);									
				}
			}
			else
			{				
				st = textArea.getSelectionStart() - sp ;				
				int end = textArea.getSelectionEnd() - sp ;
				ln = end - st;
				arr = line.toCharArray();
				//System.out.println("selected text:|"+line+"| tas: "+textArea.getSelectionStart()+",tae: "+textArea.getSelectionEnd()+" st:"+st+" ln: "+ln);
				oldStr = new String(arr,st, ln);
			}
						
			if(oldStr != null)
			{
				if(fromAsciToLanguage)
					newStr = transliterator.toLanguageString(oldStr);
				else
					newStr = transliterator.toPhoneticString(oldStr);	
												
				if(appendChar != '\0')
					newStr = newStr + appendChar;
				
				//System.out.println("oldStr:|"+oldStr+"| sp: "+sp+", st:"+st+", ln:"+ln);
				//System.out.println("newStr:|"+newStr+"| sp: "+sp+", st:"+st+", ln:"+newStr.length());
				
				System.out.println(oldStr+" == "+ newStr);
				textArea.getDocument().remove(sp+st, ln);				
				textArea.getDocument().insertString(sp+st, newStr, null);
				
			}
			else
			{
				displayMsg("Old: null");				
			}
		}
		catch(Exception exp)
		{
			log.error("Error", exp);
		}		
	}

	
	private final void displayMsg(String msg)
	{
		textField.setText("["+dateFormatter.format(new Date())+"] "+msg);
	}
	
}
