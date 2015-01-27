# To create the postgres docker container
# docker run -d --name jpo_postgres -p 5432:5432 postgres:9.4
# 
docker start jpo_postgres;

# To create the oracle docker container
# docker run -d --name jpo_oracle -p 49160:22 -p 49161:1521 wnameless/oracle-xe-11g
# 
docker start jpo_oracle;


# To create the mysql docker container
# docker run -d --name jpo_mysql -e MYSQL_ROOT_PASSWORD=rootpassword -e MYSQL_DATABASE=mysql -e MYSQL_USER=mysql -e MYSQL_PASSWORD=mysql -p 3306:3306 mysql:5.5
# 
docker start jpo_mysql;