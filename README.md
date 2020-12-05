# equipmentManagement

This application is a base for assessing the capabilities of fullstack programmer candidates. It's a simple 
equipment management app for ACME store chain.

The technology stack uses Angular 10 with Bootstrap 4.5, Sass/CSS and webpack (web frontend), Java 11 with 
Spring Boot 22.7, Hibernate 5.4.15, Liquibase 3.9 and Maven (backend) to provide the core implementation 
for a end-to-end web application.

Below is the core information required for development:

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools.
You will only need to run this command when dependencies change in [package.json](package.json).

```
npm install
```

We use npm scripts and [Webpack][] as our build system.

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

```

./mvnw


npm start
```
These commands, together will enable browsersync, so when you change a file in the frontend layer (Angular, CSS, etc.), 
the browser automatically updates with the changes. The app will run at:
```
http://localhost:9000
```
For backend code, such as Java, you need to re-compile using an editor such as IntelliJ, or in a separate terminal, run:
```
./mvnw compile
```
And the server will update with browsersync still running.

Npm is also used to manage CSS and JavaScript dependencies used in this application.

The `npm run` command will list all of the scripts available to run for this project.

### Using Angular CLI

You can also use [Angular CLI][] to generate some custom client code.

For example, the following command:

```
ng generate component my-component
```

will generate few files:

```
create src/main/webapp/app/my-component/my-component.component.html
create src/main/webapp/app/my-component/my-component.component.ts
update src/main/webapp/app/app.module.ts
```

## Testing

To launch your application's tests, run:

```
./mvnw verify
```
or, if you want a clean rebuild and test, run:
```
./mvnw clean verify
```

### Client tests

Unit tests are run by [Jest][] and written with [Jasmine][]. They're located in 
[src/test/javascript/](src/test/javascript/) and can be run with:

```
npm test
```

For more information, refer to the [Running tests page][].

## Database

This project uses H2 with disk-based persistence database in development, so you will have an in-memory database 
running, and you can access its console at:
```
http://localhost:8080/h2-console
```
We use Liquibase to manage the database updates, and stores its configuration in the 
[src/main/resources/config/liquibase/](src/main/resources/config/liquibase/) directory.

If you need the clean/delete the database (recreating it on the next run), just delete the .db files at target/h2db/db/.

## Internationalization

Internationalization (or i18n) is set up at the beginning of your project (and not as an afterthought).
   
Usage is as follows:
   
With Angular, thanks to NG2 translate and a specific JHipster component, which uses JSON files for translation

For example, to add a translation to the “first name” field, add a “translate” attribute with a key: 
<label jhiTranslate="settings.form.firstname">First Name</label>
   
This key references a JSON document, which will return the translated String. Angular/React will then replace 
the “First Name” String with the translated version.

[node.js]: https://nodejs.org/
[yarn]: https://yarnpkg.org/
[webpack]: https://webpack.github.io/
[angular cli]: https://cli.angular.io/
[browsersync]: https://www.browsersync.io/
[jest]: https://facebook.github.io/jest/
