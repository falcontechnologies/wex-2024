/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package wex.issue.pkg7;


/**
 *
 * @author shara
 */
import java.io.BufferedInputStream;

import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.Scanner;
import java.util.zip.GZIPOutputStream;



public class WexIssue7 {

    public static void main(String[] args) {
       
       
        Scanner url = new Scanner(System.in);
        Scanner num = new Scanner(System.in);
       
         String file_elements = "https://cdn.rebrickable.com/media/downloads/elements.csv.gz";
               
         String file_part_categories= "https://cdn.rebrickable.com/media/downloads/part_categories.csv.gz";
               
         String file_sets = "https://cdn.rebrickable.com/media/downloads/sets.csv.gz";
               
        String file_themes = "https://cdn.rebrickable.com/media/downloads/themes.csv.gz";
               
        String file_colors = "https://cdn.rebrickable.com/media/downloads/colors.csv.gz";
               
        String file_parts = "https://cdn.rebrickable.com/media/downloads/parts.csv.gz";
               
        String file_part_relationships = "https://cdn.rebrickable.com/media/downloads/part_relationships.csv.gz";
               
         String file_minifigs = "https://cdn.rebrickable.com/media/downloads/minifigs.csv.gz";
               
        String file_inventory_parts = "https://cdn.rebrickable.com/media/downloads/inventory_parts.csv.gz";
               
        String file_inventory_sets = "https://cdn.rebrickable.com/media/downloads/inventory_sets.csv.gz";
               
        String file_inventory_minifigs = "https://cdn.rebrickable.com/media/downloads/inventory_minifigs.csv.gz";      
               
        String file_inventories = "https://cdn.rebrickable.com/media/downloads/inventories.csv.gz";
               
         
               
        String file_name = "";
         String file = "";
        System.out.println("number of files you wish to receive");
        int nums = num.nextInt();
        String[] files = {file_elements,
            file_part_categories,
            file_sets,
            file_themes,
            file_colors,
            file_parts,
            file_inventories,
            file_part_relationships,
            file_minifigs,
            file_inventory_parts,
            file_inventory_sets,
            file_inventory_minifigs};
       

       
           
            if (nums != 12) {
                 for (int i = 0; i <nums; i++ ){
                     
                    String urls = url.nextLine();

                    for (int j = 0; j <12; j++ ){
                   
                        file_name = "https://cdn.rebrickable.com/media/downloads/"+urls+".csv.gz";

                        if (file_name.equals(files[j])) {

                            try {
                        downloadUsingNIO(file_name, urls+".csv");

                        downloadUsingStream(file_name, "New_" + urls+".csv");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                            String gzipFile = urls +".csv";
                            String newFile = "new_"+urls+".csv";

                            decompressGzipFile(gzipFile, newFile);
                        }
                    }
                     }

            }else{
                for (int j = 0; j <12; j++ ) {
                    file = files[j];
                    file_name = file.replace("https://cdn.rebrickable.com/media/downloads/", "")  ;
                    file_name =  file_name.replace("gz", "")  ;

                            try {
                        downloadUsingNIO(file, file_name);

                        downloadUsingStream(file, "new_"+file_name);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                            String gzipFile =  file_name;
                            String newFile = "new_"+file_name;

                            decompressGzipFile(gzipFile, newFile);
                       
                    }
               
                    }

                   
                       
           
        }


    private static void downloadUsingStream(String urlStr, String file) throws IOException{
        URL url = new URL(urlStr);
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        FileOutputStream fis = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int count=0;
        while((count = bis.read(buffer,0,1024)) != -1)
        {
            fis.write(buffer, 0, count);
        }
        fis.close();
        bis.close();
    }

    private static void downloadUsingNIO(String urlStr, String file) throws IOException {
        URL url = new URL(urlStr);
        try (ReadableByteChannel rbc = Channels.newChannel(url.openStream())) {
            FileOutputStream fos = new FileOutputStream(file);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
        }
    }
    private static void decompressGzipFile(String gzipFile, String newFile) {
        try {
            FileInputStream fis = new FileInputStream(gzipFile);
            GZIPInputStream gis = new GZIPInputStream(fis);
            FileOutputStream fos = new FileOutputStream(newFile);
            byte[] buffer = new byte[1024];
            int len;
            while((len = gis.read(buffer)) != -1){
                fos.write(buffer, 0, len);
            }
            //close resources
            fos.close();
            gis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
       
    }

    private static void compressGzipFile(String file, String gzipFile) {
        try {
            FileInputStream fis = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(gzipFile);
            GZIPOutputStream gzipOS = new GZIPOutputStream(fos);
            byte[] buffer = new byte[1024];
            int len;
            while((len=fis.read(buffer)) != -1){
                gzipOS.write(buffer, 0, len);
            }
            //close resources
            gzipOS.close();
            fos.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
       
    }
}


