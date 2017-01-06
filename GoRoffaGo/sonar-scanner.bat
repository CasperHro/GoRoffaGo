@echo OFF
for %%* in (.) do set CurrDirName=%%~nx*

if exist C:\tmp_install\sonar-scanner-2.8\bin\sonar-scanner.bat (

    C:\tmp_install\sonar-scanner-2.8\bin\sonar-scanner.bat
    start "" http://localhost:9000/dashboard?id=%CurrDirName%

) else (
    cls
    echo.
    echo Download SonarQube and Sonar-scanner and extract the zips in de folder:
    echo   C:\tmp_install\
    echo.
    echo Download SonarQube op: http://www.sonarqube.org/
    echo Download SonarQube Scanner van:
    echo http://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner
    echo.
    echo Start daarna eerst SonarQube met StartSonar.bat uit de bin map en 
    echo controleer op http://localhost:9000/ of de server draait.
    echo.
    echo Dan dit batch bestand opnieuw gestart worden.
    echo.
    echo.
    pause
)