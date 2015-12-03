1. Simple Utility to analyze your web access log to help you identify potential traffic, response times (min/max/avg), most used URL's etc.

2. Download the web-log-analyzer.jar & web-log-analyzer.sh from downloads link. Execute web-log-analyzer.sh the script with appropriate arguments as detailed out below.

3. Currently supported web servers - Tomcat, Apache, JBoss

<a href='http://code.google.com/p/web-log-analyzer/downloads/list'>Download</a>

web-log-analyzer.sh:

java -cp ./web-log-analyzer.jar `org.web.acesslog.AccessReader` "Time Taken: %T %h %l %u %t %r %s %b" `org.web.acesslog.parser.ApacheParserImpl` `org.web.report.TextReport` `".jsp,.html,.xhtml,ajax,AjaxAction"` localhost\_access\_log.2011-06-02.log

1. `org.web.acesslog.AccessReader` - Java Main class for the module.

2. `"Time Taken: %T %h %l %u %t %r %s %b"` - Tomcat access log format, make sure to set this value same as your web server access log pattern.

3. `org.web.acesslog.parser.ApacheParserImpl` - Parser logic specific to tomcat, you can extend it or overwrite it as per your need.

4. `org.web.report.TextReport` - Type of report generate implementation to use, at the moment only text based reporting is supported.

5. `".jsp,.html,.xhtml,ajax,AjaxAction"` - File extensions & keywords on the url that you are interested in analyzing.

6. `localhost_access_log.2011-06-02.log` - your access log.