# Stage 1: Build the Angular app
FROM node:18 AS build

# Set the working directory
WORKDIR /frontend

# Copy package.json and install dependencies
COPY package*.json ./
RUN npm install

# Copy the entire project and build it
COPY . .
RUN npm run build --prod

# Stage 2: Serve the Angular app using NGINX
FROM nginx:alpine

# Copy the build artifacts from the build stage to the NGINX html folder
COPY --from=build /dist/frontend /usr/share/nginx/html

# Expose the default port for NGINX
EXPOSE 80

# Start NGINX
CMD ["nginx", "-g", "daemon off;"]
