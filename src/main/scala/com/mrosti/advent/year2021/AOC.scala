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

import cats.effect._
import cats.implicits._

import fs2._
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

trait AOC(name: String) {
  implicit private def unsafeLogger[F[_]: Sync]: Logger[F] = Slf4jLogger.getLogger[F]

  def part1[F[_]](input: Stream[F, String]): F[String]
  def part2[F[_]](input: Stream[F, String]): F[String]

  def apply[F[_]: Async](lines: Stream[F, String]): F[Unit] = for {
    sol1 <- part1(lines)
    _ <- Logger[F].info(s"$name :: Solution 1: $sol1")
    sol2 <- part2(lines)
    _ <- Logger[F].info(s"$name :: Solution 2: $sol2")
  } yield Sync[F].unit
}

case class Solution(name: )
