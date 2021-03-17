# docker run --rm --name UserSrvcs -p 8080:8080  usersrvcs:1.0

# When using --net=host, the ports from the container are automatically mapped to host. So no need to mention the ports
# The ports 
docker run --rm --name UserSrvcs --net=host  usersrvcs:1.0
