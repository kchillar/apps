package com.ajoy.etol.app;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.Document;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author Kalyana Chillara<br>
 *
 */
public class TextPanel extends JPanel implements ActionListener, KeyListener, MouseListener
{	
	private static Logger log = LogManager.getLogger(TextPanel.class);
	private JFrame parent;
	private File file;
	private JTextArea textArea;
	private JTextField textField;

	public TextPanel(JFrame frame, File file)
	{		
		this.setLayout(new BorderLayout());		
		textArea = new JTextArea();
		textArea.setEditable(true);
		textArea.addMouseListener(this);
		textArea.addKeyListener(this);
		
		textField = new JTextField();		
		this.parent = frame;
		this.file = file;
		this.add(textArea, BorderLayout.CENTER);
		this.add(textField, BorderLayout.SOUTH);		
		textField.addActionListener(this);
		init();		
	}

	private void init()
	{
		try 
		{  
			if(file == null)
				return;			
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;							
			while((line = br.readLine())!=null)			
				textArea.append(line+"\n");			
			br.close();			
			System.out.println(file.getPath()+" has lines: "+textArea.getLineCount());
		} 
		catch (Exception evt) 
		{ 
			JOptionPane.showMessageDialog(parent, evt.getMessage()); 
		} 
	}

	public void saveToFile()
	{
		try 
		{ 			
			BufferedWriter w = new BufferedWriter( new FileWriter(file, false)); 				
			Document document = textArea.getDocument();		
			w.write(document.getText(document.getStartPosition().getOffset(), document.getEndPosition().getOffset()));				
			w.flush(); 
			w.close();
			textField.setText("Wrote file: "+file.getPath()+" size: "+file.length());			
		} 
		catch (Exception evt) 
		{ 
			JOptionPane.showMessageDialog(parent, evt.getMessage()); 
		} 
	}

	private void showCursorPositionsInTextArea()
	{
		try
		{
			int cPos = textArea.getCaretPosition();	
			int lineNumber = textArea.getLineOfOffset(cPos);
			int startPos = textArea.getLineStartOffset(lineNumber);
			int endPos = textArea.getLineEndOffset(lineNumber);
			System.out.println("Cursor at:"+cPos+", is in line:"+lineNumber+", and line startPos:"+startPos+", endPos:"+endPos);
		}
		catch(Exception exp)
		{
			log.error("error", exp);
		}
	}

	public void actionPerformed(ActionEvent e) 
	{
		JTextField tf = (JTextField) e.getSource();
		try
		{			
			showCursorPositionsInTextArea();
			System.out.println("tf.getText() "+ tf.getText());
			String lString = E2LUtil.getLanguageString(tf.getText());			
			int pos = textArea.getCaretPosition();
			textArea.insert(lString, pos);	
			textField.setText("");
		}
		catch(Exception exp)
		{
			log.error("error", exp);
		}
	}

	public void handleMenuEvent(String eventName)
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
				JOptionPane.showMessageDialog(this, evt.getMessage()); 
			} 
		} 
	}

	public void mouseClicked(MouseEvent e) 
	{		
		try
		{
			int cPos = textArea.getCaretPosition();	
			int lineNumber = textArea.getLineOfOffset(cPos);
			int startPos = textArea.getLineStartOffset(lineNumber);
			int endPos = textArea.getLineEndOffset(lineNumber);

			String line = textArea.getText(startPos, endPos-startPos);
			textField.setText(line);
			
			showCursorPositionsInTextArea();
		}
		catch(Exception exp)
		{
			log.error("Error", exp);
		}		
	}
	public void mousePressed(MouseEvent e) {
	}
	public void mouseReleased(MouseEvent e) {
	}
	public void mouseEntered(MouseEvent e) {		
	}
	public void mouseExited(MouseEvent e) {
	}

	public void keyTyped(KeyEvent e) 
	{
	}
	boolean cntrlPressed = false;
	public void keyPressed(KeyEvent e) 
	{		
		if((e.getKeyCode() == KeyEvent.VK_SPACE))
		{
			if(cntrlPressed)			
				handleCntrlSpace();
		}
		else if(e.getKeyCode() == KeyEvent.VK_CONTROL)
			cntrlPressed = true;
	}
	public void keyReleased(KeyEvent e) 
	{
		if(e.getKeyCode() == KeyEvent.VK_CONTROL)
			cntrlPressed = false;
	}
	
	private void handleCntrlSpace()
	{		
		try
		{
			textField.setText("Pressed control space: ");
			int cp = textArea.getCaretPosition();	
			int lno = textArea.getLineOfOffset(cp);
			int sp = textArea.getLineStartOffset(lno);
			int ep = textArea.getLineEndOffset(lno);
			String line = textArea.getText(sp, ep-sp);
			char[] arr = line.toCharArray();
			int ci = cp - sp -1;			
			if(ci > 0)
			{
				boolean foundNonWhitespaceChar = false;
				int i=ci;
				for(i=ci; i>=0; i--)
				{
					//System.out.println("char["+i+"]: "+arr[i]);					
					if(Character.isWhitespace(arr[i]))
					{
						if(foundNonWhitespaceChar)
							break;
					}
					else
						foundNonWhitespaceChar = true;						
				}				
				int st = i+1;
				int ln = ci-st+1;				
				//System.out.println("st: "+st+" len: "+ln);
				String oldStr = new String(arr,st, ln);
				String lStr = E2LUtil.getLanguageString(oldStr);				
				//System.out.println("Replace: "+oldStr+" new: "+lStr);				
				textArea.getDocument().remove(sp+st, ln);				
				textArea.getDocument().insertString(sp+st, lStr, null);
			}
		}
		catch(Exception exp)
		{
			log.error("Error", exp);
		}		


	}
}
