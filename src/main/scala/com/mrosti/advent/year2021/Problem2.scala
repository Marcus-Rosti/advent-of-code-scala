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

object Problem2 extends AOC("2021", "2"):

    case class Position(horizontal: Long, depth: Long, aim: Long)
    object Position:    
        lazy val start: Position = Position(0,0,0)

    sealed trait Move
    case class Forward(x: Long) extends Move
    case class Down(x: Long) extends Move
    case class Up(x: Long) extends Move

    object Move:
        def parse(command: String): Option[Move] =
            command.split(" ") match {
                case Array("forward" , x: String) => x.toLongOption.map(Forward(_))
                case Array("down" , x: String) => x.toLongOption.map(Down(_))
                case Array("up" , x: String) => x.toLongOption.map(Up(_))
                case _ => None
            }
        
        def apply(p: Position, move: Move): Position = move match
            case Forward(x) => Position(p.horizontal + x, p.depth + p.aim * x, p.aim)
            case Down(x) => Position(p.horizontal, p.depth, p.aim + x)
            case Up(x) => Position(p.horizontal, p.depth, p.aim - x)
        

    def solve[F[_]: Async](input: Stream[F, String]): F[Position] = 
        input.map(Move.parse).collect{
            case Some(value) => value
        }
        .compile
        .fold(Position.start)((p, m) => Move(p, m))

        

    override def part1[F[_]: Async](input: Stream[F, String]): F[String] = 
        solve(input).map(p => p.horizontal * p.aim).map(_.show)

    override def part2[F[_]: Async](input: Stream[F, String]): F[Option[String]] = 
        solve(input).map(p => p.horizontal * p.depth).map(_.show).map(_.some)

