package com.ajoy.test;

import java.util.List;

import javafx.scene.control.ChoiceDialog;

public class ChoiceDialogBox<T> extends ChoiceDialog<T>
{
	private List<T> choices;
	
	public ChoiceDialogBox(List<T> choiceList)
	{
		choices = choiceList;		
	}
	
}
