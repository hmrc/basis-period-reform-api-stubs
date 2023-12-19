/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.basisperiodreformapistubs.controllers

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

import controllers.Assets

import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, ControllerComponents, Result}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

@Singleton()
class StubController @Inject() (assets: Assets, cc: ControllerComponents)(implicit ec: ExecutionContext)
    extends BackendController(cc) {
  private case class StubParameters(path: String, utr: Option[String], part: Option[String])

  private val errors: Map[StubParameters, Int] = List(
    StubParameters("iv_overlap_relief_partnership", Some("987654321"), None) -> 400,
    StubParameters("iv_overlap_sole_trader", Some("1000000A2"), None)        -> 400
  ).toMap

  def stub(path: String, utr: Option[String], part: Option[String]): Action[AnyContent] = Action.async { implicit request =>
    val notFound =
      s"""
         |{
         |  "name": "$path",
         |  "elements": [],
         |  "links": [
         |    {
         |      "rel": "self",
         |      "href": "https://vdp.dev.denodo.hip.ns2n.corp.hmrc.gov.uk:9443/server/basis_period_reform/ws_overlap_relief_sole_trader/views/iv_overlap_relief_sole_trader?utr=100000002"
         |    }
         |  ]
         |}
         |
         |""".stripMargin
    val fullPath = s"/stubs/$path/"
    val file     = s"utr${utr.getOrElse("")}_part${part.getOrElse("")}.json"
    assets.at(fullPath, file)(request).map { result =>
      if (result.header.status == 404) NotFound(Json.parse(notFound))
      new Result(result.header.copy(status = errors.getOrElse(StubParameters(path, utr, part), 200)), result.body)
    }
  }
}
