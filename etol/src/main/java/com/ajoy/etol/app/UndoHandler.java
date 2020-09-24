package com.ajoy.etol.app;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

public class UndoHandler 
{
	public UndoHandler()
	{
	}
	
	public void setupUndoFunctionality(JTextArea textArea)
	{		
	    JButton undo = new JButton("Undo");
	    JButton redo = new JButton("Redo");
	    KeyStroke undoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK);
	    KeyStroke redoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK);
	    UndoManager undoManager = new UndoManager();
	    Document document = textArea.getDocument();
	    document.addUndoableEditListener(new UndoableEditListener() {
	        @Override
	        public void undoableEditHappened(UndoableEditEvent e) {
	            undoManager.addEdit(e.getEdit());
	        }
	    });
	    	    
	    // Add ActionListeners
	    undo.addActionListener((ActionEvent e) -> {
	        try {
	            undoManager.undo();
	        } catch (CannotUndoException cue) {}
	    });
	    
	    redo.addActionListener((ActionEvent e) -> {
	        try {
	            undoManager.redo();
	        } catch (CannotRedoException cre) {}
	    });
	    	   
	    // Map undo action
	    textArea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
	            .put(undoKeyStroke, "undoKeyStroke");
	    textArea.getActionMap().put("undoKeyStroke", new AbstractAction() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            try {
	                undoManager.undo();
	             } catch (CannotUndoException cue) {}
	        }
	    });
	    // Map redo action
	    textArea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
	            .put(redoKeyStroke, "redoKeyStroke");
	    textArea.getActionMap().put("redoKeyStroke", new AbstractAction() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            try {
	                undoManager.redo();
	             } catch (CannotRedoException cre) {}
	        }
	    });
	}
}
