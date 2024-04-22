# Distance Between Countries

This repository allows you to search for distance between countries.

## Table of Contents

- [Introduction](#introduction)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Usage](#usage)
- [Endpoints](#endpoints)

## Introduction

This is a basic REST API application built using [Spring Boot](https://spring.io/projects/spring-boot)  framework [Gradle](https://gradle.org/). The application allows the user to obtain distance information between countries around the world by sending a request to predefined endpoints.

## Technologies Used

- [Spring Boot](https://spring.io/projects/spring-boot): Web framework for building the REST API.
- [Geographical Coordinates of Countries of the world](https://www.nationmaster.com/country-info/stats/Geography/Geographic-coordinates): External API for obtaining information about the coordinates of countries around the world.

## Getting Started

### Prerequisites

Make sure you have the following installed:

- Java (version 21)
- Gradle

### Installation

1. Clone the repository:

    ```bash
    git clone https://github.com/IvanKhilko/DistanceBetweenCountries
    ```

The application will start on `http://localhost:8080`.

## Usage

### Endpoints

- **Getting the request data based on the name and type of the request:** 
  
  ```http
  GET /api/countries/all
  ```
  ```http
  GET /api/countries/info/?country=countryName
  ```
  ```http
  GET /api/countries/distance/{firstCountry}-{secondCountry}
  ```


  Example:
  ```http
  GET /api/countries/distance/Germany-Belarus
  ```
  
  Retrieves information about the distance between two specified countries.
