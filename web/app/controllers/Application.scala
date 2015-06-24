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

package controllers

import javax.inject.{ Inject, Named, Singleton }

import akka.actor.ActorRef
import akka.pattern._
import akka.util.Timeout
import edu.nccu.plsm.geo.BuildInfo
import edu.nccu.plsm.geo.util.LazyLogging
import models._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.Promise
import scala.concurrent.duration._
import scala.language.postfixOps

@Singleton
class Application @Inject() (@Named("compiler") actor: ActorRef) extends Controller with LazyLogging {

  def compile = Action.async(parse.json) {
    implicit request =>
      val response = Promise[Result]()
      request.body.validate[CompileRequest] match {
        case JsSuccess(json, _) =>
          response completeWith {
            implicit val timeout = Timeout(30 seconds)
            (actor ? json) map {
              case result: CompileResult =>
                Ok(Json.obj(
                  "status" -> "OK",
                  "result" -> Json.toJson(result)
                ))
            }
          }
        case JsError(error) =>
          response trySuccess {
            BadRequest(Json.obj(
              "status" -> "Request Error",
              "result" -> JsError.toJson(error)
            ))
          }
      }
      response.future
  }

  def demo = Action {
    Ok(views.html.demo())
  }

  def sample = Action {
    Ok("""import edu.nccu.plsm.geo.dsl.DSL
         |import scala.language.{postfixOps, reflectiveCalls}
         |
         |
         |object App extends DSL {
         |  // set datum
         |  val GRS80 = SET MAJORAXIS 6378137 INVERSEFLATTENING 298.247167427
         |  val TWD97 = BASE ON GRS80 CENTRAL (121 d) SCALE_FACTOR 0.9999 X_TRANSLATION 250000
         |
         |  val p1 = (121 d 30 m 27.0176 s, 25 d 2 m 56.07074 s)
         |  // projection
         |  'p1_pro := PROJECT (p1) WITH TWD97
         |  // coverage
         |  'p1_cov := CONVERGENCE (p1) WITH TWD97
         |
         |  'a := PROJECT (121 d, 23.5 d) WITH TWD97
         |
         |}""".stripMargin)
  }

  def buildInfo = Action {
    Ok(BuildInfo.toJson).as("application/json; charset=utf-8")
  }

}