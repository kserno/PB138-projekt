## PB138 project
### Europass CV XML Tools


The goal of the project is to create a set of tools being able to process the XML files produced by Europass Online Editor, compliant with EuroPass XML schema.


  - Store XML files into a XML native storage
  - Run XQueries on files in native storage
  - Present XML files as self-contained HTML5 pages
  - Export XML files as ZIP file
  

#### Program logic:

Program will work as CLI tool written in Java8 structured as a Maven Project. 

Program takes 4 options: --store / --query / --export_html / --export_zip and additional arguemnts depending on the selected option.

#### Requirements:
  - Java 8 with maven 
  - BaseX database running

#### Usage example:
  - Clone the repo
  - Run mvn install in root directory
  - Create a database named "europassDB" with root `<europasses/>` (these settings can be tweaked in configuration property file)
  - Choose one of the four options:
      - europasstoolkit store [path to file | multiple files]
      - europasstoolkit query [selected queries]
      - europasstoolkit export_html [path to file | multiple files]
      - europasstoolkit export_zip [path to file | multiple files]
      
  


#### Authors:
  - [@Adam Radvan](https://github.com/adamradvan): html export
  - [@Daniel Chorvatovič](https://github.com/dchorvat1): db querying
  - [@Jakub Petráš](https://github.com/Jakub2801): storage
  - [@Filip Sollár](https://github.com/kserno): zip export
