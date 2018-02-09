# Camel CXF memory test project

See [CAMEL-11370](https://issues.apache.org/jira/browse/CAMEL-11370) Problem with MTOM in Camel-CXF

Generate JAXB java classes:

    ./gradlew generateWsdlJavaClasses
    
Run server:

    ./gradlew bootRun   
  
Query WSDL:

    http://localhost:8080/logfiles?WSDL     
    
Memory test, i.e.:

    free heap            =     123'172'176
    file 50mb.log        =      52'565'760
    free heap            =     449'008'600

I.e., for handling a 50 MB file the heap usage is 449 - 123 MB = 326 MB. Using jvisualvm, one can see that the maximum heap usage is normally even in the order of 1 GB.
