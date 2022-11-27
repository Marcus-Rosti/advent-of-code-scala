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

package com.mrosti.advent.year2021

import cats._
import cats.effect._
import cats.effect.kernel._
import cats.effect.std.Env
import cats.implicits._

import fs2._
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.http4s._
import org.http4s.client._
import org.http4s.implicits._

import com.mrosti.advent.ReadInput

trait AOC(year: String, day: String):
  def file[F[_]: Async: Env]: Resource[F, Stream[F, String]] = ReadInput(year, day)

  def part1[F[_]: Async](input: Stream[F, String]): F[String]
  def part2[F[_]: Async](input: Stream[F, String]): F[Option[String]]

  def apply[F[_]: Async: Env](): F[Unit] =
    for {
      logger <- Slf4jLogger.create[F]
      startTime <- Sync[F].realTime
      sol1 <- file.use(input => part1(input))
      finish1 <- Sync[F].realTime
      _ <- logger.info(s"$year/$day/part1 :: $sol1 in ${finish1 - startTime}")
      sol2 <- file.use(input => part2(input))
      finish2 <- Sync[F].realTime
      _ <- logger.info(s"$year/$day/part2 :: ${sol2.show} in ${finish2 - finish1}")
    } yield ()
