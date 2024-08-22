@echo off

set DBMS=%1
set DB_USERNAME=root
set DB_PASSWORD=root

if /I "%DBMS%"=="mysql" (
    set DB_URL=jdbc:mysql://172.18.0.20:3306/goraebab
    set DB_DRIVER_CLASS_NAME=com.mysql.cj.jdbc.Driver
) else if /I "%DBMS%"=="postgresql" (
    set DB_URL=jdbc:postgresql://172.18.0.20:5432/goraebab
    set DB_DRIVER_CLASS_NAME=org.postgresql.Driver
) else if /I "%DBMS%"=="oracle" (
    set DB_URL=jdbc:oracle:thin:@172.18.0.20:1521:goraebab
    set DB_DRIVER_CLASS_NAME=oracle.jdbc.OracleDriver
) else if /I "%DBMS%"=="sqlserver" (
    set DB_URL=jdbc:sqlserver://172.18.0.20:1433;databaseName=goraebab
    set DB_DRIVER_CLASS_NAME=com.microsoft.sqlserver.jdbc.SQLServerDriver
) else (
    echo Unknown DBMS. Choose between 'mysql', 'postgresql', 'oracle', 'sqlserver'.
    exit /b 1
)

docker compose up "%DBMS%" -d
docker compose up backend -d

pause