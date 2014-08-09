# Twitter-server example

##Setup

Requires Scala version 2.10.x or 2.9.x (Twitter doesn't yet support 2.11), as well as sbt up to 0.13.

####Mac

Brew instructions for a mac running [Homebrew](http://brew.sh/).

`brew tap caskroom/versions`

`brew cask install java7`

`brew switch scala 2.10.3`

`brew install sbt`

####CentOS/RH-based system

Instructions for yum-based systems

`wget http://www.scala-lang.org/files/archive/scala-2.10.4.rpm`

`yum install scala-2.10.4.rpm`

`wget http://dl.bintray.com/sbt/rpm/sbt-0.13.5.rpm`

`yum install sbt-0.13.5.rpm`


####Both

`echo SBT_OPTS="-XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=256M"' >> ~/.sbtconfig`

`sbt assembly`

`./run.sh`

## Use

Check out the [twitter-server](http://twitter.github.io/twitter-server/Features.html) docs to see what flags are avaiable (some are set in run.sh), as well as the various admin and metrics endpoints.  For example:

`curl -s localhost:8080/admin/metrics.json?pretty=true`

and

`curl -s localhost:8080/admin/server_info`