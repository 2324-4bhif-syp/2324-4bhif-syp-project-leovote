# Stage 1: Compile and Build angular codebase

# Use official node image as the base image
FROM node:20 as build

WORKDIR /usr/src/app

COPY . /usr/src/app

# Install all the dependencies
RUN npm install

# Generate the build of the application
RUN npm run build

# Stage 2: Serve app with nginx server

# Use official nginx image as the base image
FROM nginx:1.24.0

# Copy the build output to replace the default nginx contents.
COPY --from=build /usr/src/app/dist/frontend /usr/share/nginx/html

# Expose port 80
EXPOSE 80
