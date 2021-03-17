#docker run --rm --name SimDB -e POSTGRES_PASSWORD=admin123  -p 5432:5432 -v $HOME/PostGresData/SimDB:/var/lib/postgresql/data  postgres:9.6-alpine


# Note when we use --net=host the ports of the container are directly mapped to the host ports. So no need to do port mappings with -p
docker run --rm --name SimDB -e POSTGRES_PASSWORD=admin123  --net=host -v $HOME/PostGresData/SimDB:/var/lib/postgresql/data  postgres:9.6-alpine
