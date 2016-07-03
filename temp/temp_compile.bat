@echo off
for /F %%f in ('dir /b D:\Workspace\TDDT\temp\*.java') do (
	javac %%f
	java %%~nf >%%f.log
)
pause