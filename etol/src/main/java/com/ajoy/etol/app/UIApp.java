package com.ajoy.etol.app;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ajoy.etol.Transliterator;
import com.ajoy.etol.mapper.CharSequenceMapperProvider; 

/**
 * 
 * @author Kalyana Chillara<br>
 *
 */
public class UIApp extends JFrame implements ActionListener 
{  	
	private static Logger log = LogManager.getLogger(UIApp.class);	
	private UIActionHandler handler;
	private File lastFileDialogDir = new File(System.getProperty("user.dir"));
	private String currentLanguage = Transliterator.LANGUAGE_TELUGU;
	private Transliterator transliterator;	
	private JScrollPane pane;
	private JTextArea textArea;
	private JTextField textField = new JTextField();
	private UndoHandler undoHandler = new UndoHandler();
	private File currentFile;
	 
	public UIApp() 
	{ 
		super("E2L - Editor"); 
		
		try 
		{
			textArea = new JTextArea();
			//textArea.setColumns(100);
			textArea.setFont(textArea.getFont().deriveFont(16f));
			
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			transliterator = Transliterator.getInstance(currentLanguage);
			handler = new UIActionHandler(this, textArea, textField, transliterator);			
			textArea.setText("Welcome to E2L - Editor");
			undoHandler.setupUndoFunctionality(textArea);
			pane = new JScrollPane(textArea);
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel"); 		
			MetalLookAndFeel.setCurrentTheme(new OceanTheme()); 
			Dimension dim =  new Dimension(650, 150);
			JMenuBar mb = setupMenuBar();						
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setLayout(new BorderLayout());
			this.add(mb, BorderLayout.NORTH);
			this.add(pane, BorderLayout.CENTER);
			this.add(textField, BorderLayout.SOUTH);
			this.setSize(500, 500); 		
			this.setLocation(dim.width/2-this.getPreferredSize().width/2, dim.height/2-this.getPreferredSize().height/2);
			this.setVisible(true); 			
		} 
		catch (Exception e) 
		{ 
			log.error("Error",e);
		} 
	} 
	
	public Transliterator getTransliterator()
	{
		return transliterator;
	}
	
	
	private JMenuBar setupMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();  
		JMenu fileMenu = new JMenu("File"); 
		fileMenu.setMnemonic('F');
		JMenuItem fNew = new JMenuItem("New"); 
		fNew.setMnemonic('N'); 
		JMenuItem fOpen = new JMenuItem("Open");
		fOpen.setMnemonic('O');
		fOpen.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.Event.CTRL_MASK));
		JMenuItem fSave = new JMenuItem("Save");
		fSave.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.Event.CTRL_MASK)); 
		fSave.setMnemonic('S');		
		JMenuItem fPrint = new JMenuItem("Print");
		fPrint.setMnemonic('P');

		fNew.addActionListener(this); 
		fOpen.addActionListener(this); 
		fSave.addActionListener(this); 
		fPrint.addActionListener(this); 

		fileMenu.add(fNew); 
		fileMenu.add(fOpen); 
		fileMenu.add(fSave); 
		fileMenu.add(fPrint); 

		JMenu editMenu = new JMenu("Edit");  
		editMenu.setMnemonic('E');
		JMenuItem cut = new JMenuItem("cut"); 		
		cut.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.Event.CTRL_MASK)); 				
		JMenuItem copy = new JMenuItem("copy"); 
		copy.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.Event.CTRL_MASK));		
		JMenuItem paste = new JMenuItem("paste"); 
		paste.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.Event.CTRL_MASK));
		
		cut.addActionListener(this); 
		copy.addActionListener(this); 
		paste.addActionListener(this); 
		editMenu.add(cut); 
		editMenu.add(copy); 
		editMenu.add(paste); 

		JMenuItem closeMenu = new JMenuItem("close"); 
		closeMenu.addActionListener(this); 
		menuBar.add(fileMenu); 
		menuBar.add(editMenu); 
		menuBar.add(closeMenu); 
		return menuBar;
	}
	
	public void actionPerformed(ActionEvent e) 
	{ 
		System.out.println("Got Action: "+e.getActionCommand());
		
		String s = e.getActionCommand(); 

		if (s.equals("Open")) 
		{ 
			File file = fileDialogue(true);				
			if(file !=null && file.isFile() && file.exists())
			{
				lastFileDialogDir = file.getParentFile();				
				if(handler.handleFileOpen(file))
					currentFile = file;
			}
		} 
		else if (s.equals("Save")) 
		{ 
			if(currentFile!=null)
				handler.handleSaveFile(currentFile);
		}
		else if (s.equals("New")) 
		{
			File file = fileDialogue(false);
			if(!file.exists())
			{
				lastFileDialogDir = file.getParentFile();
				currentFile = file;
				handler.handleSaveFile(file);				
			}			
		} 		
		else  
		{ 
			if(handler != null)
				handler.handleMenuEvent(s);
		} 
	} 
	
	private File fileDialogue(boolean forExistingFile)
	{
		File file = null;
		JFileChooser j = new JFileChooser(lastFileDialogDir);		
		j.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int r = -1;		
		if(forExistingFile)
			r = j.showOpenDialog(this);
		else
			r = j.showDialog(this, "New");
		
		if (r == JFileChooser.APPROVE_OPTION) 														
			file = new File(j.getSelectedFile().getAbsolutePath());												
		else
			JOptionPane.showMessageDialog(this, "FileName Cancelled!!!"); 
		return file;
	}
	
	public static void main(String args[]) 
	{ 
		
		CharSequenceMapperProvider.init();
		E2LUtil.init();
		UIApp e = new UIApp(); 
	} 
	

} 
