# Twitter-server example

##Setup

Brew instructions for a mac running [Homebrew](http://brew.sh/).

`brew tap caskroom/versions`

`brew cask install java7`

`brew switch scala 2.10.3`

`brew install sbt`

`echo SBT_OPTS="-XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=256M"' >> ~/.sbtconfig`

`sbt assembly`

`./run.sh`

## Use

Check out the [twitter-server](http://twitter.github.io/twitter-server/Features.html) docs to see what flags are avaiable (some are set in run.sh), as well as the various admin and metrics endpoints.  For example:

`curl -s localhost:8080/admin/metrics.json?pretty=true`

and

`curl -s localhost:8080/admin/server_info`