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