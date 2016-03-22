[![Build Status](https://travis-ci.org/chimpler/simtick.svg?branch=master)](https://travis-ci.org/chimpler/simtick)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/a3d06125f57243be8b9d399c607f575b)](https://www.codacy.com/app/francois-dangngoc/simtick)
[![Codacy Badge](https://api.codacy.com/project/badge/coverage/a3d06125f57243be8b9d399c607f575b)](https://www.codacy.com/app/francois-dangngoc/simtick)
[![Dependency Status](https://www.versioneye.com/user/projects/56d251e4157a690037bbb757/badge.svg?style=flat)](https://www.versioneye.com/user/projects/56d251e4157a690037bbb757)
[![GitHub License](https://img.shields.io/github/license/chimpler/pyhocon.svg)]

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
Delta at row level                      | :white_check_mark:
Delta at field level                    | :white_check_mark:

### Types supported

Type                           | Status
-------------------------------| :----:
Long (fixed)                   | :white_check_mark:
Long (delta)                   | :white_check_mark:
Fixed decimal point (delta)    | :white_check_mark:
Fixed decimal point (fixed)    | :white_check_mark:
Joda DateTime (delta)          | :white_check_mark:
Joda DateTime (fixed)          | :white_check_mark:
Fixed length string (fixed)    | :white_check_mark:
Variable length string (fixed) | :white_check_mark:
Enum (fixed)                   | :x:

### TODO

- implement input and output stream
