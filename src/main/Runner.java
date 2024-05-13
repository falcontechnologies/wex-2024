package main;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.zip.GZIPInputStream;

public class Runner {
	
	//Copy into filesToDownload in case of --all arg
	static String[] allFiles = {
		    "themes.csv.gz",
		    "colors.csv.gz",
		    "part_categories.csv.gz",
		    "parts.csv.gz",
		    "part_relationships.csv.gz",
		    "elements.csv.gz",
		    "sets.csv.gz",
		    "minifigs.csv.gz",
		    "inventories.csv.gz",
		    "inventory_parts.csv.gz",
		    "inventory_sets.csv.gz",
		    "inventory_minifigs.csv.gz"
		};
	
	public static void main(String[] args) {
		
		ArrayList<String> filesToDownload;
		
		//First, parse the launch arguments
		if (args.length != 0 ) {
			filesToDownload = ParseLaunchArgs(args);
		}else {
			filesToDownload = new ArrayList<String>();
			Collections.addAll(filesToDownload, allFiles);
		}
		 
		//Second, ensure the place we're downloading to exists
		File directoire = new File("cache/");
		if (!directoire.exists())
		{
			System.out.println("Creating new cache directory...");
		    directoire.mkdirs();
		}
		
		//Third, start downloading
		System.out.println("Now downloading files...");
		int filesDownloaded = 0;
		for (String file : filesToDownload)
		{
				//First things first, verify whether we already have the file and their dates modified
				String fileUrl = "https://cdn.rebrickable.com/media/downloads/" + file;
				String cachedDirectory = "cache/"+ file.substring(0,file.length()-3); //cut off the .gz at the end
				long NetDateModified = 0;
				try {
					 NetDateModified = new URL(fileUrl).openConnection().getLastModified();
				}catch(Exception e) {
					System.out.println("Couldn't find " + file + " in Rebrickable's CDN. Skipping...");
					continue;
				}
				
				//Second, check the net modification date against the local modification date and download based on that
				File cachedFile = new File(cachedDirectory);
				if(cachedFile.exists())
				{
					if(cachedFile.lastModified() < NetDateModified)
					{
						System.out.println("Updating " + file + "...");
						filesDownloaded += (DownloadFile(fileUrl,"cache/"+ file) ? 1 : 0); 
						UnzipFile(cachedDirectory);
					}else {
						System.out.println("File " + cachedDirectory + " already up to date. Skipping...");
					}
					
				}else {
					System.out.println("Downloading " + file + "...");
					filesDownloaded += (DownloadFile(fileUrl,"cache/"+ file) ? 1 : 0); 
					UnzipFile(cachedDirectory);
				}
			
				
		}
		
		//Lastly, print a report of what files were downloaded and what files were skipped.
		System.out.println("Complete: " + Integer.toString(filesDownloaded) + " files downloaded, " 
		+ Integer.toString(filesToDownload.size() - filesDownloaded) + " files skipped.");
		
		
	}
	
	static boolean DownloadFile(String url, String outpath)
	{
		try {
			URL site = new URL(url);
			Files.copy(site.openStream(), Paths.get(outpath), StandardCopyOption.REPLACE_EXISTING);
			return true;
			
		}catch(Exception e)
		{
			System.out.println("Failed to download file @ \"" + url +   "\". Skipping...");
			return false;
		}
	}
	
	static void UnzipFile(String filepath)
	{
		try {
			FileInputStream fis = new FileInputStream(filepath + ".gz");
			GZIPInputStream gis = new GZIPInputStream(fis);
			FileOutputStream fos = new FileOutputStream(filepath);
			
			byte[] buffer = new byte[1024];
			 int len;
	            while((len = gis.read(buffer)) != -1){
	                fos.write(buffer, 0, len);
	            }
	            //close resources
	            fos.close();
	            gis.close();
	         new File(filepath + ".gz").delete();
			
		}catch (Exception e)
		{
			System.out.println("Failed to unzip " + filepath + ". Skipping...");
		}
	}

	static ArrayList<String> ParseLaunchArgs(String[] args) //this looks like prime control spaghetti inside my editor. Isn't there a better way to do this?
	{
		ArrayList<String> getter = new ArrayList<String>();
		boolean cont = true;
		for (int i = 0; i < args.length; i++)
		{
			cont = !cont;
			if (cont) {
				continue;
			}else {
				if (args[i].equals("-d")) {
					
					try {
						getter.add(args[i+1] + ".gz");
					}catch(Exception e) {
						System.out.println("FATAL: Bad launch arguments. Program will now exit.");
						System.exit(1);
					}
						
				}else if (args[i].equals("--all")) {
					Collections.addAll(getter, allFiles);
				}else if (args[i].equals("--conf"))
				{
					try {
						BufferedReader reader = new BufferedReader(new FileReader(args[i+1]));
						String line = reader.readLine();
						while (line != null)
						{
							getter.add(line + ".gz");
							line = reader.readLine();
						}
						reader.close();
					}catch(Exception e) {
						System.out.println("FATAL: Could not find config file. Program will now exit.");
					}
					
				} else {
					System.out.println("FATAL: Bad launch arguments. Program will now exit.");
					System.exit(1);
				}
				
			}
		}
		
		return getter;
	}
	
}
