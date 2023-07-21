package appu26j.gui.screens;

import appu26j.gui.GUI;
import appu26j.utils.FileUtil;
import com.sun.jna.platform.FileUtils;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class Menu
{
    private final GUIExplorer guiExplorer;
    private final String[] buttons;
    private final float x, y;

    public Menu(GUIExplorer guiExplorer, float x, float y, String... buttons)
    {
        this.guiExplorer = guiExplorer;
        this.x = x;
        this.y = y;
        this.buttons = buttons;
    }

    public void drawScreen(float mouseX, float mouseY)
    {
        float yOffset = this.y;

        for (String button : this.buttons)
        {
            if (this.isInsideBox(mouseX, mouseY, this.x, yOffset, this.x + 300, yOffset + 40))
            {
                GUI.drawRect(this.x, yOffset, this.x + 300, yOffset + 45, new Color(78, 81, 86));
            }

            else
            {
                GUI.drawRect(this.x, yOffset, this.x + 300, yOffset + 45, new Color(58, 61, 66));
            }

            this.guiExplorer.fontRenderer.drawString(button, this.x + 5, yOffset + 10, new Color(255, 255, 255));
            yOffset += 45;
        }
    }

    public void mouseClicked(int mouseButton, float mouseX, float mouseY)
    {
        float yOffset = this.y;

        for (String button : this.buttons)
        {
            if (this.isInsideBox(mouseX, mouseY, this.x, yOffset, this.x + 300, yOffset + 40) && mouseButton == 0)
            {
                switch (button)
                {
                    case "Open":
                    {
                        try
                        {
                            for (String file : this.guiExplorer.selectedFiles)
                            {
                                File fileClass = new File(file);

                                if (fileClass.isDirectory())
                                {
                                    this.guiExplorer.scrollIndex = 0;
                                    this.guiExplorer.setSelectedFolder(fileClass);
                                }

                                else
                                {
                                    Desktop.getDesktop().open(fileClass);
                                }
                            }

                            this.guiExplorer.selectedFiles.clear();
                        }

                        catch (Exception e)
                        {
                            ;
                        }

                        break;
                    }

                    case "Copy":
                    {
                        FileUtil.FileTransferable fileTransferable = new FileUtil.FileTransferable(this.guiExplorer.selectedFiles, true);
                        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(fileTransferable, (clipboard, contents) -> {});
                        break;
                    }

                    case "Delete":
                    {
                        String[] temp = this.guiExplorer.selectedFiles.toArray(new String[0]);
                        ArrayList<File> tempFiles = new ArrayList<>();

                        for (String file : temp)
                        {
                            tempFiles.add(new File(file));
                        }

                        this.guiExplorer.selectedFiles.clear();

                        try
                        {
                            FileUtils.getInstance().moveToTrash(tempFiles.toArray(new File[0]));
                        }

                        catch (Exception e)
                        {
                            ;
                        }

                        break;
                    }
                }

                this.guiExplorer.menu = null;
            }

            yOffset += 45;
        }

        if (!this.isInsideBox(mouseX, mouseY, this.x, this.y, this.x + 300, yOffset))
        {
            this.guiExplorer.menu = null;
        }
    }

    private boolean isInsideBox(float mouseX, float mouseY, float x, float y, float width, float height)
    {
        return mouseX > x && mouseX < width && mouseY > y && mouseY < height;
    }
}
