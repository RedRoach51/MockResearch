::
@Echo off
Echo Creating git logs...
cd %1

git log --numstat --date=iso > ..\UndProjects\%1.log
git log --tags --simplify-by-decoration --pretty="format:%%ci %%d%%x0D" > ..\UndProjects\%12.log

cd ..
Echo Git logs created.