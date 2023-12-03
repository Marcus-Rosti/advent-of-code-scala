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

package com.mrosti.advent.year2023

import com.mrosti.advent.AOC
import cats.implicits.*
import cats.syntax.all.*
import cats.effect.kernel.*
import fs2.*
object Problem1 extends AOC("2023", "1"):

  private val digitNames: Seq[(String, Int)] = Seq(
    "one"   -> 1,
    "two"   -> 2,
    "three" -> 3,
    "four"  -> 4,
    "five"  -> 5,
    "six"   -> 6,
    "seven" -> 7,
    "eight" -> 8,
    "nine"  -> 9
  )

  // to hell with regex
  private def replace(str: String): String =
    digitNames.foldLeft(str) {
      case (filtered, (name, int)) =>
        filtered.replace(name, int.toString)
    }

  private def firstAndLast(digits: String): Option[String] = for {
    h <- digits.headOption
    l <- digits.lastOption
  } yield f"$h$l"

  override def part1[F[_]: Async](input: Stream[F, String]): F[String] =
    input
      .map(_.filter(_.isDigit))
      .mapFilter(firstAndLast)
      .map(_.toInt)
      .compile
      .fold(0)(_ + _)
      .map(_.toString)

  override def part2[F[_]: Async](input: Stream[F, String]): F[Option[String]] =
    input
      .map(replace)
      .map(_.filter(_.isDigit))
      .mapFilter(firstAndLast)
      .map(_.toInt)
      .compile
//      .fold(0)(_ + _)
      .toList
      .map(_.toString.pure)
