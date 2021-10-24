<h1>Mocking Research Scripts</h1>
This code and data is used for Prof. Lu Xiao's research project analyzing the mocking frameworks used in Apache's Java software projects. 
<br></br>
<h2> NOTE </h2>
The scripts use a tool called [Scitools Understand](https://www.scitools.com/), and require Understand to be added to a computer's PATH variables before it will function.
After installing Understand, add 'und.exe' to the computer's PATH variables under environment variables. This allows the CMD commands for Understand to be run in the scripts.
<br></br>
<h2> How to use:</h2>
Place all the scripts from the repository inside one directory. Clone the Git repositories from all the software projects you are testing into that directory. In order to run the script and gather data, run 'AnalysisScript.bat' in the command line.
<br></br>
All project data will be sent into a new directory, 'UndProjects'. Each project's individual metrics generated from Understand as well as Git logs will be present. The final data that is used and recorded is in 'AllMetrics.csv', 'AllMockImports.csv', and 'AllMockFrameworks.csv'.
<br></br>
You need to download an Eclipse SDK (not IDE that we usually use), inside, go to Help -> Install New Software, and in "work with", select m2e release repository. Install all of them if possible, it will help you get rid of the compilation errors when importing maven projects, which most of the projects we are analyzing are. 
<br></br>
After finish installation of eclipse SDK, go to "Keye scripts" folder, import "astSimple" in your eclipse SDK. In project explorer, right-click "astSimple", select Run as -> Eclipse applicatoin, you will see a new Eclipse pop out, called runtime-EclipseApplication. 
<br></br>
In the runtime application, import the projects you are analyzing only one per time. For each, click sample menu -> sample command on the very top, it will help you generate mocking API related data for the specific project you imported.
<br></br> 
Remember, you might need to look at the code carefully, especially the GetInfo.java file, the ouput directory is an absolute directory coded specifically for my PC, feel free to adjust on your own. 
<br></br>
If you finished the process for all projects, you are close to the end. In your normal Eclipse IDE, create a project and import "API_Analyzer.java", "API_summurize.java", "MockFrameworkFrequencyCalculator.java" and "MockedClassesAnalysis.java" from "Keye scripts" folder. Make sure you run "API_Analyzer.java" first and then "API_summurize.java". You can run the "MockFrameworkFrequencyCalculator.java" and "MockedClassesAnalysis.java" at anytime you but make sure you finish the batch script part and the Eclipse SDK part already. Similarly, the ouput directory in all these files are absolute directories coded for myself, feel free to adjust it.
<br></br>
<h2> How the scripts are related to the research questions</h2>
<h3> RQ1 </h3>
How popular are mocking frameworks that are used in testing of open source Java software projects? How many projects use mocking frameworks? What kind of projects use mocking frameworks? What mocking frameworks are used?
<br></br>
These questions could be answered after we run the "AnalysisScript.bat", with the data from 'AllMetrics.csv', 'AllMockImports.csv', and 'AllMockFrameworks.csv'.
<br></br>
<h3> RQ2 </h3>
What features of mocking frameworks are most frequently used in the testing of open source software projects? What mocking framework methods are being used, and how much? How many objects are being mocked in a single file?
<br></br>
These questions could be answered after we run "API_Analyzer.java", "API_summurize.java" and "MockFrameworkFrequencyCalculator.java", You will get the mocking framework usage data categorized by project or by framework, and those data can be used to answer RQ2.
<br></br>
<h3> RQ3 </h3>
What types of dependencies developers tend to mock? Do developers tend to mock classes in their own project or the opposite? Which outside classes are mocked most frequently by all the developers?
<br></br>
These questions could be answered after we run "MockedClassesAnalysis.java". You will get the data of the name of internal and external classes by each project, and a few .csv files for summary.
<br></br>
<h3> RQ4 </h3>
Are there any files that are completely not created for testing but still using mocking frameworks? Are there any files that does not have mocking framework imports but have "mock" included in their file path?
<br></br>
The script for the first part is embedded in "API_Analyzer.java".



