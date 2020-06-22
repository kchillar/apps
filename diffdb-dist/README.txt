1) The main use of diffdb-dist utility is to compare two MySQL Databases at a time

2) This distributed has been tested with Amazon provided Java distribution version amazon-corretto-11.jdk

3) The files needed to get started are :
./run.sh
./conf/db-config.xml
./output

4) Note run.sh uses /bin/bash shell

5) The configuration in conf/db-config.xml looks like this:

<db-connection-manager-config>

   <db-connection-helper-classname>com.pmc.fw.db.basic.BasicDBConnectionHelper</db-connection-helper-classname>

    <db-connection-helper-config name="DB1">
     <db-url>jdbc:mysql://127.0.0.1:3306/expdb</db-url>
     <driver-class>com.mysql.jdbc.Driver</driver-class>
     <username>appuser1</username>
     <password>whatever</password>
    </db-connection-helper-config>
    
    <db-connection-helper-config name="DB2">
     <db-url>jdbc:mysql://127.0.0.1:3306/expdb2</db-url>
     <driver-class>com.mysql.jdbc.Driver</driver-class>
     <username>appuser2</username>
     <password>whatever</password>
    </db-connection-helper-config>

    <db-connection-helper-config name="DB3">
     <db-url>jdbc:mysql://127.0.0.1:3306/expdb2</db-url>
     <driver-class>com.mysql.jdbc.Driver</driver-class>
     <username>appuser2</username>
     <password>whatever</password>
    </db-connection-helper-config>

</db-connection-manager-config>

6) When ./run.sh is executed the options look like below:


----------------------------------------------------
0 -- Show DB Names ( event-id:Show_DB_Names, view-id: /0 )
1 -- Compute Common Tables ( event-id:Compute_Common_Tables, view-id: /1 )
2 -- Show Common Tables ( event-id:Show_Common_Tables, view-id: /2 )
3 -- Compute Table Data Diff ( event-id:Diff_DB_Table, view-id: /3 )
4 -- Set DB Names ( event-id:Set_DB_Names, view-id: /4 )
Q -- To exit 
Please enter an option: 

7) By default, two databases DB1 and DB2 are selected to be diffed. This can be changed by selecting option 4 and specifying any  names given db-config.xml

8) Once the databases to be diffed are set, option 1 should be run to compute the list of common tables. The utility will remember the common tables unitil they are recomputed using option 1 again !!!

9) Option 2, will display the list of common tables between the two databases

10) Option 3, will ask for index of the table (from the display results of option 2) to be diffed. Once a valid index is specified, it will compare the data in two tables and generate txt files containing missing data (if any) in output directory.

11) Finally when diffing table with large differences, lot of memory may be consumed, change memory settings -Xmx1024M  or more, in run.sh

$JAVA_HOME/bin/java -Xms64M -Xmx512M -cp $CLASSPATH com.pmc.fw.resources.ResourceInitializer ./conf


Output from SAMPLE RUN:

./run.sh

----------------------------------------------------
0 -- Show DB Names ( event-id:Show_DB_Names, view-id: /0 )
1 -- Compute Common Tables ( event-id:Compute_Common_Tables, view-id: /1 )
2 -- Show Common Tables ( event-id:Show_Common_Tables, view-id: /2 )
3 -- Compute Table Data Diff ( event-id:Diff_DB_Table, view-id: /3 )
4 -- Set DB Names ( event-id:Set_DB_Names, view-id: /4 )
Q -- To exit 
Please enter an option: 0
First Database Name: DB1, Second Database Name: DB2



----------------------------------------------------
0 -- Show DB Names ( event-id:Show_DB_Names, view-id: /0 )
1 -- Compute Common Tables ( event-id:Compute_Common_Tables, view-id: /1 )
2 -- Show Common Tables ( event-id:Show_Common_Tables, view-id: /2 )
3 -- Compute Table Data Diff ( event-id:Diff_DB_Table, view-id: /3 )
4 -- Set DB Names ( event-id:Set_DB_Names, view-id: /4 )
Q -- To exit 
Please enter an option: 1
Compute Common tables 
Common Tables: 
 |--> client_tbl



----------------------------------------------------
0 -- Show DB Names ( event-id:Show_DB_Names, view-id: /0 )
1 -- Compute Common Tables ( event-id:Compute_Common_Tables, view-id: /1 )
2 -- Show Common Tables ( event-id:Show_Common_Tables, view-id: /2 )
3 -- Compute Table Data Diff ( event-id:Diff_DB_Table, view-id: /3 )
4 -- Set DB Names ( event-id:Set_DB_Names, view-id: /4 )
Q -- To exit 
Please enter an option: 2
Common Tables Are: 
 --0 -> client_tbl



----------------------------------------------------
0 -- Show DB Names ( event-id:Show_DB_Names, view-id: /0 )
1 -- Compute Common Tables ( event-id:Compute_Common_Tables, view-id: /1 )
2 -- Show Common Tables ( event-id:Show_Common_Tables, view-id: /2 )
3 -- Compute Table Data Diff ( event-id:Diff_DB_Table, view-id: /3 )
4 -- Set DB Names ( event-id:Set_DB_Names, view-id: /4 )
Q -- To exit 
Please enter an option: 3
Enter value for TableIndex (default=0): 0
Will process table: client_tbl



----------------------------------------------------
0 -- Show DB Names ( event-id:Show_DB_Names, view-id: /0 )
1 -- Compute Common Tables ( event-id:Compute_Common_Tables, view-id: /1 )
2 -- Show Common Tables ( event-id:Show_Common_Tables, view-id: /2 )
3 -- Compute Table Data Diff ( event-id:Diff_DB_Table, view-id: /3 )
4 -- Set DB Names ( event-id:Set_DB_Names, view-id: /4 )
Q -- To exit 
Please enter an option: q

kalyanc@kalmacs-MacBook-Pro ~/diffdb-dist
$ ls -l output/
total 40
-rw-r--r--  1 kalyanc  staff   69 Jun 13 18:55 data-only-in-DB1-client_tbl.txt
-rw-r--r--  1 kalyanc  staff   62 Jun 13 18:55 data-only-in-DB2-client_tbl.txt
-rw-r--r--  1 kalyanc  staff  352 Jun 13 18:55 tables-in-both-DB1-DB2.xml
-rw-r--r--  1 kalyanc  staff  365 Jun 13 18:55 tables-missing-in-DB1.xml
-rw-r--r--  1 kalyanc  staff  592 Jun 13 18:55 tables-missing-in-DB2.xml




