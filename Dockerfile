FROM theasp/clojurescript-nodejs:alpine as build
RUN mkdir -p /movie-night
WORKDIR /movie-night
COPY . /movie-night

RUN npm install
RUN npm run release

FROM nginx:alpine
COPY --from=0 /movie-night/resources/public /usr/share/nginx/html