# Rebrickable Data Downloader
Here is an automatic file downloader and unzipper for the Rebrickable data set (hosted at https://rebrickable.com/downloads/) implemented in Java.
## Editor Compliance
This project was developed with Eclipse IDE for Java Developers Version 2023-12. It should be backwards and forwards compatible with all Eclipse Java IDEs.

[For Apache NetBeans users](https://chanmingman.wordpress.com/2015/03/22/import-eclipse-project-into-netbeans/)

[Official documentation for importing into IntelliJ Idea](https://www.jetbrains.com/help/idea/import-project-from-eclipse-page-1.html)

## JDK Compliance
This project was created with Java Compiler compliance level 1.8.0 in mind. Please set your compiler compliance levels and relevant System Environment variables when running to reflect this decision.

## Usage
``java -jar RebrickableGetter.jar [OPTIONS]``

### Options:
#### ```-d x.csv -d y.csv```

``-d`` can be used to direct the program to only download certain specified CSV files to ``cache/``.

#### ```-all```

``-all`` can be used to direct the program to download all available CSV files to ``cache/``.

#### ```-conf someconfig.conf```

``-conf`` can be used to direct the program to download all CSV files listed in a specified config file, one CSV file per line.

## Things to note:
Rebrickable Data Downloader will check the last modification date of any matching CSV file in ``cache/`` to determine file newness and whether to update a file. Careful when opening any of these files in Excel or your preferred text editor to not save any changes made if you want the rate of update to be consistent with new changes on the Rebrickable CDN! 
