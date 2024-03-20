FROM node:latest
WORKDIR /g/cloud-storage/netology-diplom-frontend
COPY package*.json ./
EXPOSE 8081
RUN npm install
COPY . .
CMD ["npm",  "run", "serve"]