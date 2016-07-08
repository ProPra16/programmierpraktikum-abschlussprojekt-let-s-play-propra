@echo off
cd build\libs\code
for /F %%f in ('dir /b *.java') do (
	javac %%f
	java %%~nf
)
exit