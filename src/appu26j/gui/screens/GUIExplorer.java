package appu26j.gui.screens;

import appu26j.gui.GUI;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class GUIExplorer extends GUIScreen
{
	protected final ArrayList<String> selectedFiles = new ArrayList<>();
	private final ArrayList<Category> categories = new ArrayList<>();
	protected float scrollIndex = 0, maxScrollIndex = 0;
	private boolean isCtrlKeyDown = false;
	private String selectedCategory = "";
	private File selectedFolder = null;
	protected Menu menu = null;

	@Override
	public void drawScreen(float mouseX, float mouseY)
	{
		if (this.selectedFolder == null)
		{
			float textWidth = 720.1409F;
			this.fontRendererBig.drawString("Welcome! You may select a folder", ((this.width + 275) / 2) - (textWidth / 2), (this.height / 2) - 18, new Color(255, 255, 255));
		}

		else
		{
			ArrayList<File> temp1 = this.selectedFolder.listFiles() == null ? null : Arrays.stream(Objects.requireNonNull(this.selectedFolder.listFiles())).filter(file -> !file.isHidden() && file.isDirectory()).collect(Collectors.toCollection(ArrayList::new));
			ArrayList<File> temp2 = this.selectedFolder.listFiles() == null ? null : Arrays.stream(Objects.requireNonNull(this.selectedFolder.listFiles())).filter(file -> !file.isHidden() && !file.isDirectory()).collect(Collectors.toCollection(ArrayList::new));
			ArrayList<File> files = null;

			if (temp1 != null)
			{
				files = new ArrayList<>(temp1);
			}

			if (temp2 != null)
			{
				if (files == null)
				{
					files = new ArrayList<>();
				}

				files.addAll(temp2);
			}

			if (files == null)
			{
				float textWidth = 501.52118F;
				this.fontRendererBig.drawString("This folder doesn't exist", ((this.width + 275) / 2) - (textWidth / 2), (this.height / 2) - 18, new Color(255, 255, 255));
			}

			else if (files.isEmpty())
			{
				float textWidth = 415.74155F;
				this.fontRendererBig.drawString("This folder is empty", ((this.width + 275) / 2) - (textWidth / 2), (this.height / 2) - 18, new Color(255, 255, 255));
			}

			else
			{
				float yOffset = 55 - this.scrollIndex;
				float maxTextWidth = 0;

				for (File file : files)
				{
					float temp = this.fontRenderer.getStringWidth(file.getName());

					if (maxTextWidth < temp)
					{
						maxTextWidth = temp;
					}
				}

				maxTextWidth += 292;

				for (File file : files)
				{
					if (this.selectedFiles.contains(file.getAbsolutePath()))
					{
						GUI.drawRect(275, yOffset, maxTextWidth, yOffset + 50, new Color(0, 128, 255, 128));
					}

					else if (this.menu == null && this.isInsideBox(mouseX, mouseY, 275, yOffset, maxTextWidth, yOffset + 50))
					{
						GUI.drawRect(275, yOffset, maxTextWidth, yOffset + 50, new Color(0, 128, 255, 50));
					}

					this.fontRenderer.drawString(file.getName(), 285, yOffset + 14, new Color(255, 255, 255));
					yOffset += 55;
				}

				float temp = 0;

				for (int i = 0; i < files.size() - 1; i++)
				{
					temp += 55;
				}

				if (temp > (this.height - 55))
				{
					this.maxScrollIndex = temp;
				}

				else
				{
					this.maxScrollIndex = 0;
				}
			}
		}

		GUI.drawRect(272, 0, 275, this.height, new Color(56, 56, 61));
		GUI.drawRect(0, 52, this.width, 55, new Color(56, 56, 61));

		for (Category category : this.categories)
		{
			category.drawScreen(mouseX, mouseY);
		}

		GUI.drawRect(275, 0, this.width, 50, new Color(50, 51, 56));
		String[] pathParts = this.selectedFolder == null ? null : this.selectedFolder.getAbsolutePath().split("\\\\");
		float xOffset = 285;

		if (pathParts != null)
		{
			for (String part : pathParts)
			{
				if (part.equals("C:"))
				{
					part = part.replace("C:", "OS (C:)");
				}

				this.fontRenderer.drawString(part, xOffset, 14, new Color(255, 255, 255));

				if (this.menu == null && this.isInsideBox(mouseX, mouseY, xOffset - 4, 8, xOffset + this.fontRenderer.getStringWidth(part) + 4, 45))
				{
					GUI.drawRect(xOffset - 4, 8, xOffset + this.fontRenderer.getStringWidth(part) + 4, 45, new Color(0, 128, 255, 50));
				}

				float temp = xOffset;
				temp += this.fontRenderer.getStringWidth(part);

				if (!part.equals(pathParts[pathParts.length - 1]))
				{
					this.fontRenderer.drawString(" > ", temp, 14, new Color(255, 255, 255));
				}

				xOffset += this.fontRenderer.getStringWidth(part + " > ");
			}
		}

		if (this.menu != null)
		{
			this.menu.drawScreen(mouseX, mouseY);
		}
	}
	
	@Override
	public void mouseClicked(int mouseButton, float mouseX, float mouseY)
	{
		super.mouseClicked(mouseButton, mouseX, mouseY);

		if (this.menu != null)
		{
			this.menu.mouseClicked(mouseButton, mouseX, mouseY);
		}

		else
		{
			float yOffset = 55 - this.scrollIndex;

			if (this.selectedFolder != null)
			{
				ArrayList<File> temp1 = this.selectedFolder.listFiles() == null ? null : Arrays.stream(Objects.requireNonNull(this.selectedFolder.listFiles())).filter(file -> !file.isHidden() && file.isDirectory()).collect(Collectors.toCollection(ArrayList::new));
				ArrayList<File> temp2 = this.selectedFolder.listFiles() == null ? null : Arrays.stream(Objects.requireNonNull(this.selectedFolder.listFiles())).filter(file -> !file.isHidden() && !file.isDirectory()).collect(Collectors.toCollection(ArrayList::new));
				ArrayList<File> files = null;

				if (temp1 != null)
				{
					files = new ArrayList<>(temp1);
				}

				if (temp2 != null)
				{
					if (files == null)
					{
						files = new ArrayList<>();
					}

					files.addAll(temp2);
				}

				if (files != null)
				{
					float maxTextWidth = 0;

					for (File file : files)
					{
						float temp = this.fontRenderer.getStringWidth(file.getName());

						if (maxTextWidth < temp)
						{
							maxTextWidth = temp;
						}
					}

					maxTextWidth += 292;
					boolean temp = false;

					for (File file : files)
					{
						if (this.isInsideBox(mouseX, mouseY, 275, yOffset, maxTextWidth, yOffset + 50))
						{
							if (!temp)
							{
								temp = true;
							}

							if (mouseButton == 0)
							{
								if (this.selectedFiles.contains(file.getAbsolutePath()))
								{
									if (this.isCtrlKeyDown)
									{
										this.selectedFiles.remove(file.getAbsolutePath());
									}

									else
									{
										if (file.isDirectory())
										{
											this.selectedFiles.clear();
											this.scrollIndex = 0;
											this.selectedFolder = file;
										}

										else
										{
											try
											{
												Desktop.getDesktop().open(file);
												this.selectedFiles.remove(file.getAbsolutePath());
											}

											catch (Exception e)
											{
												;
											}
										}
									}
								}

								else
								{
									if (this.isCtrlKeyDown)
									{
										this.selectedFiles.add(file.getAbsolutePath());
									}

									else
									{
										this.selectedFiles.clear();
										this.selectedFiles.add(file.getAbsolutePath());
									}
								}
							}

							else if (mouseButton == 1)
							{
								if (this.isCtrlKeyDown)
								{
									this.selectedFiles.add(file.getAbsolutePath());
								}

								else
								{
									this.selectedFiles.clear();
									this.selectedFiles.add(file.getAbsolutePath());
								}

								this.menu = new Menu(this, mouseX, mouseY, "Open", "Copy", "Delete", "Rename");
							}
						}

						yOffset += 55;
					}

					if (!temp)
					{
						this.selectedFiles.clear();

						if (mouseButton == 1)
						{
							this.menu = new Menu(this, mouseX, mouseY, "New Folder");
						}
					}
				}
			}

			String[] pathParts = this.selectedFolder == null ? null : this.selectedFolder.getAbsolutePath().split("\\\\");
			float xOffset = 285;

			if (pathParts != null)
			{
				for (String part : pathParts)
				{
					if (part.equals("C:"))
					{
						part = part.replace("C:", "OS (C:)");
					}

					if (this.isInsideBox(mouseX, mouseY, xOffset - 4, 8, xOffset + this.fontRenderer.getStringWidth(part) + 4, 45) && mouseButton == 0)
					{
						String realPath = part;

						if (realPath.equals("OS (C:)"))
						{
							realPath = realPath.replace("OS (C:)", "C:\\\\");
						}

						String[] temp = this.selectedFolder.getAbsolutePath().split(realPath);

						if (temp.length != 0)
						{
							this.scrollIndex = 0;
							this.selectedFolder = new File(temp[0] + realPath);
						}
					}

					xOffset += this.fontRenderer.getStringWidth(part + " > ");
				}
			}
		}

		for (Category category : this.categories)
		{
			category.mouseClicked(mouseButton, mouseX, mouseY);
		}
	}

	@Override
	public void initGUI(int width, int height)
	{
		super.initGUI(width, height);

		if (this.categories.isEmpty())
		{
			String pcName = System.getProperty("user.name");

			String[] categoryNames = new String[]
			{
					"C:/Users/" + pcName,
					"C:/Users/" + pcName + "/OneDrive/Desktop",
					"C:/Users/" + pcName + "/Downloads",
					"C:/Users/" + pcName + "/OneDrive/Documents",
					"C:/Users/" + pcName + "/OneDrive/Pictures",
					"C:/Users/" + pcName + "/Music",
					"C:/Users/" + pcName + "/Videos",
			};

			float yOffset = 0;

			for (String categoryName : categoryNames)
			{
				this.categories.add(new Category(this, this.fontRenderer, categoryName, yOffset));
				yOffset += 55;
			}
		}
	}

	@Override
	public void keyPressed(int keyCode, float mouseX, float mouseY)
	{
		super.keyPressed(keyCode, mouseX, mouseY);

		if (keyCode == 341)
		{
			this.isCtrlKeyDown = true;
		}
	}

	@Override
	public void keyReleased(int keyCode, float mouseX, float mouseY)
	{
		super.keyReleased(keyCode, mouseX, mouseY);

		if (keyCode == 341)
		{
			this.isCtrlKeyDown = false;
		}
	}

	@Override
	public void onScroll(int direction)
	{
		super.onScroll(direction);
		this.scrollIndex -= direction * 55;

		if (this.scrollIndex < 0)
		{
			this.scrollIndex = 0;
		}

		if (this.scrollIndex > this.maxScrollIndex)
		{
			this.scrollIndex = this.maxScrollIndex;
		}
	}

	public void setSelectedFolder(File selectedFolder)
	{
		this.selectedFolder = selectedFolder;
	}

	public String getSelectedCategory()
	{
		return this.selectedCategory;
	}

	public void setSelectedCategory(String selectedCategory)
	{
		this.selectedCategory = selectedCategory;
	}
}