package appu26j;

import appu26j.assets.Assets;
import appu26j.gui.screens.GUIExplorer;
import appu26j.gui.screens.GUIScreen;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.awt.*;
import java.nio.DoubleBuffer;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public enum FileExplorer
{
	INSTANCE;

	private int width = (int) (getWidth() / 1.25), height = (int) (getHeight() / 1.25);
	private GUIScreen currentScreen = new GUIExplorer();
	private float mouseX = 0, mouseY = 0;
	private boolean windowHidden = false;
	private long window = 0;
	
	public void start()
	{
		this.initializeWindow();
		this.setupOpenGL();
		this.loop();
	}
	
	private void initializeWindow()
	{
		Assets.loadAssets();
		GLFWErrorCallback.createPrint(System.err).set();
		
		if (!glfwInit())
		{
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		glfwWindowHint(GLFW_FOCUS_ON_SHOW, GLFW_TRUE);
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		this.window = glfwCreateWindow(this.width, this.height, "File Explorer", 0, 0);
		glfwSetWindowPos(this.window, 25, 75);
		
		if (this.window == 0)
		{
			throw new IllegalStateException("Unable to create the GLFW window");
		}
		
		glfwMakeContextCurrent(this.window);
		glfwSwapInterval(1);
		
		glfwSetMouseButtonCallback(this.window, new GLFWMouseButtonCallback()
		{
		    public void invoke(long window, int button, int action, int mods)
		    {
		    	if (!FileExplorer.this.windowHidden)
				{
					if (action == 1)
					{
						FileExplorer.this.currentScreen.mouseClicked(button, FileExplorer.this.mouseX, FileExplorer.this.mouseY);
					}

					else
					{
						FileExplorer.this.currentScreen.mouseReleased(button, FileExplorer.this.mouseX, FileExplorer.this.mouseY);
					}
				}
		    }
		});

		glfwSetWindowSizeCallback(this.window, new GLFWWindowSizeCallback()
		{
			@Override
			public void invoke(long window, int width, int height)
			{
				FileExplorer.this.width = width;
				FileExplorer.this.height = height;
				FileExplorer.this.currentScreen.initGUI(width, height);
				setupOpenGL();
			}
		});

		glfwSetKeyCallback(this.window, new GLFWKeyCallback()
		{
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods)
			{
				if (action == 1)
				{
					FileExplorer.this.currentScreen.keyPressed(key, FileExplorer.this.mouseX, FileExplorer.this.mouseY);
				}

				else if (action != 2)
				{
					FileExplorer.this.currentScreen.keyReleased(key, FileExplorer.this.mouseX, FileExplorer.this.mouseY);
				}
			}
		});

		glfwSetScrollCallback(this.window, new GLFWScrollCallback()
		{
			@Override
			public void invoke(long window, double deltaX, double deltaY)
			{
				FileExplorer.this.currentScreen.onScroll((int) deltaY);
			}
		});

		ImageParser imageParser1 = ImageParser.loadImage(Assets.getAsset("explorer_icon_16x16.png"));
		ImageParser imageParser2 = ImageParser.loadImage(Assets.getAsset("explorer_icon_32x32.png"));

		try (GLFWImage glfwImage1 = GLFWImage.malloc(); GLFWImage glfwImage2 = GLFWImage.malloc(); GLFWImage.Buffer imageBuffer = GLFWImage.malloc(2))
		{
			glfwImage1.set(imageParser1.getWidth(), imageParser1.getHeight(), imageParser1.getImage());
			glfwImage2.set(imageParser2.getWidth(), imageParser2.getHeight(), imageParser2.getImage());
			imageBuffer.put(0, glfwImage1);
			imageBuffer.put(1, glfwImage2);
			glfwSetWindowIcon(this.window, imageBuffer);
		}

		GL.createCapabilities();
		glClearColor(0.199F, 0.2F, 0.22F, 1);
	}
	
	private void setupOpenGL()
	{
		glLoadIdentity();
		glViewport(0, 0, this.width, this.height);
		glOrtho(0, this.width, this.height, 0, 1, 0);
	}
	
	private void loop()
	{
		this.currentScreen.initGUI(this.width, this.height);
		glfwShowWindow(this.window);
		
		while (!glfwWindowShouldClose(this.window))
		{
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
			if (!this.windowHidden)
			{
				try (MemoryStack memoryStack = MemoryStack.stackPush())
				{
					DoubleBuffer mouseX = memoryStack.mallocDouble(1);
					DoubleBuffer mouseY = memoryStack.mallocDouble(1);
					glfwGetCursorPos(this.window, mouseX, mouseY);
					this.mouseX = (float) mouseX.get();
					this.mouseY = (float) mouseY.get();
				}

				this.currentScreen.drawScreen(this.mouseX, this.mouseY);
			}

			glfwSwapBuffers(this.window);
			glfwSwapInterval(1);
			glfwPollEvents();
		}
		
		this.currentScreen.getFontRenderer().shutdown();
		glfwDestroyWindow(this.window);
		glfwTerminate();
		Objects.requireNonNull(glfwSetErrorCallback(null)).free();
	}
	
	public void displayGUIScreen(GUIScreen guiScreen)
	{
		if (guiScreen != null)
		{
			guiScreen.initGUI(this.width, this.height);
		}
		
		this.currentScreen = guiScreen;
	}
	
	public GUIScreen getCurrentScreen()
	{
		return this.currentScreen;
	}
	
	public long getWindowID()
	{
		return this.window;
	}

	public boolean isWindowHidden()
	{
		return this.windowHidden;
	}

	public void setWindowHidden(boolean windowHidden)
	{
		this.windowHidden = windowHidden;
	}

	private int getWidth()
	{
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth();
	}

	private int getHeight()
	{
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight();
	}
}
