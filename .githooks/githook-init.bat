@REM Getting directory of the script to run it independant of current working directory.
for %%i in ("%~dp0.") do SET "mypath=%%~fi"
cd mypath

ECHO "Activating githooks..."
ECHO "################################"

ECHO "Copying hooks in .githooks/ to .git/hooks/..."
COPY scripts\* ..\.git\hooks\
ECHO "################################"

ECHO "Editing the permissions of the file to make it executable..."
ICACLS ..\.git\hooks\* /grant %USERNAME%:RX 