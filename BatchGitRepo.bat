:: This script should be placed in the same directory as AnalysisScript.bat.

:: This script takes an input of Apache git repository names and clones each repository.
:: Ex: BatchGitRepo.bat xxx xxx xxx xxx ...
@Echo off
SETLOCAL ENABLEDELAYEDEXPANSION

	Echo Running 'BatchGitRepo'...

for %%x in (%*) do (
	Echo https://github.com/apache/%%x...
	git clone https://github.com/apache/%%x
	Echo https://github.com/apache/%%x... cloned/n
)
