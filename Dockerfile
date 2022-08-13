FROM theasp/clojurescript-nodejs:alpine as build
WORKDIR /app
COPY . .
CMD ["./run.sh"]