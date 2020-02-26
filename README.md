# Severino

An http client testing tool. You can use it to check:

  * your client can handle random errors (status codes)
  * your client's outgoing request (gets printed on the server terminal)
  * how many requests your client makes in a time window, to se if your throttling is working
  * how your client reacts to responses taking too long
  and anything else you can think of given the possible funcitonalities


## Usage
    java -jar severino.jar port max-connetions request-duration [status-code=chance]+
or, concretely:

    java -jar severino.jar 8080 10 10000 403=1 201=3
or, from docker:

    docker run -p 8080:8080 yorggen/severino:latest 8080 2 10000 403=1 201=3

  Args:
  * port (int): desired port to serve
  * max-connections (int): will handle at most this number of connections, responding with 503 immediatly if this limit is reached
  * duration (int): amout of time, in ms, to keep client waiting before sending response
  * chances (key value int pairs): status codes and their relative chances of occuring, for example '403=1 201=3' means 1 in every 4 responses will have status code 403, and the remaining 3 will have 201


## License

Copyright © 2020 Yuri Vendruscolo da Silveira

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.

