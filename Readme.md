# Asssociation rules
This project allows for the analysis of Photovoltaic Geographical Information System (PVGIS) data and the formation of association rules between available metrics. Although the project is targeted at PVGIS data and the interfaces are tied to its format, the algorithms are implemented in a way that allows them to be used with various types of input data.

## Interface and usage
The project has simple terminal user interface and can be used on any system with Java 11+ installed.
THe project also has a `*.bat` file for a convenient startup on Windows systems.

Input data format is only `.csv` files formed on https://re.jrc.ec.europa.eu/pvg_tools/

## How to build project
The project requires Java 11+ and maven to be installed.
To build project use maven from the root directory:
```sh
mvn clean install
```

## Testing
The project has unit tests. To run them, use maven:
```sh
mvn clean test
```

