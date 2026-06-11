param(
    [switch]$ResetDatabase,
    [switch]$SkipDatabase,
    [switch]$SkipInstall
)

$ErrorActionPreference = "Stop"
$Root = $PSScriptRoot
$BackendDir = Join-Path $Root "backend"
$FrontendDir = Join-Path $Root "frontend"
$LogDir = Join-Path $Root "logs"
$SqlScript = Join-Path $Root "sql\init.sql"

New-Item -ItemType Directory -Force -Path $LogDir | Out-Null

function Write-Step($Text) {
    Write-Host "[CampusActivity] $Text" -ForegroundColor Cyan
}

function Get-SqlcmdPath {
    $cmd = Get-Command sqlcmd.exe -ErrorAction SilentlyContinue
    if ($cmd) {
        return $cmd.Source
    }

    $fallback = "C:\Program Files\Microsoft SQL Server\Client SDK\ODBC\170\Tools\Binn\SQLCMD.EXE"
    if (Test-Path $fallback) {
        return $fallback
    }

    throw "sqlcmd.exe was not found. Please install SQL Server command line tools or SSMS first."
}

function Test-PortListening($Port) {
    $pattern = ":{0}\s+.*LISTENING" -f $Port
    $lines = netstat -ano | Select-String $pattern
    return [bool]$lines
}

function Start-ManagedProcess($Name, $WorkingDirectory, $Command, $OutFile, $ErrFile) {
    Write-Step "Starting $Name ..."
    $process = Start-Process powershell.exe `
        -WindowStyle Hidden `
        -WorkingDirectory $WorkingDirectory `
        -ArgumentList @("-NoProfile", "-ExecutionPolicy", "Bypass", "-Command", $Command) `
        -RedirectStandardOutput $OutFile `
        -RedirectStandardError $ErrFile `
        -PassThru
    Write-Step "$Name started. PID: $($process.Id)"
}

if (-not $SkipDatabase) {
    $sqlcmd = Get-SqlcmdPath
    $dbCheck = & $sqlcmd -S ".\SQLEXPRESS" -E -h -1 -W -Q "SET NOCOUNT ON; SELECT DB_ID('campus_activity');"
    $dbExists = ($dbCheck -join "").Trim() -notin @("", "NULL")

    if ($ResetDatabase -or -not $dbExists) {
        if ($ResetDatabase) {
            Write-Step "Resetting database campus_activity ..."
        } else {
            Write-Step "Database campus_activity does not exist. Initializing ..."
        }
        & $sqlcmd -S ".\SQLEXPRESS" -E -i $SqlScript
        Write-Step "Database initialization completed."
    } else {
        Write-Step "Database campus_activity already exists. Skipping initialization. Use -ResetDatabase to rebuild it."
    }
}

if (-not $SkipInstall) {
    if (-not (Test-Path (Join-Path $FrontendDir "node_modules"))) {
        Write-Step "Frontend dependencies are missing. Running npm install ..."
        Push-Location $FrontendDir
        npm install
        Pop-Location
    }
}

if (Test-PortListening 8080) {
    Write-Step "Port 8080 is already listening. Skipping backend startup."
} else {
    Start-ManagedProcess `
        -Name "backend service" `
        -WorkingDirectory $BackendDir `
        -Command ".\start.ps1" `
        -OutFile (Join-Path $LogDir "backend.log") `
        -ErrFile (Join-Path $LogDir "backend.err.log")
}

if (Test-PortListening 5173) {
    Write-Step "Port 5173 is already listening. Skipping frontend startup."
} else {
    Start-ManagedProcess `
        -Name "frontend service" `
        -WorkingDirectory $FrontendDir `
        -Command "npm run dev" `
        -OutFile (Join-Path $LogDir "frontend.log") `
        -ErrFile (Join-Path $LogDir "frontend.err.log")
}

Write-Host ""
Write-Host "Startup completed." -ForegroundColor Green
Write-Host "Frontend: http://localhost:5173/"
Write-Host "Backend:  http://localhost:8080/"
Write-Host "Logs:     $LogDir"
Write-Host ""
Write-Host "Demo accounts:" -ForegroundColor Green
Write-Host "Student:   2026001 / 123456"
Write-Host "Organizer: org001 / 123456"
Write-Host "Admin:     admin / 123456"
