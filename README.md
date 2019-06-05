# Configuration needed to build the project (Docker will obviate the need for these steps)


Add 

`resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"`

to `~/.sbt/1.0/global.sbt`

According to this SO answer:
https://stackoverflow.com/questions/47456328/unresolved-dependency-com-artima-supersafesupersafe-2-12-41-1-3-not-found

# Build

The build is using quite a lot of additional compiler plug-ins. Most of these
are either for nice functional capabilities, like partial unification, or for
ensuring cleaner code, like failing on warnings.

These are mostly taken whole-cloth from
https://tpolecat.github.io/2017/04/25/scalac-flags.html

# How to be awesome

Include

`addSbtPlugin("com.orrsella" % "sbt-stats" % "1.0.7")`

`addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.1")`

in your global sbt config (`~/.sbt/1.0/plugins/plugins.sbt`)

and then run

`sbt clean coverage test coverageReport doc stats`

to generate a test coverage report, create the scaladoc, and get general stats
on the project. Coverage report and scaladoc both create html files in the
target directory. Find uncovered code and write tests.

TODO 

We just moved a general getEdgesBetween method to the GraphBackend trait instead of a specific implementation. Do that for everything else
The goal is that we should only need to implement about 4 abstract methods to write a new backend.
