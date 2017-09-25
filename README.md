
#                             CYC: Your Cycling                              #


# Creating a runnable JAR
This project is scaffolded by Maven and uses this directory stucture.
POM.xml Contains the build process for Maven

Maven must be installed on the machine
this can be checked by the command 

`mvn -version`

* On linux the command 

`sudo apt-get install maven`

* On OSX, using Homebrew

`brew install maven`

* On Windows 

Download Maven from http://maven.apache.org/
and unzip it to the desired destination. Preferably \usr\bin
Using windows system properties configure the path for maven to point to the 
unzipped folder

Those steps will install maven

A tested, runnable jar can be built by navigating into the dir: Seng202Group1
using the command: 

`mvn clean package  `

This will create the dir target with the Jar in it and remove any previous 
Maven builds for this Project
Changing into the target directory, the project can then by run with the 
command:

`java -jar SENG202Group1-1.0-SNAPSHOT.jar`

Then a dir to hold the project files in the same dir as the .jar file



# Importing the project into an IDE
The use of IntelliJ is recommended.

To Import The Source Code:

clone the git repository, into your current dir, using the git account 
assigned to the repository either: javanut13 or jpn23
from eng-git with the following command:

`git clone https://eng-git.canterbury.ac.nz/jbu71/Seng202group1.git`

Close any open projects
On the IntelliJ landing screen select the option:
Import Project
Then select the dir it is saved in 
Choose Maven (this should be done by default)
and Click import
the project should now be open. 