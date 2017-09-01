#Running the app

1.Create Docker network
```bash
docker network create --subnet=172.18.0.0/16 mynet
```
2.Run the solr on docker
```bash
docker run --name my_solr --net mynet --ip 172.18.0.22 -d -p 8983:8983 -t solr
```
