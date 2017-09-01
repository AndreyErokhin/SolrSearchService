#Running the app

1. Create Docker network
```bash
docker network create --subnet=172.18.0.0/16 mynet
```
2. Run the solr on docker
```bash
docker run --name my_solr --net mynet --ip 172.18.0.22 -d -p 8983:8983 -t solr
```
3. Create solr Core
```bash
docker exec -it --user=solr my_solr bin/solr create_core -c userIndex
```
Or if you are using windows
```bash
winpty docker exec -it --user=solr my_solr bin/solr create_core -c userIndex
```
