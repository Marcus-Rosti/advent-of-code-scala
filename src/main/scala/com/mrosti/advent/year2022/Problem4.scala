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
import cats.effect.kernel.Async
import fs2.*
import cats.data.Op

object Problem4 extends AOC("2022", "4"):

  object ParseRange:
    def apply(range: String): Set[Int] =
        range
        .split("-")
        .map(_.toInt) match
          case Array(a: Int, b: Int) => Seq.range(a, b+1).toSet
  
  private def sol[F[_]: Async, A](func: ((Set[Int], Set[Int])) => Boolean)(input: Stream[F, String]) =
    input
      .map(_.split(",").map(ParseRange(_)))
      .collect{
        case Array(a: Set[Int], b: Set[Int]) => (a, b)
      }
      .filter(func)
      .compile
      .count
      .map(_.show)
      

  override def part1[F[_]: Async](input: Stream[F, String]): F[String] = 
    sol((a,b) => a.subsetOf(b) || b.subsetOf(a))(input)

        

  override def part2[F[_]: Async](input: Stream[F, String]): F[Option[String]] =    
    sol((a,b) => a.intersect(b).nonEmpty)(input).map(_.pure)
