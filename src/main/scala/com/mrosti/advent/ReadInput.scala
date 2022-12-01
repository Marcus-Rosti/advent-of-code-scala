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

import cats.effect.std.Env
import cats._
import cats.effect._
import cats.syntax.all._
import fs2._
import fs2.text
import fs2.io.file._
import fs2.io.file.{Path => FPath}
import org.http4s.ember.client._
import org.http4s._
import org.http4s.client._
import org.http4s.client.middleware.{Logger => CLogger}
import org.http4s.implicits._
import org.typelevel.log4cats.slf4j.Slf4jLogger

object ReadInput:

  private def aocUrl(year: String, day: String) =
    uri"https://adventofcode.com" / year / "day" / day / "input"
  private def inputdirectory(year: String, day: String) = Path(s"./data/$year/day/$day")
  private def inputFile(year: String, day: String) = Path(s"./data/${year}/day/$day/input")

  private def error[F[_]: MonadCancelThrow](path: Path) =
    MonadCancelThrow[F].raiseError[Stream[F, Byte]](new NoSuchFileException(path.show))

  private def fileIfExists[F[_]: Async](year: String, day: String): F[Stream[F, Byte]] = {
    for {
      logger <- Slf4jLogger.create[F]
      _ <- logger.debug(s"Loading from file $year / $day")
      iFile = inputFile(year, day)
      exists <- Files[F].exists(iFile)
      ret <- Option.when(exists)(Files[F].readAll(iFile).pure).getOrElse(error(iFile))
    } yield ret
  }
  private def downloadFile[F[_]: Async: Env](year: String, day: String): F[Unit] =
    for {
      maybeToken <- Env[F].get("AOC_TOKEN")
      token <- Async[F].fromOption(maybeToken, new RuntimeException("No token"))
      _ <- Files[F].createDirectories(inputdirectory(year, day))
      _ <- Files[F].createFile(inputFile(year, day))
      _ <- adventClient(token).use(
        _.stream(Request[F](uri = aocUrl(year, day)))
          .flatMap(_.body)
          .through(Files[F].writeAll(inputFile(year, day), Flags.Write))
          .compile
          .drain)
    } yield ()

  private def cacheAndLoadFile[F[_]: Async: Env](
      year: String,
      day: String): F[Stream[F, Byte]] =
    for {
      logger <- Slf4jLogger.create[F]
      _ <- logger.trace(s"Downloading file for $year / $day")
      _ <- downloadFile(year, day)
      _ <- logger.trace(s"Downloaded file for $year / $day")
      f <- fileIfExists(year, day)
      _ <- logger.trace(s"Returning file for $year / $day")
    } yield f

  private def addTestHeader[F[_]: MonadCancelThrow](
      token: String,
      underlying: Client[F]): Client[F] = Client[F] { req =>
    underlying.run(
      req.addCookie("session", token)
    )
  }

  private def adventClient[F[_]: Async](token: String): Resource[F, Client[F]] =
    EmberClientBuilder
      .default[F]
      .build
      .map(CLogger(logHeaders = false, logBody = false))
      .map(addTestHeader(token, _))

  def apply[F[_]: Async: Env](year: String, day: String): Resource[F, Stream[F, String]] = {
    Resource
      .make(fileIfExists(year, day).recoverWith(_ => cacheAndLoadFile(year, day)))(_ =>
        Async[F].unit)
      .map(_.through(text.utf8.decode).through(text.lines).dropLast)
  }
