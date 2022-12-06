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

import cats.*
import cats.implicits.*
import com.mrosti.advent.AOC
import cats.effect.kernel.Async
import fs2.*
import java.util.Stack
import cats.data.State

import cats.parse.Parser
import cats.syntax.validated
import scala.annotation.tailrec

object Problem5 extends AOC("2022", "5"):

  type Stack = Seq[Char]
  type SwapStack = (Stack, Stack, Int) => (Stack, Stack)

  final case class Move(amount: Int, from: Int, to: Int)
  object Move:
    val reg = raw"move (\d+) from (\d+) to (\d+)".r
    def apply(str: String): Option[Move] =
      str match 
        case reg(a,b,c) => Option(Move(a.toInt,b.toInt,c.toInt))
        case _ => Option.empty

  @tailrec
  def part1Swap(fromStack: Stack, toStack: Stack, amount: Int): (Stack, Stack) =
    if (amount == 0) (fromStack, toStack)
    else part1Swap(fromStack.tail, fromStack.headOption.toSeq ++ toStack, amount - 1)

  def part2Swap(fromStack: Stack, toStack: Stack, amount: Int): (Stack, Stack) =
    (fromStack.drop(amount), fromStack.take(amount) ++ toStack)


  def applyStep(step: SwapStack)(initialState: Map[Int, Stack], move: Move): Map[Int, Stack] = (for {
    fromStack <- initialState.get(move.from)
    toStack <- initialState.get(move.to)
    (newFromStack, newToStack) = step(fromStack, toStack, move.amount)
  } yield initialState + (move.from -> newFromStack) + (move.to -> newToStack)).getOrElse(Map.empty)

  def applyAllSteps(step: SwapStack)(initialState: Map[Int, Stack]): Seq[Move] => Map[Int, Stack] = 
    _.foldl(initialState)(applyStep(step))


  val regex = raw"""(\[[A-Z]\] ?|(   ) ?)""".r
  def parseInput(header: Seq[String], footer: Seq[String]): (Map[Int, Stack], Seq[Move]) = {
    val matches = header.map(regex.findAllMatchIn(_).toSeq.map(_.toString.strip))
    val stacks = matches.map(_.map(
      _.toCharArray match
        case Array('[', a, ']') => a
        case _ => ' '
     ))
    val initialState = stacks.transpose.map(_.filterNot(_.isWhitespace))
    .zipWithIndex.map((c, i) => (i+1, c)).toMap
    
    (initialState, footer.flatMap(Move(_)))
  }
  override def part1[F[_]: Async](input: Stream[F, String]): F[String] = 
    for {
      h <- input.take(8).compile.toList
      f <- input.drop(10).compile.toList
      (ini, mov) = parseInput(h, f)
    } yield applyAllSteps(part1Swap)(ini)(mov).toSeq.sortBy(_._1).map(_._2).map(_.head).mkString

  override def part2[F[_]: Async](input: Stream[F, String]): F[Option[String]] =    for {
      h <- input.take(8).compile.toList
      f <- input.drop(10).compile.toList
      (ini, mov) = parseInput(h, f)
    } yield applyAllSteps(part2Swap)(ini)(mov).toSeq.sortBy(_._1).map(_._2).map(_.head).mkString.pure

