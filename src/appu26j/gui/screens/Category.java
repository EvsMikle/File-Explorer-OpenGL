package appu26j.gui.screens;

import appu26j.gui.GUI;
import appu26j.gui.font.FontRenderer;

import java.awt.*;
import java.io.File;

public class Category
{
    private final FontRenderer fontRenderer;
    private final GUIExplorer guiExplorer;
    private final String filePath;
    private final float y;

    public Category(GUIExplorer guiExplorer, FontRenderer fontRenderer, String filePath, float y)
    {
        this.fontRenderer = fontRenderer;
        this.guiExplorer = guiExplorer;
        this.filePath = filePath;
        this.y = y;
    }

    public void drawScreen(float mouseX, float mouseY)
    {
        if (this.guiExplorer.getSelectedCategory().equals(this.filePath))
        {
            GUI.drawRect(0, y, 272, y + 50, new Color(0, 128, 255, 128));
        }

        else if (this.isInsideBox(mouseX, mouseY, 0, y, 272, y + 50))
        {
            GUI.drawRect(0, y, 272, y + 50, new Color(0, 128, 255, 50));
        }

        String[] parts = filePath.split("/");
        String fileName = parts[parts.length - 1];
        this.fontRenderer.drawString(this.y == 0 ? "This PC" : fileName, 8, y + 14, new Color(255, 255, 255));
    }

    public void mouseClicked(int mouseButton, float mouseX, float mouseY)
    {
        if (this.isInsideBox(mouseX, mouseY, 0, y, 272, y + 50))
        {
            if (mouseButton == 0)
            {
                this.guiExplorer.setSelectedFolder(new File(this.filePath));
                this.guiExplorer.setSelectedCategory(this.filePath);
                this.guiExplorer.scrollIndex = 0;
                this.guiExplorer.menu = null;
            }
        }
    }

    private boolean isInsideBox(float mouseX, float mouseY, float x, float y, float width, float height)
    {
        return mouseX > x && mouseX < width && mouseY > y && mouseY < height;
    }
}
