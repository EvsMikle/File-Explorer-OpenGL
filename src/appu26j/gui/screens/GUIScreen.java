package appu26j.gui.screens;

import appu26j.assets.Assets;
import appu26j.gui.GUI;
import appu26j.gui.font.FontRenderer;

public abstract class GUIScreen extends GUI
{
	protected FontRenderer fontRenderer, fontRendererBig;
	protected float width = 0, height = 0;
	
	public abstract void drawScreen(float mouseX, float mouseY);
	
	public void mouseClicked(int mouseButton, float mouseX, float mouseY)
	{
		;
	}
	
	public void mouseReleased(int mouseButton, float mouseX, float mouseY)
	{
		;
	}

	public void keyPressed(int keyCode, float mouseX, float mouseY)
	{
		;
	}

	public void keyReleased(int keyCode, float mouseX, float mouseY)
	{
		;
	}

	public void onScroll(int direction)
	{
		;
	}
	
	public void initGUI(int width, int height)
	{
		this.width = width;
		this.height = height;
		this.fontRendererBig = new FontRenderer(Assets.getAsset("segoeui.ttf"), 64);
		this.fontRenderer = new FontRenderer(Assets.getAsset("segoeui.ttf"), 40);
	}
	
	protected boolean isInsideBox(float mouseX, float mouseY, float x, float y, float width, float height)
	{
		return mouseX > x && mouseX < width && mouseY > y && mouseY < height;
	}
	
	public FontRenderer getFontRenderer()
	{
		return this.fontRenderer;
	}
}
