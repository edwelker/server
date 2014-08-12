package gov.nih.nlm.ncbi

import java.net.InetSocketAddress

import com.twitter.conversions.time._
import com.twitter.finagle.Http
import com.twitter.finagle.Service
import com.twitter.finagle.zookeeper._
import com.twitter.finagle.http.HttpMuxer
import com.twitter.io.Charsets
import com.twitter.server.TwitterServer
import com.twitter.util.{Await, Future, Time}
import org.jboss.netty.buffer.ChannelBuffers.copiedBuffer
import org.jboss.netty.handler.codec.http._

import scala.collection.JavaConverters._

object Server extends TwitterServer {

  val what = flag("what", "hello", "String to return")
  val addr = flag("bind", new InetSocketAddress(0), "Bind address")
  val durations = flag("alarms", (1.second, 5.second), "2 alarm durations")
  val counter = statsReceiver.counter("requests_counter")
 
  val zookeeperClient = new ZookeeperClient(1.second, "localhost:2181")
  val serverSet = new ServerSetImpl(zk.zookeeperClient, "testservice")
  val cluster = new ZookeeperServerSetCluster(serverSet)

  cluster.join(serverHost)

  val service = new Service[HttpRequest, HttpResponse] {
    def apply(request: HttpRequest) = {
      log.debug("Received a request at " + Time.now)
      counter.incr()

      //get the query string parameters, Map<String, List<String>>
      val params = new QueryStringDecoder(request.getUri()).getParameters()

      //convert them to a string (tho they would be auto-cast elsewhere
      //val sparams: String = params.toString

      //map over Map items, do positional unpacking
      val hparams = params.asScala.map{
	 case i => <dt>{i._1}</dt><dd>{i._2}</dd>
	 case _ => <i>None</i>
      }

      //string interpolation within a xml literal. weird.
      val resp = <html><head><title>whatever</title></head><body><h1>The query string params sent</h1><dl>{ hparams }</dl></body></html>

      val response =
        new DefaultHttpResponse(request.getProtocolVersion, HttpResponseStatus.OK)
      val content = copiedBuffer(resp + "\n", Charsets.Utf8)
      response.setContent(content)
      response.setHeader(HttpHeaders.Names.CONTENT_TYPE, "text/html")
      Future.value(response)
    }
  }

  //so the quickstart uses sbt to imply which main to use
  def main() {
    val server = Http.serveAndAnnounce("zk!localhost:2181/a", ":8080", service)
    // We can create a new http server but in that case we profit from the
    // one already started for /admin/*
    // The `TwitterServer` trait exposes an `adminHttpServer` that serve all routes
    // registered in the HttpMuxer object, we just have to add our own.
    HttpMuxer.addHandler("/echo", service)
    HttpMuxer.addHandler("/echo/", service)
    // And wait on the server
    Await.ready(adminHttpServer)
  }
}
