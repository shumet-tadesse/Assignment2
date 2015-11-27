# Assignment2
RESTfull web service project
------------ How to run the Project-----------------------
1. create a dynamic project in eclipse
2.copy the packages from scr folder and add them to the eclipse project
3.copy the ivy.xml and build.xml files into your project
4.start the server by running the class introsde.rest.ehealth.App.java
5.run the client by executing the file introsde.rest.ehealth.MyClient.java
       or from gitbash type ant execute.client 
6. enter the baseUrl from the console  http://localhost:4843/sdelab/

--------Sample client code running and the results------------
ant execute.client:
     [echo] ant executing the client.
     [java] Enter Base Url: http://localhost:4843/sdelab/
http://localhost:4843/sdelab/
     [java] Request #1:GET/person Accept:application/xml Content-Type:application/xml
     [java] Result:OK
     [java] HTTP Status:200
     [java]  Number of people =95
     [java]
     [java] Request #2:GET/person/1Accept:application/xmlContent-Type:application/xml
     [java] Result:OK
     [java] HTTP Status:200
     [java]  Person First Name on list= Cristhian
     [java]
     [java] Request #3:GET/person/4353Accept:application/xmlContent-Type:application/xml
     [java] Result:OK
     [java] HTTP Status:200
     [java]  Person First Name on list= Chuck
     [java]
     [java] Request #4: POST /personAccept:application/xmlContent-Type:application/xml
     [java] Result:OK
     [java] HTTP Status:200
     [java]   id=4403<person><firstname>Chuck</firstname><lastname>Norris</lastname><birthdate>1945-01-01</birthdate><healthProfile><height>172</height><weight>78.9</weight></healthProfile></person>
     [java]
     [java] Request #5:GET/person/4403Accept:application/xmlContent-Type:application/xml
     [java] Result:OK
     [java] HTTP Status:200
     [java]  Person First Name on list= Chuck
     [java]
     [java] Request #9:GET/measureTypesAccept:application/xmlContent-Type:application/xml
     [java] Result:OK
     [java] HTTP Status:200
     [java]      Number of MeasurTypes =6
     [java]
     [java] Request #6: GET/measureTypes/1
     [java]  Accept:application/xml Content-Type:application/xml Result=OK
     [java] HTTP Status:200
     [java] weight  Number of measures= 17
     [java]  Accept:application/xml Content-Type:application/xml Result=OK
     [java] HTTP Status:200
     [java] height  Number of measures= 17
     [java]  Accept:application/xml Content-Type:application/xml Result=OK
     [java] HTTP Status:200
     [java] steps  Number of measures= 17
     [java]  Accept:application/xml Content-Type:application/xml Result=OK
     [java] HTTP Status:200
     [java] blood pressure  Number of measures= 17
     [java]  Accept:application/xml Content-Type:application/xml Result=OK
     [java] HTTP Status:200
     [java] heart rate  Number of measures= 17
     [java]  Accept:application/xml Content-Type:application/xml Result=OK
     [java] HTTP Status:200
     [java] bmi  Number of measures= 17
     [java] Request #6: GET/measureTypes/4353
     [java]  Accept:application/xml Content-Type:application/xml Result=OK
     [java] HTTP Status:200
     [java] weight  Number of measures= 0
     [java]  Accept:application/xml Content-Type:application/xml Result=OK
     [java] HTTP Status:200
     [java] height  Number of measures= 0
     [java]  Accept:application/xml Content-Type:application/xml Result=OK
     [java] HTTP Status:200
     [java] steps  Number of measures= 0
     [java]  Accept:application/xml Content-Type:application/xml Result=OK
     [java] HTTP Status:200
     [java] blood pressure  Number of measures= 0
     [java]  Accept:application/xml Content-Type:application/xml Result=OK
     [java] HTTP Status:200
     [java] heart rate  Number of measures= 0
     [java]  Accept:application/xml Content-Type:application/xml Result=OK
     [java] HTTP Status:200
     [java] bmi  Number of measures= 0
     [java] Request #7:GET/person/1/weight/1Accept:application/xmlContent-Type:application/xml
     [java] Result:OK
     [java] HTTP Status:200
     [java]
     [java]  measure count before update: 17
     [java] Request #8: POST /person/1/weight Accept:application/xml Content-Type:application/xml
     [java] Result:OK
     [java] HTTP Status:200
     [java] <measure><value>72</value><timestamp>2011-12-09</timestamp></measure>
     [java]
     [java] measure count after update using POST Request#8   18
     [java] Request #10: PUT /person/1/weight/904Accept:nullContent-Type:null
     [java] Result:Created
     [java] HTTP Status:201
     [java] <measure><value>90</value><timestamp>2011-12-09</timestamp></measure>
     [java] GET/person/1/weight/904 Accept:application/xml Content-Type:application/xml
     [java] Result:OK
     [java] HTTP Status:200
     [java]
     [java] Value after update: 90
     [java]
