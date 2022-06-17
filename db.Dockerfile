FROM --platform=linux/x86_64 mysql:8.0.29

ENV character-set-server utf8mb4
ENV collation-server utf8mb4_general_ci
ENV default-character-set utf8mb4
ENV default-collation utf8mb4_general_ci

ENV MYSQL_DATABASE yousinsa
ENV MYSQL_ROOT_PASSWORD password

EXPOSE 3306

# docker build -t yousinsa:latest .
# docker run  -d -p 3306:3306 --name yousinsa yousinsa:latest
