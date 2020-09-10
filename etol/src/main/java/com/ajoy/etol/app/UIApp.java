package com.ajoy.etol.app;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 

/**
 * 
 * @author Kalyana Chillara<br>
 *
 */
public class UIApp extends JFrame implements ActionListener 
{  	
	private static Logger log = LogManager.getLogger(UIApp.class);
	private TextPanel textPanel;
	private File lastFileDialogDir = new File(System.getProperty("user.dir"));
	 
	public UIApp() 
	{ 
		super("E2L - Editor"); 
		
		try 
		{ 
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel"); 		
			MetalLookAndFeel.setCurrentTheme(new OceanTheme()); 
		} 
		catch (Exception e) 
		{ 
		} 
				
		JMenuBar mb = setupMenuBar();
		this.setJMenuBar(mb); 
		this.setSize(500, 500); 
		this.show(); 
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
		String s = e.getActionCommand(); 

		if (s.equals("Open")) 
		{ 
			JFileChooser j = new JFileChooser(lastFileDialogDir); 
			int r = j.showOpenDialog(null); 
			if (r == JFileChooser.APPROVE_OPTION) 
			{												
				File file = new File(j.getSelectedFile().getAbsolutePath());				
				if(file.isFile() && file.exists())
				{
					lastFileDialogDir = file.getParentFile();				
					if(textPanel != null)					
						this.remove(textPanel);					
					this.add((textPanel = new TextPanel(this, file)));
				}
			}  
			else
				JOptionPane.showMessageDialog(this, "Cancelled open"); 
		} 
		else if (s.equals("Save")) 
		{ 
			if(textPanel != null)
				textPanel.saveToFile();
		} 
		else  
		{ 
			if(textPanel != null)
				textPanel.handleMenuEvent(s);
		} 
	} 

	public static void main(String args[]) 
	{ 
		E2LUtil.init();
		UIApp e = new UIApp(); 
	} 
} 
