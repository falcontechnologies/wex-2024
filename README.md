# FetchFiles Program
## Overview
The FetchFiles program is designed to download and unzip files from the Rebrickable CDN. It allows users to specify which files to download, where to save them, and can handle downloading all files in one go. The program is written in Java and was developed using NetBeans IDE.
# Configuration and Assumptions
* Java Development Kit (JDK) version 8 or higher is required.
* The program assumes that the target download directory exists or can be created.
* Internet connectivity is required to download files from the Rebrickable CDN.
# Language and Version
* Language: Java
* Version: 8 or higher
# Development Environment Setup 
To set up your local development environment, follow these steps:
1. Install JDK: Ensure that JDK 8 or higher is installed on your machine.
2. Install NetBeans IDE: Download and install NetBeans IDE from NetBeans Official Site.
3. Clone Repository: Clone the GitHub repository containing the FetchFiles program
4. Open Project: Open the project in NetBeans IDE by selecting File -> Open Project and navigating to the cloned repository.


## How to Run the Application
## Running from NetBeans IDE
1. Open Project: Open the project in NetBeans IDE.
2. Build Project: Right-click on the project in the Project Explorer and select Clean and Build.
3. Run Project: Right-click on the project in the Project Explorer and select Run.
## Running from the Command Line
1. Navigate to Project Directory: Open a terminal and navigate to the root directory of the project.
2. Compile the Program: Compile the Java source files.
3. Run the Program: Execute the program with the desired options. Options:
    * -f <filename>: Specify a file to download. Can be used multiple times to download multiple files.
    * -d <directory>: Specify the directory to save downloaded files.
    * --all: Download all available files.  
## Dependencies
* No external dependencies other than JDK are required.
## Design Notes
### * Timestamped Directory: Each download session creates a timestamped directory to prevent file overwrites.
### * Error Handling: The program includes error checking for missing arguments and unknown options. It also checks if files already exist before downloading.
### * File Unzipping: After downloading .gz files, the program automatically unzips them.



