@echo off
cd build\libs\aufgaben\%1
for /F %%f in ('dir /b *.java') do (
	javac %%f
	java %%~nf
)
exit