package fetch_files5;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author allan
 */
public class Fetch_files5 {

    // Base URL for the files to download
    private static  String baseUrl = "https://cdn.rebrickable.com/media/downloads/";
    
    // List of all possible files to download
    private static String[] allFiles = {
        "themes.csv.gz", "colors.csv.gz", "part_categories.csv.gz",
        "parts.csv.gz", "part_relationships.csv.gz", "elements.csv.gz",
        "sets.csv.gz", "minifigs.csv.gz", "inventories.csv.gz",
        "inventory_parts.csv.gz", "inventory_sets.csv.gz", "inventory_minifigs.csv.gz"
    };

    private static void downloadFile(String fileName, String downloadDirectory) {
        try {
            URL url = new URL(baseUrl + fileName);
            InputStream inputStream = new BufferedInputStream(url.openStream());
            Path outputPath = Paths.get(downloadDirectory, fileName);
            if (Files.exists(outputPath)) {
                System.err.println("File already exists: " + outputPath);
                return;
            }

            FileOutputStream fileOutputStream = new FileOutputStream(outputPath.toFile());
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
            fileOutputStream.close();
            inputStream.close();
            System.out.println("Downloaded: " + fileName);

            // Unzip the file after downloading
            unzipGzFile(outputPath);

        } catch (IOException e) {
            System.err.println("Failed to download file: " + fileName);
            e.printStackTrace();
        }
    }

    private static void unzipGzFile(Path gzFilePath) {
        String outputFileName = gzFilePath.toString().replace(".gz", "");
        Path outputPath = Paths.get(outputFileName);

        try (GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(gzFilePath.toFile()));
             FileOutputStream fileOutputStream = new FileOutputStream(outputPath.toFile())) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = gzipInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }

            System.out.println("Unzipped: " + outputFileName);
            gzFilePath.toFile().delete();

        } catch (IOException e) {
            System.err.println("Failed to unzip file: " + gzFilePath);
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        List<String> filesToDownload = new ArrayList<>();
        String downloadDirectory = ".";
        boolean downloadAll = false;
        
        // Parse command line arguments
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-f":
                    // Add specific files to download list
                    if (i + 1 < args.length) {
                        filesToDownload.add(args[++i]);
                    } else {
                        System.err.println("Missing filename after -f");
                        return;
                    }
                    break;
                case "-d":
                    // Set the download directory
                    if (i + 1 < args.length) {
                        downloadDirectory = args[++i];
                    } else {
                        System.err.println("Missing directory after -d");
                        return;
                    }
                    break;
                case "--all":
                    // Flag to download all files
                    downloadAll = true;
                    break;
                default:
                    System.err.println("Unknown option: " + args[i]);
                    return;
            }
        }

        if (downloadAll) {
            for (String file : allFiles) {
                filesToDownload.add(file);
            }
        }

        if (filesToDownload.isEmpty()) {
            System.err.println("No files specified for download.");
            return;
        }

        Path downloadPath = Paths.get(downloadDirectory);
        if (Files.notExists(downloadPath)) {
            try {
                Files.createDirectories(downloadPath);
            } catch (IOException e) {
                System.err.println("Failed to create directory: " + downloadDirectory);
                e.printStackTrace();
                return;
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timestamp = sdf.format(new Date());
        Path timestampedDir = downloadPath.resolve(timestamp + "-rebrickable-data");
        try {
            Files.createDirectory(timestampedDir);
        } catch (IOException e) {
            System.err.println("Failed to create timestamped directory: " + timestampedDir);
            e.printStackTrace();
            return;
        }

        for (String file : filesToDownload) {
            downloadFile(file, timestampedDir.toString());
        }
    }
}