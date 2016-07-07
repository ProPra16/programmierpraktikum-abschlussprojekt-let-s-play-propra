@echo off
for /F %%f in ('dir /b *.java') do (
	javac %%f
	echo Main-Class: %%~nf > Manifest.txt
	jar -cvfm Test.jar Manifest.txt %%~nf.class
)

java -jar Test.jar

pause