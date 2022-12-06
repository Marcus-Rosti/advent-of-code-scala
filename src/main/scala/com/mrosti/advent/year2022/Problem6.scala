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
object Problem6 extends AOC("2022", "6"):

  // https://stackoverflow.com/questions/58193156/interleave-multiple-streams
  def zipAll[F[_], A](streams: Seq[Stream[F, A]]): Stream[F, A] =
    streams
      .foldLeft[Stream[F, Seq[Option[A]]]](Stream.empty) { (acc, s) =>
        zipStreams(acc, s)
      }
      .flatMap(l => Stream.emits(l.reverse.flatten))

  def zipStreams[F[_], A](s1: Stream[F, Seq[Option[A]]], s2: Stream[F, A]): Stream[F, Seq[Option[A]]] =
    s1.zipAllWith(s2.map(Option(_)))(Nil, Option.empty[A]) { case (acc, a) => a +: acc }

  def getChunks[F[_]](stream: Stream[F, Char], windowSize: Int): Stream[F, Chunk[Char]] =
    zipAll(Seq.range(0, windowSize).map(i => stream.drop(i).chunkN(windowSize, false)))

  def parseStream(stream: Stream[Pure, Char], windowSize: Int): Option[String] = 
    getChunks(stream, windowSize)
      .zipWithIndex
      .filter((chunk, index) => chunk.to(Set).size==windowSize)
      .map(_._2)
      .compile
      .toList
      .headOption
      .map(_ + windowSize)
      .map(_.show)

  override def part1[F[_]](input: Stream[F, String])(using Async[F]): F[String] = 
    for {
      t <- input.compile.toList.map(_.toSeq)
    } yield parseStream(Stream.emits(t.head.toCharArray), 4).get
  override def part2[F[_]](input: Stream[F, String])(using Async[F]): F[Option[String]] =
    for {
      t <- input.compile.toList.map(_.toSeq)
    } yield parseStream(Stream.emits(t.head.toCharArray), 14)

