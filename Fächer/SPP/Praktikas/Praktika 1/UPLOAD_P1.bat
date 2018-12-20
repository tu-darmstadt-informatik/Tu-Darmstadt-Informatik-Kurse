@echo off
if not exist ".\upload" mkdir upload

for %%i in (.\*) do (
  copy %%i .\upload
)

del .\upload\UPLOAD_P1.bat
if exist .\upload\*.pdf del .\upload\*.pdf

set /p id=Enter TU-ID : 

bash -c "scp ./upload/* %id%@lcluster4.hrz.tu-darmstadt.de:~/P1"
