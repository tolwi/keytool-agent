# keytool-agent
Java agent to workaround keytool bugs

To workaround "**Parse Generalized time, invalid format**" issue use `-J-Xbootclasspath/p:keytool-agent-1.0.jar -J-javaagent:keytool-agent-1.0.jar` arguments:

```sh
> keytool -list -v -keystore test.keystore
> keytool error: java.security.cert.CertificateParsingException: java.io.IOException: Parse Generalized time, invalid format
```
```sh
> keytool -list -v -keystore test.keystore -J-Xbootclasspath/p:keytool-agent-1.0.jar -J-javaagent:keytool-agent-1.0.jar
> >>> catched Parse Generalized time, invalid format .. returning current date
> Enter keystore password:  
```
