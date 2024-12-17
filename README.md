# MusicHub
MusicHub is site for sharing of music. It is created for Technical University Sofia.

## How to start application
1. Install MySQL 5.7
2. In application.properties we have following configuration, which should be passed from environmental variables:
- API_KEY
- API_SECRET
- CLOUD_NAME
- DB_ADDRESS
- DB_USERNAME
- DB_PASSWORD
- GOOGLE_CLIENT_ID
- GOOGLE_SECRET
- MAIL_USERNAME
- MAIL_PASSWORD
3. API_KEY, API_SECRET, CLOUD_NAME are related to Cloudinary CDN which is responsible for saving uploaded music files. This configuration should be obtained from Cloudinary Console.
4. DB_ADDRESS, DB_USERNAME, DB_PASSWORD are related to database configuration.
5. GOOGLE_CLIENT_ID, GOOGLE_SECRET should be obtained from Google Cloud Console.
6. MAIL_USERNAME, MAIL_PASSWORD are related to email client.
6. After MySQL installation and filled configuration, you can start immediately Spring Boot app.
7. To create automatically tables in database, should change property value of **spring.jpa.hibernate.ddl-auto** to be **create** in *application.properties* file.

## How can use MusicHub
1. All users, including anonymous can see all uploaded music files.
2. All users can listen music files.
3. Application supports search by category, tags, uploaded user.
4. Registered users can download, upload music files and write comments.
5. Application supports two type of authentication:
- Application authentication
- Google SSO
6. Users can use functionality for password change with email. 
7. Every user can edit or delete their own songs.
8. In application there is administrative part for users with admin or moderator role:
- Admin can manage all data in application
- Moderator can manage only tags and approve/reject comments 

Public codesandbox link:
https://githubbox.com/thunderstorm773/MusicHub
