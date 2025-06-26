@echo off
setlocal enabledelayedexpansion

:: Capture all arguments as the URL (handles spaces)
set "url=%*"

if "%url%"=="" (
    echo No URL provided.
    echo Usage: %~nx0 URL
    pause
    exit /b 1
)

:: Check if Chrome is running
tasklist /FI "IMAGENAME eq chrome.exe" | find /I "chrome.exe" >nul

if %errorlevel%==0 (
    echo Chrome is running.
) else (
    echo Chrome is not running.
)

:: Open the URL in Chrome
start "" "C:\Program Files\Google\Chrome\Application\chrome.exe" "!url!"
