$ErrorActionPreference = "Stop"
$tcp = Get-ItemProperty 'HKLM:\SOFTWARE\Microsoft\Microsoft SQL Server\MSSQL16.SQLEXPRESS\MSSQLServer\SuperSocketNetLib\Tcp\IPAll' -ErrorAction SilentlyContinue
if ($tcp -and $tcp.TcpDynamicPorts) {
    $env:DB_URL = "jdbc:sqlserver://localhost:$($tcp.TcpDynamicPorts);databaseName=campus_activity;encrypt=true;trustServerCertificate=true;integratedSecurity=true"
}
mvn spring-boot:run "-Dspring-boot.run.jvmArguments=-Djava.library.path=$PSScriptRoot\auth"
