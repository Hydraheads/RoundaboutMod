@echo off
setlocal
cd /d "%~dp0"
title JJF Roundabout Internal Mixin Cleanup - Fixed
cls
echo ================================================
echo JJF - Internal Mixin Cleanup (fixed launcher)
echo ================================================
echo.
echo Make sure the branch is JumpinJackFlash in GitHub Desktop.
echo Both fix files must be beside gradlew in the RoundaboutMod folder.
echo.
powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%~dp0Fix-JJF-Mixins.ps1"
set "EXITCODE=%ERRORLEVEL%"
echo.
if not "%EXITCODE%"=="0" (
    echo Fix stopped with error %EXITCODE%. Please send the STOPPED message.
) else (
    echo Fix completed. Compile both Fabric and Forge before pushing.
)
echo.
pause
