package com.example.xstream.whitelisting.inconsistency;

public class ImageParameters {
	private byte[] image;
	private float x;
	private float y;
	private TextParameters textParameters;

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

    public float getX()
    {
        return x;
    }

    public void setX(float x)
    {
        this.x = x;
    }

    public float getY()
    {
        return y;
    }

    public void setY(float y)
    {
        this.y = y;
    }
    
	public TextParameters getTextParameters() {
		return textParameters;
	}

	public void setTextParameters(TextParameters textParameters) {
		this.textParameters = textParameters;
	}
}
