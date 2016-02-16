package com.example.xstream.whitelisting.inconsistency;

import java.awt.Color;
import java.awt.Font;

public class TextParameters {
	public static final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 12);
	public static final Color DEFAUT_TEXT_COLOR = Color.BLACK;

	public enum Position {
		TOP, BOTTOM
	}

	private Position position = Position.BOTTOM;
	private String text;
	private Font font = DEFAULT_FONT;
	private Color textColor = DEFAUT_TEXT_COLOR;

    public Position getPosition()
    {
        return position;
    }

    public void setPosition(Position position)
    {
        this.position = position;
    }

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public Color getTextColor() {
		return textColor;
	}

	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
