::
@Echo off
Echo Creating git logs...
cd %1
git log --numstat --date=iso > ..\UndProjects\%1.log
cd ..
Echo Git log created.