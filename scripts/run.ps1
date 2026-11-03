param([switch]$BuildOnly)
$ErrorActionPreference = 'Stop'
$projectRoot = [IO.Path]::GetFullPath((Join-Path $PSScriptRoot '..'))
Push-Location $projectRoot
try {
    & (Join-Path $PSScriptRoot 'prepare-assets.ps1')
    New-Item -ItemType Directory -Path 'target/classes' -Force | Out-Null
    $sources = @(Get-ChildItem -LiteralPath 'src/main/java' -Recurse -Filter '*.java' -File |
        ForEach-Object { '"' + $_.FullName.Replace('\', '/') + '"' })
    [IO.File]::WriteAllLines((Join-Path $projectRoot 'target/sources.txt'), [string[]]$sources, (New-Object Text.UTF8Encoding($false)))
    & javac -encoding UTF-8 --release 21 -d target/classes '@target/sources.txt'
    if ($LASTEXITCODE -ne 0) { throw "Java compilation failed with exit code $LASTEXITCODE" }
    Write-Host "Compiled $($sources.Count) Java source files."
    if (-not $BuildOnly) {
        & java -cp target/classes Project3_6713120.MainApplication
        if ($LASTEXITCODE -ne 0) { throw "Game exited with code $LASTEXITCODE" }
    }
} finally {
    Pop-Location
}
