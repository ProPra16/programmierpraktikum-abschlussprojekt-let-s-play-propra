::@echo off
for /F %%f in ('dir /b temp\*.java') do (
	javac temp\%%f
	java temp\%%~nf
)
pause