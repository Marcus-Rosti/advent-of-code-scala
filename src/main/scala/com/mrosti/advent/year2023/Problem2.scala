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

import cats.Order
import com.mrosti.advent.AOC
import cats.implicits.*
import cats.syntax.all.*
import cats.effect.kernel.*
import fs2.*
object Problem2 extends AOC("2023", "2"):

  sealed trait Color:
    val marbles: Int
  case class Red(override val marbles: Int) extends Color
  given Order[Red] = new Order[Red]:
    override def compare(x: Red, y: Red): Int =
      Order.compare(x.marbles, y.marbles)
  case class Green(override val marbles: Int) extends Color

  given Order[Green] = new Order[Green]:
    override def compare(x: Green, y: Green): Int =
      Order.compare(x.marbles, y.marbles)
  case class Blue(override val marbles: Int) extends Color

  given Order[Blue] = new Order[Blue]:
    override def compare(x: Blue, y: Blue): Int =
      Order.compare(x.marbles, y.marbles)
  case class Game(red: Red, green: Green, blue: Blue)

  given Order[Game] = new Order[Game]:
    override def compare(x: Game, y: Game): Int =
      Seq(
        Order.compare(x.red, y.red),
        Order.compare(x.green, y.green),
        Order.compare(x.blue, y.blue)
      ).max

  // only 12 red cubes, 13 green cubes, and 14 blue cubes?
  val part1Maximum: Game = Game(Red(12), Green(13), Blue(14))

  def parseColor(color: String): Option[Color] =
    color match
      case s"$i red"   => Red(i.strip().toInt).pure
      case s"$i blue"  => Blue(i.strip().toInt).pure
      case s"$i green" => Green(i.strip().toInt).pure
      case _           => Option.empty[Color]

  def parseGame(game: String): Game =
    val colors: Seq[Color] = game.split(",").flatMap(parseColor)
    val red                = colors.collectFirst { case r: Red => r }.getOrElse(Red(0))
    val green              = colors.collectFirst { case r: Green => r }.getOrElse(Green(0))
    val blue               = colors.collectFirst { case r: Blue => r }.getOrElse(Blue(0))
    Game(red, green, blue)

  def parseLine(games: String): Seq[Game] =
    games.split(";").map(parseGame)

  case class Index(idx: Int, games: Seq[Game])

  override def part1[F[_]: Async](input: Stream[F, String]): F[String] =
    input
      .map { case s"Game $i: $theRest" => Index(i.toInt, parseLine(theRest)) }
      .filter(_.games.forall(Order.lteqv(_, part1Maximum)))
      .map(_.idx)
      .compile
      .fold(0)(_ + _)
      .map(_.toString)

  def minMax(games: Seq[Game]): Int =
    games.map(_.red.marbles).max *
      games.map(_.green.marbles).max *
      games.map(_.blue.marbles).max

  override def part2[F[_]: Async](input: Stream[F, String]): F[Option[String]] =
    input
      .map { case s"Game $_: $theRest" => parseLine(theRest) }
      .map(minMax)
      .compile
      .fold(0)(_ + _)
      .map(_.toString.pure)
