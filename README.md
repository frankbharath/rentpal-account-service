# Rentpal Account Service

This document describes Rentpal account service and API's that were developed.

## Purpose
When developing any web application, there are two things that needs to be considered
1. Authentication - To identify the authenticity of the users, an act of validating that users are whom they claim to be.
2. Authorization - A process of giving the user permission to access a specific resource or function. Not every authenticated user can access every resource available. For example, a normal user cannot access admin related features.

## Technical Stack
- Frontend - Javascript and AngularJS
- Backend - Java and Spring Security
- Unit Test - JUnit and Mockito
- Database - Postgres and Redis

## Design Considerations
- User informations are stored in Postgres with index created on primary key and email.
- User session in stored in redis that can be scaled horizantally.
- All URL's except for GET request are protected with CSRF token.

## API
| URL | Type | Parameter | Description |
| --- | --- | --- | --- |
| /login | GET | - | Returns login page |
| /login | POST | username, password| Authentication url |   
| /register | GET | - | Return registration page |
| /register | POST | username, password and confirmPassword | Creates new user |
| /reset | GET | - | Return password reset html |
| /reset | POST | email | Creates new password reset link |
| /reset | PUT | password, confirmPassword, token | Updates password if token is valid |
| /verify | GET | token | Verifies user account if token is valid |

## Screenshots
![image](https://user-images.githubusercontent.com/49817583/100555823-65425680-329e-11eb-9833-6cc17efdd014.png)
![image](https://user-images.githubusercontent.com/49817583/100555836-768b6300-329e-11eb-9acd-6882817324e8.png)
![image](https://user-images.githubusercontent.com/49817583/100555843-886d0600-329e-11eb-8a7b-e466163b9c09.png)

