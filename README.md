# CYC: Your Cycling

## Creating a runnable JAR
This project is scaffolded by Maven and uses its directory stucture.
The file `POM.xml` contains the build instructions for Maven.

To use this, Maven must be installed on the machine.
This can be checked by running the command `mvn -version` on Linux.
If it is not installed, it should be installed (see the section on this below).

A tested, runnable jar can be built by navigating into the directory `Seng202Group1`
and using the command `mvn clean package`.
This will create the directory `target` with the JAR in it (and remove any previous 
Maven builds for this project).
The project can then by run with the command 
`java -jar target/SENG202Group1-1.0-SNAPSHOT.jar`.

The program will create a directory structure (headed by `src`) to hold the project files
in the same directory as the `.jar` file.

## Installing Maven
On Linux: run the command `sudo apt-get install maven`

On OSX: using Homebrew, run the command `brew install maven`

On Windows: download Maven from http://maven.apache.org/
and unzip it to the desired destination (preferably ``C:\usr\bin``).
Using Windows System Properties, configure the path for maven to point to the 
unzipped folder.

## Importing the project into Intellij

1. On the IntelliJ landing screen, select the option Import Project.
2. Select the directory of this project.
3. Select Maven and click import.

The project should now be open. 
