echo "Use the following curl commands to create/get/update/get all/delete:"
echo ""
#1) To Create three users
echo "curl -X POST -d '`cat ./user1.json`'  -H 'Content-Type: application/json' http://localhost:8080/usersrvcs/add-user"
echo "curl -X POST -d '`cat ./user2.json`'  -H 'Content-Type: application/json' http://localhost:8080/usersrvcs/add-user"
echo "curl -X POST -d '`cat ./user3.json`'  -H 'Content-Type: application/json' http://localhost:8080/usersrvcs/add-user"

#2) To see a user
echo "curl -X GET -v http://localhost:8080/usersrvcs/id"

#3) To modify a user
echo "curl -X PUT -d '`cat ./modifieduser.json`' -H 'Content-Type: application/json' http://localhost:8080/usersrvcs/id"

#4) To see all users
echo "curl -X GET -v http://localhost:8080/usersrvcs"

#5) To delete a user
echo "curl -X DELETE http://localhost:8080/usersrvcs/id"

#curl -d `cat ./testmsg.json`  -H 'Content-Type: application/json' http://localhost:8080/add-user
#curl -d "user=user1&pass=abcd" -X POST https://example.com/login
#curl --form "fileupload=@my-file.txt" https://example.com/resource.cgi
#curl -X POST https://example.com/

