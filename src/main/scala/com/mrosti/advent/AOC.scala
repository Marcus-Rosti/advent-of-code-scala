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

import cats.*
import cats.effect.*
import cats.effect.implicits.*
import cats.effect.kernel.*
import cats.effect.std.Env
import cats.implicits.*

import fs2.*
import org.typelevel.log4cats.slf4j.*
import org.typelevel.log4cats.syntax.*
import org.http4s.*
import org.http4s.client.*
import org.http4s.implicits.*
import scala.concurrent.duration.*

import com.mrosti.advent.ReadInput
import org.typelevel.log4cats.Logger

trait AOC(year: String, day: String):
  def file[F[_]: Async: Env]: Resource[F, Stream[F, String]] = ReadInput(year, day)

  def part1[F[_]: Async](input: Stream[F, String]): F[String]
  def part2[F[_]: Async](input: Stream[F, String]): F[Option[String]]

  def runAndTime[F[_]: Async, B: Show](fSol: F[B], part: String) = for {
      logger <- Slf4jLogger.create[F]
      (finish, sol) <- Clock[F].timed(fSol)
        _ <- logger.info(s"$year/$day/$part :: ${sol.show} in ${finish.toMillis}ms")
      } yield ()
  def both[F[_]: Async](input: Stream[F, String]): F[Unit] = 
    Seq(
      runAndTime(part1(input), "part1"),
      runAndTime(part2(input), "part2")
      )
      .sequence
      .map(_ => Async[F].unit)
  def apply[F[_]: Async: Env](): F[Unit] = file.use(both)
