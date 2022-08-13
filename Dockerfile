FROM theasp/clojurescript-nodejs:alpine as build
COPY . /app
WORKDIR /app
CMD ["./run.sh"]