
# basis-period-reform-api-stubs

This repository contains stubbed endpoints for use in testing the basis period reform api

## Stubbing
To add more stubs:

There are a collection of stubs inside of `resources/stubs/`
Add a new Json file inside the folder which matches the endpoint in the following format `utr<utr>_part<partnership_ref_number>.json` this will get picked up automatically and returned with a 200
If you need to add an error go into StubController and add to the map at the top for your case

## Requirements

This service is written in [Scala](http://www.scala-lang.org/) and [Play](http://playframework.com/), so needs at least
a [JRE] to run.

## Tests

The tests include unit tests and integration tests.
In order to run, use this command line:

```
./run_all_tests.sh
```

## Run the application

To run the application use the `run_local.sh` script

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").