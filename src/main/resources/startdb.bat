@echo off
echo "set environment variable of Derby : " %cd%
echo "**********************************************"
set DERBY_HOME=%cd%\database\derby
set path=%DERBY_HOME%\bin;%PATH%
call %DERBY_HOME%\bin\ij.bat
echo "**********************************************"
echo "init database end"
pause