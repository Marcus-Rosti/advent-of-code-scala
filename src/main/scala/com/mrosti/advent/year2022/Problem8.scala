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
import cats.implicits.*
import cats.effect.implicits.*
import cats.effect.kernel.Async
import fs2.*
import cats.Applicative

object Problem8 extends AOC("2022", "8"):

  type Height = Int

  type Grid = Map[(Int, Int), Height]

  def readGrid[F[_]: Async](input: Stream[F, String]): F[Grid] =
    input
      .map(_.map(_.toString.toInt).zipWithIndex)
      .zipWithIndex
      .flatMap((row, y) => Stream.emits(row.map((h, x) => (x, y.toInt) -> h)))
      .compile
      .toList
      .map(_.toMap)

  // O((i*j)^2)
  def isVisible(maxX: Int, maxY: Int, grid: Grid)(x: Int, y: Int): Int = {
    if (x == maxX || x == 0 || y == 0 || y == maxX) 1
    else
      val left  = (0 to (x - 1)).map((_, y)).reverse
      val right = ((x + 1) to maxX).map((_, y))
      val above = (0 to (y - 1)).map((x, _)).reverse
      val below = ((y + 1) to maxY).map((x, _))

      val height = grid.getOrElse((x, y), -1)

      Seq(left, right, above, below)
        .find(_.flatMap(grid.get).forall(_ < height))
        .fold(0)(_ => 1)
  }

  def visibilityScore(maxX: Int, maxY: Int, grid: Grid)(x: Int, y: Int): Int = {

    val left  = (0 to (x - 1)).map((_, y)).reverse
    val right = ((x + 1) to maxX).map((_, y))
    val above = (0 to (y - 1)).map((x, _)).reverse
    val below = ((y + 1) to maxY).map((x, _))

    val height = grid.get((x, y)).get
    Seq(left, right, above, below)
      .map(lst =>
        val shorter = lst.takeWhile((i, j) => grid.get((i, j)).get < height).size
        if shorter == lst.size
        then shorter
        else shorter + 1
      )
      .product
  }

  override def part1[F[_]](input: Stream[F, String])(using Async[F]): F[String] =
    readGrid(input).map { grid =>
      val (maxX, maxY) = grid.keySet.max
      (for {
        x <- 0 to maxX
        y <- 0 to maxY
      } yield isVisible(maxX, maxY, grid)(x, y)).sum.show
    }

  override def part2[F[_]](input: Stream[F, String])(using Async[F]): F[Option[String]] =
    readGrid(input).map { grid =>
      val (maxX, maxY) = grid.keySet.max
      (for {
        x <- 0 to maxX
        y <- 0 to maxY
      } yield visibilityScore(maxX, maxY, grid)(x, y)).max.show.some
    }
