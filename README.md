# Description

This is a simple test tool to validate Java Kerberos authentication against a Microsoft SQL Server database. The tool requires two configuration file: SQLSERVER.conf and krb5.conf.

# SQLSERVER.conf

A sample file is provided in the zip distribution. This file can be used as is.

# krb5.conf

A sample file is provided, but it needs to be modified to fit your environment: specify the relevant domains, KDC and admin servers.

See https://web.mit.edu/kerberos/krb5-1.12/doc/admin/conf_files/krb5_conf.html for more information.

# Usage

The usage information will show as follows:
```
usage: SQL Server JDBC Kerberos Test Tool
 -?,--help             Show help
 -h,--host <arg>       Database host
 -P,--port <arg>       Database port
 -p,--password <arg>   Database user password
 -u,--user <arg>       Database user
 ```

The options `-h` is required to specify a host. The default port is 1433 and can be overridden using the option `-p`. If no user or no password is specified, a prompt will be displayed to provide the username and/or password.

An example call looks as follows:
```
java  -Dsun.security.krb5.debug=true -Djava.security.auth.login.config=SQLSERVER.conf -Djava.security.krb5.conf=krb5.conf -jar kerberos-test.jar -h sql.example.com -u user@example.com
```

The option `-Dsun.security.krb5.debug=true` enables kerberos debugging. The option `-Djava.security.auth.login.config=SQLSERVER.conf` tells Java where to find the `SQLSERVER.conf` file. An alternative location could be provided, if needed. Similarly, the option `-Djava.security.krb5.conf=krb5.conf` points to the `krb5.conf` file. The option `-jar` is needed and requires the jar file name (`kerberos-test.jar` as provided). 
