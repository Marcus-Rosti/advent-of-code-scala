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

package com.mrosti.advent.year2022

import com.mrosti.advent.AOC
import cats.*
import cats.implicits.*
import cats.effect.kernel.*
import fs2.*
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Problem2 extends AOC("2022", "2"):

  sealed trait Play
  case object Rock     extends Play
  case object Paper    extends Play
  case object Scissors extends Play

  final case class Round(them: Play, you: Play)

  given Show[Play] = new Show[Play] {
    def show(p: Play): String = p match
      case Rock     => "Rock"
      case Paper    => "Paper"
      case Scissors => "Scissors"
  }

  given Show[Round] = (t: Round) => t.them.show + " vs " + t.you.show

  given Order[Play] = new Order[Play]:
    def compare(them: Play, you: Play): Int =
      (them, you) match
        case (Rock, Paper)     => 6
        case (Paper, Scissors) => 6
        case (Scissors, Rock)  => 6
        case (Rock, Scissors)  => 0
        case (Paper, Rock)     => 0
        case (Scissors, Paper) => 0
        case _                 => 3

  object PlayValue:
    def apply(p: Play): Int = p match
      case Rock     => 1
      case Paper    => 2
      case Scissors => 3

  object Play:
    def apply(thing: String): Play =
      thing match
        case "A" | "X" => Rock
        case "B" | "Y" => Paper
        case "C" | "Z" => Scissors

  object Score:
    def apply(round: Round): Int =
      round match
        case Round(them, you) => (them compare you) + PlayValue(you)

  object Parse2:
    def apply(them: String, you: String) =
      you match
        // lose
        case "X" =>
          Play(them) match
            case Rock     => Score(Round(Rock, Scissors))
            case Paper    => Score(Round(Paper, Rock))
            case Scissors => Score(Round(Scissors, Paper))
        // draw
        case "Y" =>
          Play(them) match
            case Rock     => Score(Round(Rock, Rock))
            case Paper    => Score(Round(Paper, Paper))
            case Scissors => Score(Round(Scissors, Scissors))
        // win
        case "Z" =>
          Play(them) match
            case Rock     => Score(Round(Rock, Paper))
            case Paper    => Score(Round(Paper, Scissors))
            case Scissors => Score(Round(Scissors, Rock))

  override def part1[F[_]: Async](input: Stream[F, String]): F[String] =
    input
      .map(_.split(" ").map(Play(_)))
      .collect { case Array(them: Play, you: Play) => Round(them, you) }
      .map(Score(_))
      .fold(0)(_ + _)
      .head
      .compile
      .toList
      .map(_.head.show)

  override def part2[F[_]: Async](input: Stream[F, String]): F[Option[String]] = input
    .map(_.split(" "))
    .collect { case Array(them: String, you: String) => Parse2(them, you) }
    .fold(0)(_ + _)
    .head
    .compile
    .toList
    .map(_.head.show.some)
