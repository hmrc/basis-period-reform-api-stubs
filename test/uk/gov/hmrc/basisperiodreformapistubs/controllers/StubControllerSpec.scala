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

import scala.concurrent.Future

import org.scalatest.OptionValues
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.WsScalaTestClient
import org.scalatestplus.play.guice.GuiceOneAppPerSuite

import play.api.http.Status
import play.api.mvc.Result
import play.api.test.Helpers._
import play.api.test.{DefaultAwaitTimeout, FakeRequest, FutureAwaits}

class StubControllerSpec extends AnyWordSpec with Matchers with OptionValues with WsScalaTestClient
    with DefaultAwaitTimeout with FutureAwaits with GuiceOneAppPerSuite {
  implicit def mat: org.apache.pekko.stream.Materializer = app.injector.instanceOf[org.apache.pekko.stream.Materializer]

  "GET" should {
    "return 200 and a file for sole trader" in {
      val result = doGet("/iv_overlap_relief_sole_trader?utr=100000002")
      status(result) shouldBe Status.OK
    }
    "return 400 and a file for sole trader" in {
      val result = doGet("/iv_overlap_relief_sole_trader?utr=1000000A2")
      status(result) shouldBe Status.BAD_REQUEST
    }
    "return 200 and a file for relief partnership" in {
      val result = doGet("/iv_overlap_relief_partnership?utr=987654321&partnership_ref_number=5600000015")
      status(result) shouldBe Status.OK
    }
    "return 400 and a file for relief partnership" in {
      val result = doGet("/iv_overlap_relief_partnership?utr=987654321")
      status(result) shouldBe Status.BAD_REQUEST
    }

    "return 200 and empty if not found" in {
      val result = doGet("/iv_overlap_relief_partnership?utr=ABCDEFGH")
      status(result) shouldBe Status.OK
    }
  }

  def doGet(uri: String): Future[Result] = {
    val fakeRequest = FakeRequest(GET, uri)
    route(app, fakeRequest).get
  }
}
