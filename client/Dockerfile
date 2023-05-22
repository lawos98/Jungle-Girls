FROM node:14
WORKDIR /app
COPY package*.json .
COPY . .
EXPOSE 5173
RUN npm install
CMD ["npm", "run","dev","--","--host"]