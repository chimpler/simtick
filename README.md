[![Build Status](https://travis-ci.org/chimpler/simtick.svg?branch=master)](https://travis-ci.org/chimpler/simtick)

simtick
=======

    THIS IS A WORK IN PROGRESS. DO NOT USE.

Simple Java library to encode / decode tick data time series using delta compression.

### Features

Feature                                 | Status
----------------------------------------| :----:
Schema in header block                  | :x:
Partitioning                            | :x:
Auto discovery of delta encoding params | :x:
Codec command line tool                 | :x:

### Types supported

Type                           | Status
-------------------------------| :----:
Long (fixed)                   | :x:
Long (delta)                   | :white_check_mark:
Fixed decimal point (delta)    | :white_check_mark:
Fixed decimal point (fixed)    | :x:
Joda DateTime (delta)          | :white_check_mark:
Joda DateTime (fixed)          | :x:
Fixed length string (fixed)    | :x:
Variable length string (fixed) | :x:
Enum (fixed)                   | :x:
