/*
 * Copyright 2021 Marcus Rosti
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

package com.mrosti.advent

import cats.implicits._
import cats.effect._
import cats.effect.implicits._
import cats.effect.IO._
import cats.effect.std.Env

import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import fs2._

import fs2.io.file._
import com.mrosti.advent.year2021._
import org.http4s.ember.client._
import org.http4s._
import org.http4s.client._
import org.typelevel.ci._
import org.http4s.client.middleware.{Logger => CLogger}

object Main extends IOApp:
  implicit def unsafeLogger[F[_]: Sync]: Logger[F] = Slf4jLogger.getLogger[F]



  def addTestHeader[F[_]: MonadCancelThrow](token: String, underlying: Client[F]): Client[F] = Client[F] { req =>
    underlying
      .run(
        req.addCookie("session", token)
      )
  }

  def adventClient[F[_]: Async](token: String, logger: Logger[F]): Resource[F, Client[F]] = 
    EmberClientBuilder
      .default[F]
      .build
      .map(CLogger(logHeaders = false, logBody = false))
      .map(addTestHeader(token, _))


  def solutions[F[_]: Async](client: Resource[F, Client[F]]): F[Seq[Unit]] = Seq(
     Problem1(client)
  ).parTraverse(identity(_))

  override def run(args: List[String]): IO[ExitCode] =
    for {
      logger <- Slf4jLogger.create[IO]
      maybeToken <- Env[IO].get("AOC_TOKEN")
      token <- IO.fromOption(maybeToken)(new RuntimeException("No token"))
      client = adventClient(token, logger)
      _ <- logger.info("Starting!")
      ans <- solutions(client)
      _ <- logger.info("Finished!")
    } yield ExitCode.Success
