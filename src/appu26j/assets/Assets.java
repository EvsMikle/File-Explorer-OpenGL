package appu26j.assets;

import java.io.*;
import java.util.ArrayList;

public class Assets
{
	private static final String tempDirectory = System.getProperty("java.io.tmpdir");
	private static final ArrayList<String> assets = new ArrayList<>();
	
	static
	{
		assets.add("explorer_icon_16x16.png");
		assets.add("explorer_icon_32x32.png");
		assets.add("segoeui.ttf");
	}
	
	public static void loadAssets()
	{
		File assetsDirectory = new File(tempDirectory, "assets");
		
		if (!assetsDirectory.exists())
		{
			assetsDirectory.mkdirs();
		}
		
		for (String name : assets)
		{
			File asset = new File(assetsDirectory, name);
			
			if (!asset.exists())
			{
				FileOutputStream fileOutputStream = null;
				InputStream inputStream = null;
				
				try
				{
					inputStream = Assets.class.getResourceAsStream(name);
					
					if (inputStream == null)
					{
						throw new NullPointerException();
					}
					
					fileOutputStream = new FileOutputStream(asset);
					byte[] bytes = new byte[4096];
				    int read;
				    
				    while ((read = inputStream.read(bytes)) != -1)
				    {
				    	fileOutputStream.write(bytes, 0, read);
				    }
				}
				
				catch (Exception e)
				{
					;
				}
				
				finally
				{
					if (fileOutputStream != null)
					{
						try
						{
							fileOutputStream.close();
						}
						
						catch (Exception e)
						{
							;
						}
					}
					
					if (inputStream != null)
					{
						try
						{
							inputStream.close();
						}
						
						catch (Exception e)
						{
							;
						}
					}
				}
			}
		}
	}
	
	public static File getAsset(String name)
	{
		return new File(tempDirectory, "assets" + File.separator + name);
	}
}
