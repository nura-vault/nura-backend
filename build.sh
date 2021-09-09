echo password: laksjdhfg
openssl pkcs12 -export -out src/main/resources/certificate.p12 -inkey /etc/letsencrypt/live/micartey.dev/privkey.pem -in /etc/letsencrypt/live/micartey.dev/cert.pem -certfile /etc/letsencrypt/live/micartey.dev/chain.pem
docker-compose up --build