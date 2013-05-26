FimFictionAPI
=============
An API of many functions of http://FIMFiction.net/.

You can grab binary downloads at [my Jenkins](http://ci.yawk.at/job/FimFictionAPI).

Dependencies
-------------
- json-simple-1.1.1
- JUnit (for testing)
- YXML-1.0

Code Style
-----------
- Use [Oracle Code Conventions](http://www.oracle.com/technetwork/java/codeconv-138413.html)
- Do not wrap after 80 characters
- Always wrap arrays and enums if they are longer than 80 characters
- Do not use tabs. Use four spaces for every indentation level (see code conventions)
- Indent empty lines
- Do not wrap empty method or class declarations or empty blocks
- Always use blocks for if, for and while
- All variables, arguments and method return types are to be marked using the @Nullable annotation from Google Guava if they can contain null in a valid state
