:: This script should be placed in the same directory as 
:: the project directories alongside the UndProjects directory.

@ECHO off
ECHO.
SETLOCAL ENABLEDELAYEDEXPANSION

:: Add any directories the script should ignore here.
set NonProjectDirectory=MockResearch UndProjects

:: Set metrics that the script should record here.
set Metrics=CountLineCode Cyclomatic AvgLineCode SumEssential


:: Searches for project directories in local folder and updates
:: Understand projects stored in 'UndProjects' accordingly.	
for /D %%X in ("*") DO (
	set validProject=true
	for %%y in (%NonProjectDirectory%) do (
		if %%X==%%y (
			set validProject=false
		)
	)
	if !validProject!==true (
		echo Found Project:  %%X
		call GitRepoUpdater.bat %%X
		if not exist UndProjects\%%X.und (
			echo No previous Understand project created for %%X, initializing...
			und create -languages java UndProjects\%%X.und 
		)
		und add %%X UndProjects\%%X.und
		call GitLogWriter.bat %%X
		echo.
	)
)

:: Perform analysis on all projects in 'UndProjects'.
Echo Analyzing files in '\UndProjects'.
Echo.

cd UndProjects
for /D %%X in ("*.und") do (
	echo Analyzing Project:  %%X
	und analyze -changed %%X
	und settings -metricFileNameDisplayMode RelativePath %%X
	und settings -metrics %Metrics% %%X
	echo Generating Metrics...
	und metrics %%X
	echo Metrics Generated
	echo.
) 

cd ..
Echo Running 'CSVReader.py'.
Echo.
python CSVReader.py
Echo.

:skip
Echo -- AnalysisScript.bat Complete --
