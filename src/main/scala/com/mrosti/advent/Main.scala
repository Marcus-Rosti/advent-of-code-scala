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
import org.typelevel.ci._

object Main extends IOApp:

  def solutions[F[_]: Async: Env]: F[Seq[Unit]] = Seq(
    Problem1(),
    Problem2(),
    Problem3()
  ).parTraverse(identity(_))

  override def run(args: List[String]): IO[ExitCode] =
    for {
      logger <- Slf4jLogger.create[IO]
      _ <- logger.info("Starting!")
      ans <- solutions
      _ <- logger.info("Finished!")
    } yield ExitCode.Success
