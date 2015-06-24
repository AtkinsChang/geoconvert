/*
 * Licensed to the Programming Language and Software Methodology Lab (PLSM)
 * under one or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership.
 * The PLSM licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package actors

import java.util.concurrent.TimeUnit
import javax.inject.Inject

import akka.actor.{ Actor, DiagnosticActorLogging }
import com.google.common.base.Stopwatch
import edu.nccu.plsm.geo.projection.{ LatLng, Result }
import models.{ GoogleMapPoint, CompileRequest, CompileResult }
import play.api.cache.{ CacheApi, NamedCache }
import play.api.libs.concurrent.InjectedActorSupport

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.reflect.runtime
import scala.tools.reflect.{ ToolBoxError, ToolBox }

class Compiler @Inject() (@NamedCache("main-cache") protected val cacheApi: CacheApi)
    extends Actor with InjectedActorSupport with DiagnosticActorLogging {
  private[this] lazy val mirror = runtime.currentMirror
  private[this] lazy val toolbox = mirror.mkToolBox()

  override def receive: Receive = {
    case CompileRequest(code) => sender() ! compile(s"$code\nApp.resultMap")
  }

  def compile(code: String): CompileResult = {
    type Map[A, +B] = scala.collection.Map[A, B]
    def run(f: () => Any) = {
      def format(name: String, from: LatLng, result: String) = {
        val fromStr = s"(${from.longitude},${from.latitude})"
        f"- $name%-10s: $fromStr%-40s => $result%s"
      }
      f().asInstanceOf[Map[Symbol, Result]].map {
        case (symbol, r) if r.result.length == 1 =>
          val result = s"${r.result.head} ${r.unit}"
          val log = format(symbol.name, r.from, result)
          GoogleMapPoint(
            symbol.name,
            log,
            views.html.infowindow(symbol, r).body,
            LatLng(r.from.latDegree, r.from.lngDegree)
          )
        case (symbol, r) if r.result.length == 2 =>
          val result = s"(${r.result.head} ${r.unit}, ${r.result.last} ${r.unit})"
          val log = format(symbol.name, r.from, result)
          GoogleMapPoint(
            symbol.name,
            log,
            views.html.infowindow(symbol, r).body,
            LatLng(r.from.latDegree, r.from.lngDegree)
          )
      }.toSeq
    }
    val ticker = Stopwatch.createStarted
    cacheApi.get[() => Any](code) match {
      case Some(f) =>
        val elapsed = ticker.elapsed(TimeUnit.MILLISECONDS)
        ticker.reset
        log info s"Retrieved compiled result from cache, cost $elapsed ms"
        CompileResult(success = true, run(f), elapsed)
      case _ =>
        log.info("Toolbox compiling...")
        try {
          val f = toolbox compile (toolbox parse code)
          cacheApi.set(code, f, 10 minutes)
          val elapsed = ticker.elapsed(TimeUnit.MILLISECONDS)
          ticker.reset
          log info s"Done, cost $elapsed ms"
          CompileResult(success = true, run(f), elapsed)
        } catch {
          case error: ToolBoxError =>
            val message = error.getLocalizedMessage
            log.warning(message, error)
            CompileResult(error = Seq(message, error.getStackTrace mkString "\n"))
        }
    }
  }
}
