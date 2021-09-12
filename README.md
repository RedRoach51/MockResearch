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
After finish instalation of eclipse SDK, go to "Keye scripts" folder, import "astSimple" in your eclipse SDK. In project explorer, right-click "astSimple", select Run as -> Eclipse applicatoin, you will see a new Eclipse pop out, called runtime-EclipseApplication. 
<br></br>
In the runtime application, import the projects you are analyzing only one per time. For each, click sample menu -> sample command on the very top, it will help you generate mocking API related data for the specific project you imported.
<br></br> 
Remember, you might need to look at the code carefully, especially the GetInfo.java file, the ouput directory is an absolute directory coded specifically for my PC, feel free to adjust on your own. 
<br></br>
If you finished the process for all projects, you are close to the end. In your normal Eclipse IDE, create a project and import "API_Analyzer.java", "API_summurize.java", "MockFrameworkFrequencyCalculator.java" from "Keye scripts" folder. Run "API_Analyzer.java" first and then "API_summurize.java". You can run the "MockFrameworkFrequencyCalculator.java" at anytime you want since that is a separate file. Similarly, the ouput directory in all these files are absolute directories coded for myself, feel free to adjust it.
<br></br>
More scripts are coming up for RQ3
