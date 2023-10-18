# Artificial Intelligence Implementation on Adjacency Strategy Game

## Table of Contents
* [Technologies Used](#technologies-used)
* [Features](#features)
* [Setup](#setup)
* [Usage](#usage)
* [Project Status](#project-status)
* [Room for Improvement](#room-for-improvement)
* [Acknowledgements](#acknowledgements)


## Technologies Used
- Java   - version 20.0
- JavaFX - version 21.0


## Features
Artificial algorithms implemented:
- Sideways Local Search Algorithm
- Minimax Algorithm
- Genetic Algorithm


## Setup

### Installation
To get started, please install the latest version of the Java Development Kit (JDK) and please install a Java IDE such as IntelliJ. Please note that the deployment instructions below use IntelliJ as the IDE.

### Deployment (IntelliJ)
1. Clone the repository through Git by running the following command:
git clone https://github.com/GAIB20/adversarial-adjacency-strategy-game.git, or simply download the repository.
2. Open the repository folder through IntelliJ.
3. Set up the JDK by going to File -> Project Structure -> Project tab. In the Project tab, go to Project SDK, click New, and browse to the location of your JDK folder.
4. NOTE: JavaFX has been removed starting from JDK 11, and it is now a standalone module. The JavaFX files needed to set up the Adjacency program are located in the repository folder itself. To set it up, go to File -> Project Structure -> Libraries tab. In the Libraries tab, press the + button, browse to the "javafx-sdk/lib" folder in the repository, and add it to the list of libraries.
5. IMPORTANT: Go to Run -> Edit Configurations, and go to the VM options. In this line, please add in the full path to the lib folder of the javafx-sdk folder on your own computer, and then add the following line
--add-modules=javafx.controls,javafx.fxml.

For example, I added the following line to my VM options: --module-path "C:\Jed's Work\CS Side Projects\Adjacency-Strategy-Game\javafx-sdk\lib" --add-modules=javafx.controls,javafx.fxml
6. Open the Main class in the IntelliJ file interface.
<hr>


## Project Status
Project is: _complete_, and there are no further improvement planned.


## Room for Improvement
Room for improvement:
- Multithreading for faster execution
- Additional settings to benchmark bot vs bot on GUI


## Acknowledgements
- This project was spearheaded by the Institut Teknologi Bandung's Informatics major of Computer Graphics and Artificial Intelligence laboratory, which has been well organized by the IF3170-2023 professors and assistants.
- README template by [@flynerdpl](https://www.flynerd.pl/)
