# Academic Search Engine

This project is part of the Information Retrieval course at Université François Rabelais - Tours. The expected outcome of the project is a search engine specialized in the domain of scientific research publications. This search engine should allow searching for relevant textual documents using keywords and natural languages utterances, and should provide ranked results.

#### Instructions to download and run the project
1. Clone the repository to your machine by either using [GitHub's Client for Windows] or by typing the following in the command line (make sure you working directory is the folder where you want the project to be in):

   ` git clone https://github.com/larissaleite/AcademicSearchEngine.git `

2. Import the project on Eclipse as follows: 

   ` File > Import > Maven > Existing Maven Project `
  
3. If there are errors, right-click on the project `Run as > Maven Install`
4. Select `JDK 5 / JRE 1.5`
5. Add your MySQL (make sure you have it installed and running, and **create a schema called searchengine**) username and password on the file `AcademicSearchEngine/WebContent/WEB-INF/applicationContext.xml`
6. If you don't have Tomcat set up on Eclipse, download [Tomcat 7] and add it as a Server (to open the Server window go to `Window > Show View > Other > Server > Servers`) by right-clicking on the Server window
7. Run the project by right-clicking on it `Run as > Run on Server`
8. Enter `http://localhost:8080/AcademicSearchEngine` on the web browser

[GitHub's Client for Windows]: https://desktop.github.com/
[Tomcat 7]: https://tomcat.apache.org/download-70.cgi