:: This script should be placed in the same directory as AnalysisScript.bat.
@Echo off
Echo Updating local repository from Github...
cd %1
git pull
cd ..
