export DBFILES=/home/kalyanc/Personal/CodeRepos/apps/deployusersrvcs/dbfiles


# 1)
# Create a network that acts like a LAN
# When we create a network, the docker assigns a network id and CIDR or subnet mask to the this network
# Each container within the network are assigned IP based on the network id and CIDR of the network
# If a name is assigned to the containers that are started in this network, the containers can communicate within the network using the assigned names
# Note that the container name is resolved to an IP address within this network automatically
docker network create simapp-network

# 2)
# start a PostGres DB containe with name SimDB
# The postgres DB by default listens on port 5432 within the simapp-network
# Expose the 5432 port of the container to 15432 of ths host so that SQL tools from outside of the container network can talk to database to check the database state
# Mapping the dbfiles in the local directory so that the required table and data are available in the database after start up
# Note as we are mapping a directory in host to the container data directory, the database changes are persisted in the host even after the database container is stopped
# -d option is to detach the container from the current shell
# -rm is to remove the container when we stop it
# --name is to give a logical name to the container
# -p to map host port 15432 to 5432 of the container
# --network=simapp-network to make sure the container runs in the simapp network

docker run --detach --rm --name SimDB -e POSTGRES_PASSWORD=admin123  --network=simapp-network -p 15432:5432  -v $DBFILES:/var/lib/postgresql/data  postgres:9.6-alpine

# 3)
# start the container for usersrvcs 
# the application properties within usersrvcs uses SimDB:5432 as host and port for DB connection
docker run --detach --rm --name UserSrvcs --network=simapp-network -p 9080:8080  usersrvcs:1.0

#Access user services on 9080 port of the localhost
#Access DB on port 15432 of the localhost
