@echo off
cd build\classes\main\code
for /F %%f in ('dir /b *.java') do (
	javac %%f
	java %%~nf
)
exit