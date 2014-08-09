package gov.nih.nlm.ncbi

import java.net.InetSocketAddress

import com.twitter.conversions.time._
import com.twitter.finagle.Service
import com.twitter.finagle.http.HttpMuxer
import com.twitter.server.TwitterServer
import com.twitter.util.{Await, Future, Time}
import org.jboss.netty.buffer.ChannelBuffers.copiedBuffer
import org.jboss.netty.handler.codec.http._

object AdvancedServer extends TwitterServer {

  val what = flag("what", "hello", "String to return")
  val addr = flag("bind", new InetSocketAddress(0), "Bind address")
  val durations = flag("alarms", (1.second, 5.second), "2 alarm durations")
  val counter = statsReceiver.counter("requests_counter")

  val service = new Service[HttpRequest, HttpResponse] {
    def apply(request: HttpRequest) = {
      log.debug("Received a request at " + Time.now)
      counter.incr()
      val response =
        new DefaultHttpResponse(request.getProtocolVersion, HttpResponseStatus.OK)
      val content = copiedBuffer(what() + "\n", Charsets.Utf8)
      response.setContent(content)
      Future.value(response)
    }
  }

  def main() {
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
