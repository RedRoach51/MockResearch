<h1>Mocking Research Scripts</h1>

This code and data is used for Prof. Lu Xiao's research project analyzing the mocking frameworks used in Apache's Java software projects. 

<h2> NOTES </h2>

The project uses [Scitools Understand](https://www.scitools.com/), and require Understand to be added to a computer's PATH variables before it will function.

After installing Understand, add 'und.exe' to the computer's PATH variables under environment variables. This allows the CMD commands for Understand to be run in the scripts.

Batch is used for data gathering, which is only compatible with Windows. Batch scripts from this project are not compatible with MacOS or Linux.

The project uses an Eclipse Plug-in Project, which requires an Eclipse SDK installation. 

Some projects require software that a clean SDK install will not have installed. To install this software, go to Help -> Install New Software, and in "work with", select m2e release repository. Install all of them if possible, it will help get rid of the compilation errors when importing most Maven projects. 

<br/>

<h1> How to use:</h1>

Clone the Git repositories from the software projects you are testing into a single directory. Multiple repositories can be cloned at once using either [BatchGitRepo.bat](BatchGitRepo.bat) or [DownloadApacheGitRepository.pl](DownloadApacheGitRepository.pl). 

Place all the scripts from "Erick scripts" inside the same directory as the Git repositories. Run AnalysisScript.bat in the command line

All project data will be sent into a new directory, 'UndProjects'. Each project's individual metrics will be generated and then processed into group datasets 'AllMetrics.csv', 'AllMockImports.csv', and 'AllMockFrameworks.csv'.

Open the Eclipse SDK, and import the "astSimple" project from the "Keye scripts" folder. Select Run as -> Eclipse application, which will create a new Eclipse instance called runtime-EclipseApplication. 

Follow these steps after creating the new runtime instance to run the plug-in project:

1. Import all projects that need to be analyzed into workspace in the running instance.
1. Modify the OUTPUT_DIECTORY variable to the directory that you want to store the output files.
1. Modify the PROJECT_REPOSITORY variable to the directory that contains all of your apache git repositories.
1. Run the sample command and all outputs will be generated into OUTPUT_DIECTORY.

If there are any conflicts between two projects, the plug-in project can be run on the projects individually as well.

When the process is complete for all projects, you are close to the end. In your normal Eclipse IDE, import the "Summer 2021" project. Run "API_Analyzer.java", "API_summarize", "MockFrameworkFrequencyCalculator.java", and "MockedClassesAnalysis.java". Make sure "API_Analyzer.java" is run first, and that the group metrics located under 'UndProjects' have been created.  

 Remember, you might need to look at the code carefully, as the directories are absolute directories coded specifically for my PC. Feel free to adjust on your own.

Visual diagram representing process flow:

![Visual Data Flow](Visual%20data%20flow.png)

<h1> Research Questions:</h1>

<h3> RQ1 </h3>

<h4> How popular are mocking frameworks that are used in testing of open source Java software projects? How many projects use mocking frameworks? What kind of projects use mocking frameworks? What mocking frameworks are used? </h4>

<br/>

These questions should be answered after running "AnalysisScript.bat" with the data from 'AllMetrics.csv', 'AllMockImports.csv', and 'AllMockFrameworks.csv'.


<h3> RQ2 </h3>

<h4>What features of mocking frameworks are most frequently used in the testing of open source software projects? What mocking framework methods are being used, and how much? How many objects are being mocked in a single file?</h4>

<br/>

These questions should be answered after running "API_Analyzer.java", "API_summurize.java" and "MockFrameworkFrequencyCalculator.java". The mocking framework usage data will be categorized by project and by framework, both of which can be used to answer RQ2.

<h3> RQ3 </h3>

<h4>What types of dependencies developers tend to mock? Do developers tend to mock classes in their own project or the opposite? Which outside classes are mocked most frequently by all the developers?</h4>

<br/>

These questions should be answered after running "MockedClassesAnalysis.java". You will get data containing the names of internal and external mocked classes for each project, and a few .csv files for summary.

<h3> RQ4 </h3>

<h4>Are there any files that are completely not created for testing but still using mocking frameworks? Are there any files that does not have mocking framework imports but have "mock" included in their file path?</h4>

<br/>

<div>The script for the first part is embedded in "API_Analyzer.java". The script for the second part is embedded in "CSVReader.py".</div> 