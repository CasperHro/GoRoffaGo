@echo OFF
for %%* in (.) do set CurrDirName=%%~nx*

if exist ..\sonar-scanner-2.8\bin\sonar-scanner.bat (

    ..\sonar-scanner-2.8\bin\sonar-scanner.bat
    timeout /T 2 /NOBREAK
    start "" http://localhost:9000/dashboard/index?id=%CurrDirName%

) else (
    cls
    echo.
    echo Kan sonarscanner niet vonden. Zorg dat de sonarQube en sonarscanner
    echo in de rootmap van het project staan.
    echo.
    echo Download SonarQube op: http://www.sonarqube.org/
    echo Download SonarQube Scanner van:
    echo http://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner
    echo.
    echo Start daarna eerst SonarQube met start-sonarqube.bat wacht tot 
    echo "SonarQube is up" wordt weergegeven en controleer op
    echo http://localhost:9000/ of de server draait.
    echo.
    echo Dan dit batch bestand opnieuw gestart worden.
    echo.
    echo.
    pause
)
