$ErrorActionPreference = 'Stop'
Add-Type -AssemblyName System.IO.Compression.FileSystem
$projectRoot = [IO.Path]::GetFullPath((Join-Path $PSScriptRoot '..'))
$archivePath = Join-Path $projectRoot 'EGCO213-Project-main.zip'
$archive = [IO.Compression.ZipFile]::OpenRead($archivePath)
$prefix = 'EGCO213-Project-main/'
$count = 0
try {
    foreach ($entry in $archive.Entries) {
        if (-not $entry.FullName.StartsWith($prefix)) { continue }
        $relative = $entry.FullName.Substring($prefix.Length)
        if ($relative -notmatch '^src/main/java/Project3_6713120/(resource|fonts)/') { continue }
        if ($entry.FullName.EndsWith('/') -or $relative.EndsWith('.java')) { continue }
        $destination = [IO.Path]::GetFullPath((Join-Path $projectRoot $relative))
        if (-not $destination.StartsWith($projectRoot + [IO.Path]::DirectorySeparatorChar, [StringComparison]::OrdinalIgnoreCase)) {
            throw 'Archive entry is outside the project directory.'
        }
        # Preserve media that a developer has already restored or edited.
        if (Test-Path -LiteralPath $destination) { continue }
        [IO.Directory]::CreateDirectory([IO.Path]::GetDirectoryName($destination)) | Out-Null
        [IO.Compression.ZipFileExtensions]::ExtractToFile($entry, $destination, $false)
        $count++
    }
} finally {
    $archive.Dispose()
}
Write-Host "Restored $count media/font files from the original archive."
