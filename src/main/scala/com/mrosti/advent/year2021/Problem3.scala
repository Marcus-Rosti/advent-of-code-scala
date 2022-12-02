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

import com.mrosti.advent.AOC
import cats.effect._
import cats.implicits._
import fs2._

object Problem3 extends AOC("2021", "3"):
  case class InputRow()

  override def part1[F[_]: Async](input: Stream[F, String]): F[String] =
    Async[F].pure("")
  override def part2[F[_]: Async](input: Stream[F, String]): F[Option[String]] =
    Async[F].pure(Option.empty)
